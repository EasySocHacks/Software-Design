package refactoring.servlet.query;

import refactoring.servlet.AbstractServlet;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

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
    public void doGetMainLogic(HttpServletRequest request, PrintWriter printWriter) throws Exception {
        String command = request.getParameter("command");

        switch (command) {
            case "max":
                queryServletFactory.max(databaseUtils).doMainLogic(request, printWriter);
                break;
            case "min":
                queryServletFactory.min(databaseUtils).doMainLogic(request, printWriter);
                break;
            case "sum":
                queryServletFactory.sum(databaseUtils).doMainLogic(request, printWriter);
                break;
            case "count":
                queryServletFactory.count(databaseUtils).doMainLogic(request, printWriter);
                break;
            default:
                printWriter.println("Unknown command: " + command);
                break;
        }
    }
}
