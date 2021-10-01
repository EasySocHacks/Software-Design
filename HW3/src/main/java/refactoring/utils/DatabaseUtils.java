package refactoring.utils;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {
    private final @NotNull Connection connection;
    private final @NotNull Statement statement;

    public DatabaseUtils(@NotNull String url, @NotNull String user, @NotNull String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    public void close() throws SQLException {
        statement.close();
        connection.close();
    }

    public @NotNull Statement getStatement() {
        return statement;
    }
}
