package refactoring.servlet.query;

import org.jetbrains.annotations.NotNull;
import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLResponseUtils;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractQueryServlet {
    private final @NotNull DatabaseUtils databaseUtils;
    private final @NotNull HTMLResponseUtils htmlResponseUtils;

    public AbstractQueryServlet(@NotNull DatabaseUtils databaseUtils, @NotNull HTMLResponseUtils htmlResponseUtils) {
        this.databaseUtils = databaseUtils;
        this.htmlResponseUtils = htmlResponseUtils;
    }

    abstract void doMainLogic(@NotNull HttpServletRequest request) throws Exception;

    public @NotNull DatabaseUtils getDatabaseUtils() {
        return databaseUtils;
    }

    public @NotNull HTMLResponseUtils getHtmlResponseUtils() {
        return htmlResponseUtils;
    }
}
