package railwayTicketing;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/railway_ticketing";
    private static final String USER = "root";
    private static final String PASSWORD = "MSDhoni@007";
    
    // Static connection object
    private static Connection connection = null;
    
    // Method to establish connection
    public static Connection getConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create connection
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            }
            return connection;
            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found!");
            System.out.println("Error: " + e.getMessage());
            return null;
            
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    // Method to close connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed!");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
