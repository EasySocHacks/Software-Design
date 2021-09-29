package refactoring.servlet.query;

import refactoring.utils.DatabaseUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.sql.ResultSet;

public class MaxQueryServlet extends AbstractQueryServlet {
    public MaxQueryServlet(DatabaseUtils databaseUtils) {
        super(databaseUtils);
    }

    @Override
    ResultSet proceedQuery() throws Exception {
        return getDatabaseUtils().getStatement().executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
    }

    @Override
    void printResult(HttpServletRequest request, ResultSet resultSet, PrintWriter printWriter) throws Exception {
        printWriter.println("<h1>Product with max price: </h1>");

        while (resultSet.next()) {
            String  name = resultSet.getString("name");
            int price  = resultSet.getInt("price");
            printWriter.println(name + "\t" + price + "</br>");
        }
    }
}
