package easy.soc.hacks.hw4.utils

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

object DatabaseUtils {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:todo.db", "", "")
    val statement: Statement = connection.createStatement()

    @Throws(SQLException::class)
    fun close() {
        statement.close()
        connection.close()
    }
}