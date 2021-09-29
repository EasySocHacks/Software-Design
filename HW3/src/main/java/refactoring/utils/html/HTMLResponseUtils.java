package refactoring.utils.html;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HTMLResponseUtils {
    private final HttpServletResponse httpServletResponse;
    private final PrintWriter printWriter;

    public HTMLResponseUtils(HttpServletResponse httpServletResponse) throws IOException {
        this.httpServletResponse = httpServletResponse;

        printWriter = httpServletResponse.getWriter();
    }

    public void sendMessage(String message, Object... args) {
        printWriter.println(String.format(message, args));

        httpServletResponse.setContentType("text/html");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

    public void sendHTMLPage(HTMLPage htmlPage) {
        printWriter.println(htmlPage.getLines());

        httpServletResponse.setContentType("text/html");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }
}
