package refactoring.servlet.query;

import org.jetbrains.annotations.NotNull;
import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLPageBuilder;
import refactoring.utils.html.HTMLResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

public class MinQueryServlet extends AbstractQueryServlet {
    public MinQueryServlet(@NotNull DatabaseUtils databaseUtils, @NotNull HTMLResponseUtils htmlResponseUtils) {
        super(databaseUtils, htmlResponseUtils);
    }

    @Override
    void doMainLogic(@NotNull HttpServletRequest request) throws Exception {
        ResultSet resultSet =
                getDatabaseUtils().getStatement().executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

        HTMLPageBuilder htmlPageBuilder = new HTMLPageBuilder()
                .addBlock("h1", "Product with min price: ");

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int price = resultSet.getInt("price");

            htmlPageBuilder = htmlPageBuilder.addLineWithBr("%s\t%d", name, price);
        }

        getHtmlResponseUtils().sendHTMLPage(htmlPageBuilder.build().getHtmlPage());
    }
}
