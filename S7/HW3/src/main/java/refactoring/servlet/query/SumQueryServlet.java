package refactoring.servlet.query;

import org.jetbrains.annotations.NotNull;
import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLPageBuilder;
import refactoring.utils.html.HTMLResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

public class SumQueryServlet extends AbstractQueryServlet {
    public SumQueryServlet(@NotNull DatabaseUtils databaseUtils, @NotNull HTMLResponseUtils htmlResponseUtils) {
        super(databaseUtils, htmlResponseUtils);
    }

    @Override
    void doMainLogic(@NotNull HttpServletRequest request) throws Exception {
        ResultSet resultSet =
                getDatabaseUtils().getStatement().executeQuery("SELECT SUM(price) FROM PRODUCT");

        HTMLPageBuilder htmlPageBuilder = new HTMLPageBuilder()
                .addLine("Summary price: ");

        if (resultSet.next()) {
            htmlPageBuilder = htmlPageBuilder.addLine("%d", resultSet.getInt(1));
        }

        getHtmlResponseUtils().sendHTMLPage(htmlPageBuilder.build().getHtmlPage());
    }
}
