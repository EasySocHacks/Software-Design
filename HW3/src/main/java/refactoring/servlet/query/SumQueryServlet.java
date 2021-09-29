package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.sql.ResultSet;

public class SumQueryServlet extends AbstractQueryServlet {
    public SumQueryServlet(DatabaseUtils databaseUtils) {
        super(databaseUtils);
    }

    @Override
    ResultSet proceedQuery() throws Exception {
        return getDatabaseUtils().getStatement().executeQuery("SELECT SUM(price) FROM PRODUCT");
    }

    @Override
    void printResult(HttpServletRequest request, ResultSet resultSet, PrintWriter printWriter) throws Exception {
        printWriter.println("Summary price: ");

        if (resultSet.next()) {
            printWriter.println(resultSet.getInt(1));
        }
    }
}
