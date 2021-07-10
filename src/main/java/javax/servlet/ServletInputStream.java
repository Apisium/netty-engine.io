package javax.servlet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

public class ServletInputStream extends ByteBufInputStream {
    public ServletInputStream(HttpRequest request) {
        super(request instanceof FullHttpRequest
                ? ((FullHttpRequest) request).content() : Unpooled.buffer(0));
    }
}
