package refactoring.servlet;

import refactoring.utils.DatabaseUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public abstract class AbstractServlet extends HttpServlet {
    private String databaseUrl = "jdbc:sqlite:test.db";
    private String user = "";
    private String password = "";

    protected DatabaseUtils databaseUtils;

    {
        try {
            databaseUtils = new DatabaseUtils(databaseUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AbstractServlet() { }

    public AbstractServlet(String databaseUrl, String user, String password) {
        this.databaseUrl = databaseUrl;
        this.user = user;
        this.password = password;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            doGetMainLogic(request, response);
        } catch (Exception e) {
            try {
                databaseUtils.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }

    abstract void doGetMainLogic(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
