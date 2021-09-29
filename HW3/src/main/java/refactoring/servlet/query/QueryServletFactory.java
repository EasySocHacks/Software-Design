package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;

public class QueryServletFactory {
    public AbstractQueryServlet getQueryServletByCommand(String command, DatabaseUtils databaseUtils) {
        switch (command) {
            case "max": return max(databaseUtils);
            case "min": return min(databaseUtils);
            case "sum": return sum(databaseUtils);
            case "count": return count(databaseUtils);
            default: return null;
        }
    }

    public AbstractQueryServlet max(DatabaseUtils databaseUtils) {
        return new MaxQueryServlet(databaseUtils);
    }

    public AbstractQueryServlet min(DatabaseUtils databaseUtils) {
        return new MinQueryServlet(databaseUtils);
    }

    public AbstractQueryServlet sum(DatabaseUtils databaseUtils) {
        return new SumQueryServlet(databaseUtils);
    }

    public AbstractQueryServlet count(DatabaseUtils databaseUtils) {
        return new CountQueryServlet(databaseUtils);
    }

}
