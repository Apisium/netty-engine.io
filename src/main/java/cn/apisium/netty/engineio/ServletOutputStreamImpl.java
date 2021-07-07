package cn.apisium.netty.engineio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class ServletOutputStreamImpl extends ServletOutputStream implements AutoCloseable {
    private final ByteBufOutputStream out;

    public ServletOutputStreamImpl(ByteBuf response) {
        out = new ByteBufOutputStream(response);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte @NotNull [] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte @NotNull [] b, int offset, int len) throws IOException {
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
}
