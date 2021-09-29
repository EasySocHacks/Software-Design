package refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
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
    void doGetMainLogic(HttpServletRequest request, PrintWriter printWriter) throws Exception {
        ResultSet resultSet = databaseUtils.getStatement().executeQuery("SELECT * FROM PRODUCT");

        printWriter.println("<html><body>");

        while (resultSet.next()) {
            String  name = resultSet.getString("name");
            int price  = resultSet.getInt("price");
            printWriter.println(name + "\t" + price + "</br>");
        }
        printWriter.println("</body></html>");
    }
}
