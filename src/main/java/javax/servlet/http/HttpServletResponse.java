package javax.servlet.http;

import javax.servlet.ServletOutputStream;
import java.io.PrintWriter;

@SuppressWarnings("unused")
public interface HttpServletResponse {
    void addHeader(String name, String value);
    void setStatus(int sc);
    void setStatus(int sc, String sm);
    ServletOutputStream getOutputStream();
    PrintWriter getWriter();
    void setCharacterEncoding(String charset);
    void setContentLength(int len);
    void setContentType(String type);
}
