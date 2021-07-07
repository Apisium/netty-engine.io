package cn.apisium.netty.engineio;

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
        if (HttpUtil.is100ContinueExpected(msg)) ctx.channel().writeAndFlush(new DefaultHttpResponse(HTTP_1_1, CONTINUE));
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        HttpServletResponseImpl resp = new HttpServletResponseImpl(response);
        server.handleRequest(new HttpServletRequestImpl(msg), resp);
        System.out.println(msg.uri());
        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        if (keepAlive) response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ctx.channel().writeAndFlush(response);
    }
}

