package refactoring.servlet.get;

import refactoring.servlet.AbstractServlet;
import refactoring.utils.html.HTMLPageBuilder;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractServlet {
    public GetProductsServlet() {
        super();
    }

    public GetProductsServlet(String databaseUrl, String user, String password) {
        super(databaseUrl, user, password);
    }

    @Override
    public void doGetMainLogic(HttpServletRequest request) throws Exception {
        ResultSet resultSet = databaseUtils.getStatement().executeQuery("SELECT * FROM PRODUCT");

        HTMLPageBuilder htmlPageBuilder = new HTMLPageBuilder();

        while (resultSet.next()) {
            String  name = resultSet.getString("name");
            int price  = resultSet.getInt("price");

            htmlPageBuilder = htmlPageBuilder.addLineWithBr("%s\t%d", name, price);
        }

        htmlResponseUtils.sendHTMLPage(htmlPageBuilder.build().getHtmlPage());
    }
}
