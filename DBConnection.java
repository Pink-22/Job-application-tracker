// DBConnection.java
// Handles MySQL database connection using JDBC

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Change these values to match your MySQL setup
    private static final String URL      = "jdbc:mysql://localhost:3306/job_tracker";
    private static final String USER     = "root";
    private static final String PASSWORD = ""; // Enter your MySQL password here

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector.jar to classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
