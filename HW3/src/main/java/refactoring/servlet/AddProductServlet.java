package refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractServlet {
    public AddProductServlet() {
        super();
    }

    public AddProductServlet(String databaseUrl, String user, String password) {
        super(databaseUrl, user, password);
    }

    @Override
    void doGetMainLogic(HttpServletRequest request, PrintWriter printWriter) throws Exception {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        databaseUtils.getStatement().executeUpdate(
                "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                        "('" + name + "'," + price + ")");

        printWriter.println("OK");
    }
}
