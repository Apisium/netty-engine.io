package cn.apisium.netty.engineio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.socket.engineio.server.EngineIoServer;
import org.jetbrains.annotations.NotNull;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@ChannelHandler.Sharable
public class EngineIOHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private final EngineIoServer server;
    public EngineIOHandler(@NotNull EngineIoServer server) {
        this.server = server;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
        if (!msg.uri().startsWith("/socket.io")) {
            ctx.fireChannelRead(msg);
            return;
        }
        if (HttpUtil.is100ContinueExpected(msg)) {
            ctx.channel().writeAndFlush(new DefaultHttpResponse(HTTP_1_1, CONTINUE)).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        DefaultHttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        ByteBuf buf = Unpooled.buffer(0);
        HttpServletResponseImpl resp = new HttpServletResponseImpl(response, buf);
        server.handleRequest(new HttpServletRequestImpl(msg), resp);
        System.out.println(msg.uri());
        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        if (keepAlive) response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ctx.channel().write(response);
        ChannelFuture future = ctx.channel().writeAndFlush(buf);
        if (!keepAlive) future.addListener(ChannelFutureListener.CLOSE);
    }
}

