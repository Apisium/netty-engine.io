package cn.apisium.netty.engineio;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class HttpServletResponseImpl implements HttpServletResponse {
    private final FullHttpResponse originalResponse;
    private final ServletOutputStream outputStream;
    private final PrintWriter printWriter;
    public HttpServletResponseImpl(FullHttpResponse originalResponse, Channel channel) {
        this.originalResponse = originalResponse;
        outputStream = new ServletOutputStream(originalResponse, channel);
        printWriter = new PrintWriter(outputStream);
    }

    public void addHeader(String name, String value) {
        originalResponse.headers().add(name, value);
    }

    public void setStatus(int sc) {
        this.originalResponse.setStatus(HttpResponseStatus.valueOf(sc));
    }

    public void setStatus(int sc, String sm) {
        this.originalResponse.setStatus(new HttpResponseStatus(sc, sm));
    }

    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    public PrintWriter getWriter() {
        return printWriter;
    }

    public void setCharacterEncoding(String charset) {
        originalResponse.headers().set(HttpHeaderNames.CONTENT_ENCODING, charset);
    }

    public void setContentLength(int len) {
        HttpUtil.setContentLength(this.originalResponse, len);
    }

    public void setContentType(String type) {
        originalResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, type);
    }
}
