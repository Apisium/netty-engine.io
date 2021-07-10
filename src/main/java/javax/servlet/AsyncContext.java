package javax.servlet;

@SuppressWarnings("unused")
public interface AsyncContext {
    void complete();
    void addListener(AsyncListener listener);
    void setTimeout(long timeout);
}
