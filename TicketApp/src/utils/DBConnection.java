// JDBC connection helper placeholder
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Change username/password if your SQL Server is different
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ticket_resell;encrypt=false";
    private static final String USER = "sa";           // SQL Server username
    private static final String PASSWORD = "YourPasswordHere"; // Your SQL Server password

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
