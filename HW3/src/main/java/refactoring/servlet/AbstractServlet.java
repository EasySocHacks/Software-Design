package refactoring.servlet;

import refactoring.utils.DatabaseUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public abstract class AbstractServlet extends HttpServlet {
    protected DatabaseUtils databaseUtils;

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
        try {
            doGetMainLogic(request, response.getWriter());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                databaseUtils.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    abstract void doGetMainLogic(HttpServletRequest request, PrintWriter printWriter) throws Exception;
}
