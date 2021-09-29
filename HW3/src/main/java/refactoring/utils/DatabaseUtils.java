package refactoring.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {
    private final Connection connection;
    private final Statement statement;

    public DatabaseUtils(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    public void close() throws SQLException {
        statement.close();
        connection.close();
    }

    public Statement getStatement() {
        return statement;
    }
}
