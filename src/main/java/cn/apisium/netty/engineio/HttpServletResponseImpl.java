package cn.apisium.netty.engineio;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

public class HttpServletResponseImpl implements HttpServletResponse {
    private final FullHttpResponse originalResponse;
    private final ServletOutputStream outputStream;
    private final PrintWriter printWriter;
    public HttpServletResponseImpl(FullHttpResponse originalResponse) {
        this.originalResponse = originalResponse;
        outputStream = new ServletOutputStreamImpl(originalResponse.content());
        printWriter = new PrintWriter(outputStream);
    }
    @Override
    public void addCookie(Cookie cookie) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean containsHeader(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String encodeURL(String url) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String encodeRedirectURL(String url) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String encodeUrl(String url) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String encodeRedirectUrl(String url) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void sendError(int sc, String msg) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void sendError(int sc) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void sendRedirect(String location) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void setDateHeader(String name, long date) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void addDateHeader(String name, long date) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void setHeader(String name, String value) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void addHeader(String name, String value) {
        originalResponse.headers().add(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void addIntHeader(String name, int value) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void setStatus(int sc) {
        this.originalResponse.setStatus(HttpResponseStatus.valueOf(sc));
    }

    @Override
    public void setStatus(int sc, String sm) {
        this.originalResponse.setStatus(new HttpResponseStatus(sc, sm));
    }

    @Override
    public int getStatus() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getHeader(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Collection<String> getHeaders(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Collection<String> getHeaderNames() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getCharacterEncoding() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getContentType() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return printWriter;
    }

    @Override
    public void setCharacterEncoding(String charset) {
        originalResponse.headers().set(HttpHeaderNames.CONTENT_ENCODING, charset);
    }

    @Override
    public void setContentLength(int len) {
        HttpUtil.setContentLength(this.originalResponse, len);
    }

    @Override
    public void setContentLengthLong(long len) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void setContentType(String type) {
        originalResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, type);
    }

    @Override
    public void setBufferSize(int size) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public int getBufferSize() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void flushBuffer() {
        printWriter.flush();
    }

    @Override
    public void resetBuffer() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean isCommitted() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void reset() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void setLocale(Locale loc) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Locale getLocale() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }
}
