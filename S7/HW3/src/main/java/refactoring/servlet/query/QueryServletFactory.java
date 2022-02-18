package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLResponseUtils;

public class QueryServletFactory {
    public AbstractQueryServlet getQueryServletByCommand(
            String command,
            DatabaseUtils databaseUtils,
            HTMLResponseUtils htmlResponseUtils) {
        switch (command) {
            case "max":
                return max(databaseUtils, htmlResponseUtils);
            case "min":
                return min(databaseUtils, htmlResponseUtils);
            case "sum":
                return sum(databaseUtils, htmlResponseUtils);
            case "count":
                return count(databaseUtils, htmlResponseUtils);
            default:
                return null;
        }
    }

    public AbstractQueryServlet max(DatabaseUtils databaseUtils, HTMLResponseUtils htmlResponseUtils) {
        return new MaxQueryServlet(databaseUtils, htmlResponseUtils);
    }

    public AbstractQueryServlet min(DatabaseUtils databaseUtils, HTMLResponseUtils htmlResponseUtils) {
        return new MinQueryServlet(databaseUtils, htmlResponseUtils);
    }

    public AbstractQueryServlet sum(DatabaseUtils databaseUtils, HTMLResponseUtils htmlResponseUtils) {
        return new SumQueryServlet(databaseUtils, htmlResponseUtils);
    }

    public AbstractQueryServlet count(DatabaseUtils databaseUtils, HTMLResponseUtils htmlResponseUtils) {
        return new CountQueryServlet(databaseUtils, htmlResponseUtils);
    }

}
