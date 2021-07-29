package cn.apisium.netty.engineio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoWebSocket;
import io.socket.parseqs.ParseQS;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class EngineIoHandler extends SimpleChannelInboundHandler<Object> {
    private final String url;
    private final EngineIoServer server;
    private final WebSocketServerHandshakerFactory handshakerFactory;
    private WebSocketServerHandshaker hs;
    private EngineIoWebSocketImpl socket;

    public EngineIoHandler(EngineIoServer server) {
        this(server, "/socket.io");
    }

    public EngineIoHandler(EngineIoServer server, String url) {
        this(server, url, "ws://127.0.0.1/");
    }

    public EngineIoHandler(EngineIoServer server, String url, String webSocketURL) {
        this(server, url, webSocketURL, 65536);
    }

    public EngineIoHandler(EngineIoServer server, String url, String webSocketURL, int maxLength) {
        this.url = url;
        this.server = server;
        handshakerFactory = new WebSocketServerHandshakerFactory(webSocketURL, null, true, maxLength);
    }

    @SuppressWarnings("RedundantCast")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof FullHttpRequest) {
            FullHttpRequest msg = (FullHttpRequest) obj;
            if (url != null && !msg.uri().startsWith(url)) {
                ctx.fireChannelRead(msg.retain());
                return;
            }
            if (!msg.decoderResult().isSuccess()) {
                ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
                return;
            }
            if ("websocket".equals(msg.headers().get("Upgrade"))) {
                hs = handshakerFactory.newHandshaker(msg);
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
            HttpServletRequestImpl request = new HttpServletRequestImpl(msg, ctx);
            HttpServletResponseImpl resp = new HttpServletResponseImpl(response.retain(), ctx.channel());
            server.handleRequest(request, resp);
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            if (!request.isAsyncStarted() && !resp.getOutputStream().closed) resp.getOutputStream().close();
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
            else if (frame instanceof BinaryWebSocketFrame) {
                ByteBuf buf = frame.content();
                byte[] arr = new byte[buf.readableBytes()];
                buf.readBytes(arr);
                socket.emit("message", (Object) arr);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (socket != null) socket.emit("error", "unknown error", cause.getMessage());
        if (ctx.channel().isActive()) ctx.close();
    }

    private final static class EngineIoWebSocketImpl extends EngineIoWebSocket {
        private final Channel channel;
        private final Map<String, String> query;
        private final Map<String, List<String>> headers = new HashMap<>();

        public EngineIoWebSocketImpl(Channel channel, FullHttpRequest request) {
            this.channel = channel;
            Map<String, String> q;
            try {
                q = ParseQS.decode(new URL(request.uri()).getQuery());
            } catch (MalformedURLException e) {
                q = Collections.emptyMap();
            }
            query = q;
            HttpHeaders h = request.headers();
            h.names().forEach(it -> headers.put(it, h.getAll(it)));
        }

        @Override
        public Map<String, String> getQuery() { return query; }

        @Override
        public Map<String, List<String>> getConnectionHeaders() { return headers; }

        @Override
        public void write(String message) { channel.writeAndFlush(new TextWebSocketFrame(message)); }

        @Override
        public void write(byte[] message) {
            channel.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(message)));
        }

        @Override
        public void close() { channel.close(); }
    }
}

