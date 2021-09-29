package refactoring.servlet;

import refactoring.utils.DatabaseUtils;
import refactoring.utils.html.HTMLResponseUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public abstract class AbstractServlet extends HttpServlet {
    protected DatabaseUtils databaseUtils;
    protected HTMLResponseUtils htmlResponseUtils;

    public AbstractServlet() {
        try {
            databaseUtils = new DatabaseUtils("jdbc:sqlite:test.db", "", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AbstractServlet(String databaseUrl, String user, String password) {
        try {
            databaseUtils = new DatabaseUtils(databaseUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        htmlResponseUtils = new HTMLResponseUtils(response);

        try {
            doGetMainLogic(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                databaseUtils.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public abstract void doGetMainLogic(HttpServletRequest request) throws Exception;
}
