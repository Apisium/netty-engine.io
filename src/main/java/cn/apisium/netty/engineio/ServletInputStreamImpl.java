package cn.apisium.netty.engineio;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;

public class ServletInputStreamImpl extends ServletInputStream {

    public HttpRequest request;

    private final ByteBufInputStream in;

    public ServletInputStreamImpl(HttpRequest request) {
        this.request = request;

        this.in = new ByteBufInputStream(request instanceof FullHttpRequest
                ? ((FullHttpRequest) request).content() : Unpooled.buffer(0));
    }
    @Override
    public boolean isFinished() {
        return !isReady();
    }

    @Override
    public boolean isReady() {
        try {
            return in.available() != 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public int read() throws IOException {
        return this.in.read();
    }

    @Override
    public int read(byte[] buf) throws IOException {
        return this.in.read(buf);
    }

    @Override
    public int read(byte[] buf, int offset, int len) throws IOException {
        return this.in.read(buf, offset, len);
    }
}
