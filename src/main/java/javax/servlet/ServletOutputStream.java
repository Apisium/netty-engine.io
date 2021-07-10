package javax.servlet;

import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.IOException;

public class ServletOutputStream extends ByteBufOutputStream {
    private final FullHttpResponse response;
    private final Channel channel;
    public boolean closed;

    public ServletOutputStream(FullHttpResponse response, Channel channel) {
        super(response.content());
        this.response = response;
        this.channel = channel;
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        closed = true;
        super.close();
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        response.release();
    }
}
