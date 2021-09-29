package refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    void doGetMainLogic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultSet resultSet = databaseUtils.getStatement().executeQuery("SELECT * FROM PRODUCT");

        response.getWriter().println("<html><body>");

        while (resultSet.next()) {
            String  name = resultSet.getString("name");
            int price  = resultSet.getInt("price");
            response.getWriter().println(name + "\t" + price + "</br>");
        }
        response.getWriter().println("</body></html>");
    }
}
