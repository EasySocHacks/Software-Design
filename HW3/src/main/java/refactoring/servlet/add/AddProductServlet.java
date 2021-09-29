package refactoring.servlet.add;

import refactoring.servlet.AbstractServlet;

import javax.servlet.http.HttpServletRequest;

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
    public void doGetMainLogic(HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        databaseUtils.getStatement().executeUpdate(
                "INSERT INTO PRODUCT (NAME, PRICE) VALUES " +
                        "('" + name + "'," + price + ")");

        htmlResponseUtils.sendMessage("OK");
    }
}
