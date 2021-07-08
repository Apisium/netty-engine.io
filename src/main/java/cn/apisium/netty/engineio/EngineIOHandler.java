package cn.apisium.netty.engineio;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoWebSocket;
import io.socket.parseqs.ParseQS;
import org.jetbrains.annotations.NotNull;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class EngineIOHandler extends SimpleChannelInboundHandler<Object> {
    private final EngineIoServer server;
    private WebSocketServerHandshaker hs;
    private EngineIoWebSocketImpl socket;
    public EngineIOHandler(@NotNull EngineIoServer server) {
        this.server = server;
    }
    @SuppressWarnings("RedundantCast")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof FullHttpRequest) {
            FullHttpRequest msg = (FullHttpRequest) obj;
            if (!msg.uri().startsWith("/socket.io")) {
                ctx.fireChannelRead(msg);
                return;
            }
            if (!msg.decoderResult().isSuccess()) {
                ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
                return;
            }
            if ("websocket".equals(msg.headers().get("Upgrade"))) {
                 hs = new WebSocketServerHandshakerFactory(
                        ((FullHttpRequest) obj).uri().replaceAll("^http", "") + "ws",
                        null, false).newHandshaker(msg);
                if (hs == null) WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                else {
                    hs.handshake(ctx.channel(), msg).addListener(it -> {
                        if (!it.isSuccess()) return;
                        socket = new EngineIoWebSocketImpl(ctx.channel(), msg);
                        server.handleWebSocket(socket);
                    });
                }
                return;
            }
            if (HttpUtil.is100ContinueExpected(msg)) ctx.channel().writeAndFlush(new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.CONTINUE));
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            HttpServletResponseImpl resp = new HttpServletResponseImpl(response);
            server.handleRequest(new HttpServletRequestImpl(msg), resp);
            System.out.println(msg.uri());
            boolean keepAlive = HttpUtil.isKeepAlive(msg);
            if (keepAlive) response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            ChannelFuture cf = ctx.channel().writeAndFlush(response);
            if (!keepAlive) cf.addListener(ChannelFutureListener.CLOSE);
        } else if (obj instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) obj;
            if (frame instanceof CloseWebSocketFrame) {
                hs.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
                if (socket != null) {
                    socket.emit("close");
                    socket = null;
                }
                return;
            }
            if (frame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
                return;
            }
            if (socket == null) return;
            if (frame instanceof TextWebSocketFrame) socket.emit("message", ((TextWebSocketFrame) frame).text());
            else if (frame instanceof BinaryWebSocketFrame) socket.emit("message", (Object) frame.content().array());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (socket != null) socket.emit("error", "unknown error", cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    private final static class EngineIoWebSocketImpl extends EngineIoWebSocket {
        private final Channel channel;
        private final Map<String, String> query;
        private final Map<String, List<String>> headers = new HashMap<>();

        public EngineIoWebSocketImpl(Channel channel, FullHttpRequest request) {
            this.channel = channel;
            query = ParseQS.decode(new QueryStringDecoder(request.uri()).rawQuery());
            HttpHeaders h = request.headers();
            h.names().forEach(it -> headers.put(it, h.getAll(it)));
        }

        @Override
        public Map<String, String> getQuery() {
            return query;
        }

        @Override
        public Map<String, List<String>> getConnectionHeaders() {
            return headers;
        }

        @Override
        public void write(String message) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }

        @Override
        public void write(byte[] message) {
            channel.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(message)));
        }

        @Override
        public void close() {
            channel.close();
        }
    }
}

