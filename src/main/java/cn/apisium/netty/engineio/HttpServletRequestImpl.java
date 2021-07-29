package cn.apisium.netty.engineio;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.concurrent.ScheduledFuture;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HttpServletRequestImpl implements HttpServletRequest {
    private final HttpRequest originalRequest;
    private final ChannelHandlerContext ctx;
    private HashMap<String, Object> attributes;
    private ServletInputStream inputStream;
    private String queryString;
    private AsyncContext asyncContext;

    public HttpServletRequestImpl(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        this.originalRequest = originalRequest;
        this.ctx = ctx;
    }

    public String getHeader(String name) {
        return this.originalRequest.headers().get(name);
    }

    public Enumeration<String> getHeaders(String name) {
        return Collections.enumeration(this.originalRequest.headers().getAll(name));
    }

    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(originalRequest.headers().names());
    }

    public String getMethod() {
        return originalRequest.method().name();
    }

    public String getQueryString() {
        return queryString == null ? (queryString = new QueryStringDecoder(originalRequest.uri()).rawQuery()) : queryString;
    }

    public Object getAttribute(String name) {
        return attributes == null ? null : attributes.get(name);
    }

    public int getContentLength() {
        return HttpUtil.getContentLength(this.originalRequest, -1);
    }

    public ServletInputStream getInputStream() {
        return inputStream == null ? (inputStream = new ServletInputStream(originalRequest)) : inputStream;
    }

    public void setAttribute(String name, Object o) {
        if (this.attributes == null) this.attributes = new HashMap<>();
        this.attributes.put(name, o);
    }

    public AsyncContext startAsync() throws IllegalStateException {
        return asyncContext == null ? (asyncContext = new AsyncContextImpl()) : asyncContext;
    }

    public boolean isAsyncStarted() { return asyncContext != null; }

    public boolean isAsyncSupported() { return true; }

    public AsyncContext getAsyncContext() { return asyncContext; }

    private final class AsyncContextImpl implements Runnable, AsyncContext {
        private ScheduledFuture<?> timeoutFuture;
        private AsyncListener listener;

        public void complete() {
            if (timeoutFuture != null) {
                timeoutFuture.cancel(false);
                timeoutFuture = null;
            }
            if (listener != null) listener = null;
        }

        public void addListener(AsyncListener listener) { this.listener = listener; }

        public void setTimeout(long timeout) {
            timeoutFuture = ctx.executor().schedule(this, timeout, TimeUnit.MILLISECONDS);
        }

        @Override
        public void run() {
            timeoutFuture = null;
            if (listener != null) {
                try { listener.onTimeout(null); } catch (Exception ignored) { }
                listener = null;
            }
        }
    }
}
