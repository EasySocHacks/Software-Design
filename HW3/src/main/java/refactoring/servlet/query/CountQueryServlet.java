package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLPageBuilder;
import refactoring.utils.html.HTMLResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

public class CountQueryServlet extends AbstractQueryServlet {
    public CountQueryServlet(DatabaseUtils databaseUtils, HTMLResponseUtils htmlResponseUtils) {
        super(databaseUtils, htmlResponseUtils);
    }

    @Override
    void doMainLogic(HttpServletRequest request) throws Exception {
        ResultSet resultSet =
                getDatabaseUtils().getStatement().executeQuery("SELECT COUNT(*) FROM PRODUCT");

        HTMLPageBuilder htmlPageBuilder = new HTMLPageBuilder()
                .addLine("Number of products: ");

        if (resultSet.next()) {
            htmlPageBuilder = htmlPageBuilder.addLine("%d", resultSet.getInt(1));
        }

        getHtmlResponseUtils().sendHTMLPage(htmlPageBuilder.build().getHtmlPage());
    }
}
