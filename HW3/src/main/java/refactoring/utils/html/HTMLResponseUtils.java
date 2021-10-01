package refactoring.utils.html;

import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HTMLResponseUtils {
    private final @NotNull HttpServletResponse httpServletResponse;
    private final @NotNull PrintWriter printWriter;

    public HTMLResponseUtils(@NotNull HttpServletResponse httpServletResponse) throws IOException {
        this.httpServletResponse = httpServletResponse;

        printWriter = httpServletResponse.getWriter();
    }

    public void sendMessage(@NotNull String message, Object... args) {
        printWriter.println(String.format(message, args));

        httpServletResponse.setContentType("text/html");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

    public void sendHTMLPage(@NotNull HTMLPage htmlPage) {
        printWriter.println(htmlPage.getLines());

        httpServletResponse.setContentType("text/html");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }
}
