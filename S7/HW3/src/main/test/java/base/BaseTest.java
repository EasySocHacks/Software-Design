package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseTest {
    private Connection connection;
    private Statement statement;

    public BaseTest() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=1",
                    "sa",
                    "sa");

            statement = connection.createStatement();

            statement.execute("drop table if exists product");
            statement.execute(
                    "create table product " +
                            "(name text not null, " +
                            "price integer not null)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return statement;
    }

    public void close() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
