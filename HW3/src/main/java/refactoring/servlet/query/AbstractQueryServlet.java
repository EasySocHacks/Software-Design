package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.sql.ResultSet;

public abstract class AbstractQueryServlet {
    private final DatabaseUtils databaseUtils;

    public AbstractQueryServlet(DatabaseUtils databaseUtils) {
        this.databaseUtils = databaseUtils;
    }

    abstract ResultSet proceedQuery() throws Exception;

    abstract void printResult(HttpServletRequest request, ResultSet resultSet, PrintWriter printWriter) throws Exception;

    public void doMainLogic(HttpServletRequest request, PrintWriter printWriter) throws Exception {
        ResultSet resultSet = proceedQuery();

        printWriter.println("<html><body>");

        printResult(request, resultSet, printWriter);

        printWriter.println("</body></html>");
    }

    public DatabaseUtils getDatabaseUtils() {
        return databaseUtils;
    }
}
