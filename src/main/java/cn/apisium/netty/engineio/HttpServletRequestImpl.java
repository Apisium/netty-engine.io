package cn.apisium.netty.engineio;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.concurrent.ScheduledFuture;
import org.jetbrains.annotations.NotNull;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HttpServletRequestImpl implements HttpServletRequest {
    private final HttpRequest originalRequest;
    private final ChannelHandlerContext ctx;
    private HashMap<String, Object> attributes;
    private ServletInputStream inputStream;
    private String queryString;
    private AsyncContextImpl asyncContext;

    public HttpServletRequestImpl(@NotNull HttpRequest originalRequest, ChannelHandlerContext ctx) {
        this.originalRequest = originalRequest;
        this.ctx = ctx;
    }

    @Override
    public String getAuthType() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Cookie[] getCookies() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public long getDateHeader(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getHeader(String name) {
        return this.originalRequest.headers().get(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return Collections.enumeration(this.originalRequest.headers().getAll(name));
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(originalRequest.headers().names());
    }

    @Override
    public int getIntHeader(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getMethod() {
        return originalRequest.method().name();
    }

    @Override
    public String getPathInfo() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getPathTranslated() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getContextPath() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getQueryString() {
        return queryString == null ? (queryString = new QueryStringDecoder(originalRequest.uri()).rawQuery()) : queryString;
    }

    @Override
    public String getRemoteUser() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean isUserInRole(String role) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getRequestedSessionId() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getRequestURI() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getServletPath() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public HttpSession getSession(boolean create) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public HttpSession getSession() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String changeSessionId() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean authenticate(HttpServletResponse response) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void login(String username, String password) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void logout() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Collection<Part> getParts() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Part getPart(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Object getAttribute(String name) {
        return attributes == null ? null : attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getCharacterEncoding() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void setCharacterEncoding(String env) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public int getContentLength() {
        return HttpUtil.getContentLength(this.originalRequest, -1);
    }

    @Override
    public long getContentLengthLong() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getContentType() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public ServletInputStream getInputStream() {
        return inputStream == null ? (inputStream = new ServletInputStreamImpl(originalRequest)) : inputStream;
    }

    @Override
    public String getParameter(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Enumeration<String> getParameterNames() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String[] getParameterValues(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getProtocol() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getScheme() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getServerName() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public int getServerPort() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public BufferedReader getReader() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getRemoteAddr() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getRemoteHost() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public void setAttribute(String name, Object o) {
        if (this.attributes == null) this.attributes = new HashMap<>();
        this.attributes.put(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Locale getLocale() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public Enumeration<Locale> getLocales() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean isSecure() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getRealPath(String path) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public int getRemotePort() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getLocalName() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public String getLocalAddr() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public int getLocalPort() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public ServletContext getServletContext() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return asyncContext == null ? (asyncContext = new AsyncContextImpl()) : asyncContext;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    @Override
    public boolean isAsyncStarted() { return asyncContext != null; }

    @Override
    public boolean isAsyncSupported() { return true; }

    @Override
    public AsyncContext getAsyncContext() { return asyncContext; }

    @Override
    public DispatcherType getDispatcherType() {
        throw new IllegalStateException("This method needn't to be implemented!");
    }

    private final class AsyncContextImpl implements AsyncContext, Runnable {
        private ScheduledFuture<?> timeoutFuture;
        private AsyncListener listener;

        @Override
        public ServletRequest getRequest() { throw new IllegalStateException("This method needn't to be implemented!");  }

        @Override
        public ServletResponse getResponse() { throw new IllegalStateException("This method needn't to be implemented!"); }

        @Override
        public boolean hasOriginalRequestAndResponse() { throw new IllegalStateException("This method needn't to be implemented!"); }

        @Override
        public void dispatch() { throw new IllegalStateException("This method needn't to be implemented!"); }

        @Override
        public void dispatch(String path) { throw new IllegalStateException("This method needn't to be implemented!"); }

        @Override
        public void dispatch(ServletContext context, String path) { throw new IllegalStateException("This method needn't to be implemented!"); }

        @Override
        public void complete() {
            if (timeoutFuture != null) {
                timeoutFuture.cancel(false);
                timeoutFuture = null;
            }
            if (listener != null) listener = null;
        }

        @Override
        public void start(Runnable run) { throw new IllegalStateException("This method needn't to be implemented!"); }

        @Override
        public void addListener(AsyncListener listener) { this.listener = listener; }

        @Override
        public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse) {
            throw new IllegalStateException("This method needn't to be implemented!");
        }

        @Override
        public <T extends AsyncListener> T createListener(Class<T> clazz) {
            throw new IllegalStateException("This method needn't to be implemented!");
        }

        @Override
        public void setTimeout(long timeout) {
            timeoutFuture = ctx.executor().schedule(this, timeout, TimeUnit.MILLISECONDS);
        }

        @Override
        public long getTimeout() { throw new IllegalStateException("This method needn't to be implemented!"); }

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
