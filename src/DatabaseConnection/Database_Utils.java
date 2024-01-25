package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Utils {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sudoku_project";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "admin";
    
    // Database bağlantısı kurulur
    public static Connection dbConnector() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
        return connection;
    }
    
    // Kurulan database bağlantısı kesilir
    public static void dbDisconnector(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while closing the database connection.");
            e.printStackTrace();
        }
    }
    

    /*
    public static void main(String[] args) {
        // Test dbConnector()
        Connection connection = dbConnector();

        // Test dbDisconnector()
        dbDisconnector(connection);
    }
    */
}