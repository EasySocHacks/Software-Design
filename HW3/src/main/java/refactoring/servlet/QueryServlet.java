package refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.sql.ResultSet;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractServlet {
    public QueryServlet() {
        super();
    }

    public QueryServlet(String databaseUrl, String user, String password) {
        super(databaseUrl, user, password);
    }

    @Override
    void doGetMainLogic(HttpServletRequest request, PrintWriter printWriter) throws Exception {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            ResultSet rs = databaseUtils.getStatement().executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
            printWriter.println("<html><body>");
            printWriter.println("<h1>Product with max price: </h1>");

            while (rs.next()) {
                String  name = rs.getString("name");
                int price  = rs.getInt("price");
                printWriter.println(name + "\t" + price + "</br>");
            }
            printWriter.println("</body></html>");
        } else if ("min".equals(command)) {
            ResultSet rs = databaseUtils.getStatement().executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
            printWriter.println("<html><body>");
            printWriter.println("<h1>Product with min price: </h1>");

            while (rs.next()) {
                String  name = rs.getString("name");
                int price  = rs.getInt("price");
                printWriter.println(name + "\t" + price + "</br>");
            }
            printWriter.println("</body></html>");
        } else if ("sum".equals(command)) {
            ResultSet rs = databaseUtils.getStatement().executeQuery("SELECT SUM(price) FROM PRODUCT");
            printWriter.println("<html><body>");
            printWriter.println("Summary price: ");

            if (rs.next()) {
                printWriter.println(rs.getInt(1));
            }
            printWriter.println("</body></html>");
        } else if ("count".equals(command)) {
            ResultSet rs = databaseUtils.getStatement().executeQuery("SELECT COUNT(*) FROM PRODUCT");
            printWriter.println("<html><body>");
            printWriter.println("Number of products: ");

            if (rs.next()) {
                printWriter.println(rs.getInt(1));
            }
            printWriter.println("</body></html>");
        } else {
            printWriter.println("Unknown command: " + command);
        }
    }

}
