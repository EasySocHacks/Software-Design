package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLPageBuilder;
import refactoring.utils.html.HTMLResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

public class MaxQueryServlet extends AbstractQueryServlet {
    public MaxQueryServlet(DatabaseUtils databaseUtils, HTMLResponseUtils htmlResponseUtils) {
        super(databaseUtils, htmlResponseUtils);
    }

    @Override
    ResultSet proceedQuery() throws Exception {
        return getDatabaseUtils().getStatement().executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
    }

    @Override
    void printResult(HttpServletRequest request, ResultSet resultSet) throws Exception {
        HTMLPageBuilder htmlPageBuilder = new HTMLPageBuilder()
                .addLine("<h1>Product with max price: </h1>");

        while (resultSet.next()) {
            String  name = resultSet.getString("name");
            int price  = resultSet.getInt("price");

            htmlPageBuilder = htmlPageBuilder.addLine("%s\t%d</br>", name, price);
        }

        getHtmlResponseUtils().sendHTMLPage(htmlPageBuilder.build().getHtmlPage());
    }
}
