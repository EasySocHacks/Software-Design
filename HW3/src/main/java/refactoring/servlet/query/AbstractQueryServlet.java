package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

public abstract class AbstractQueryServlet {
    private final DatabaseUtils databaseUtils;
    private final HTMLResponseUtils htmlResponseUtils;

    public AbstractQueryServlet(DatabaseUtils databaseUtils, HTMLResponseUtils htmlResponseUtils) {
        this.databaseUtils = databaseUtils;
        this.htmlResponseUtils = htmlResponseUtils;
    }

    abstract ResultSet proceedQuery() throws Exception;

    abstract void printResult(HttpServletRequest request, ResultSet resultSet) throws Exception;

    public void doMainLogic(HttpServletRequest request) throws Exception {
        ResultSet resultSet = proceedQuery();

        printResult(request, resultSet);
    }

    public DatabaseUtils getDatabaseUtils() {
        return databaseUtils;
    }

    public HTMLResponseUtils getHtmlResponseUtils() {
        return htmlResponseUtils;
    }
}
