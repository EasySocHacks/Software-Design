package refactoring.servlet.query;

import refactoring.servlet.AbstractServlet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractServlet {
    private final QueryServletFactory queryServletFactory = new QueryServletFactory();

    public QueryServlet() {
        super();
    }

    public QueryServlet(String databaseUrl, String user, String password) {
        super(databaseUrl, user, password);
    }

    @Override
    public void doGetMainLogic(HttpServletRequest request) throws Exception {
        String command = request.getParameter("command");

        switch (command) {
            case "max":
                queryServletFactory.max(databaseUtils, htmlResponseUtils).doMainLogic(request);
                break;
            case "min":
                queryServletFactory.min(databaseUtils, htmlResponseUtils).doMainLogic(request);
                break;
            case "sum":
                queryServletFactory.sum(databaseUtils, htmlResponseUtils).doMainLogic(request);
                break;
            case "count":
                queryServletFactory.count(databaseUtils, htmlResponseUtils).doMainLogic(request);
                break;
            default:
                htmlResponseUtils.sendMessage("Unknown command: %s", command);
                break;
        }
    }
}
