package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLResponseUtils;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractQueryServlet {
    private final DatabaseUtils databaseUtils;
    private final HTMLResponseUtils htmlResponseUtils;

    public AbstractQueryServlet(DatabaseUtils databaseUtils, HTMLResponseUtils htmlResponseUtils) {
        this.databaseUtils = databaseUtils;
        this.htmlResponseUtils = htmlResponseUtils;
    }

    abstract void doMainLogic(HttpServletRequest request) throws Exception;

    public DatabaseUtils getDatabaseUtils() {
        return databaseUtils;
    }

    public HTMLResponseUtils getHtmlResponseUtils() {
        return htmlResponseUtils;
    }
}
