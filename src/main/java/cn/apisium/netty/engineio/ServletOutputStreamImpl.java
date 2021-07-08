package cn.apisium.netty.engineio;

import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class ServletOutputStreamImpl extends ServletOutputStream {
    private final ByteBufOutputStream out;
    private final FullHttpResponse response;
    private final Channel channel;
    protected boolean closed;

    public ServletOutputStreamImpl(FullHttpResponse response, Channel channel) {
        this.response = response;
        this.channel = channel;
        out = new ByteBufOutputStream(response.content());
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b, int offset, int len) throws IOException {
        out.write(b, offset, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new IllegalStateException("This method needn't to be implemented!");
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
