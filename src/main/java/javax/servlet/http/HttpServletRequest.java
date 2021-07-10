package javax.servlet.http;

import javax.servlet.AsyncContext;
import javax.servlet.ServletInputStream;
import java.util.Enumeration;

@SuppressWarnings("unused")
public interface HttpServletRequest {
    String getHeader(String name);
    Enumeration<String> getHeaders(String name);
    Enumeration<String> getHeaderNames();
    String getMethod();
    String getQueryString();
    Object getAttribute(String name);
    int getContentLength();
    ServletInputStream getInputStream();
    void setAttribute(String name, Object o);
    AsyncContext startAsync() throws IllegalStateException;
    boolean isAsyncStarted();
    boolean isAsyncSupported();
    AsyncContext getAsyncContext();
}
