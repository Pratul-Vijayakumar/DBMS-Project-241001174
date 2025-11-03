package railwayTicketing;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseOperations {
    
    private Connection connection;
    
    // Constructor
    public DatabaseOperations() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    // ============ USER OPERATIONS ============
    
    // Register new user (Signup)
    public boolean registerUser(String username, String password, String fullName, String contactNo) {
        try {
            String query = "INSERT INTO users (username, password, full_name, contact_no) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, contactNo);
            
            int result = stmt.executeUpdate();
            stmt.close();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    // Login user
    public User loginUser(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("contact_no")
                );
                stmt.close();
                return user;
            }
            stmt.close();
            return null;
            
        } catch (SQLException e) {
            System.out.println("Error logging in user: " + e.getMessage());
            return null;
        }
    }
    
    // Check if username already exists
    public boolean usernameExists(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            stmt.close();
            return exists;
            
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
            return false;
        }
    }
    
    // ============ TRAIN OPERATIONS ============
    
    // Get all unique sources
    public Set<String> getAllSources() {
        Set<String> sources = new HashSet<>();
        try {
            String query = "SELECT DISTINCT source FROM trains";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                sources.add(rs.getString("source"));
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error fetching sources: " + e.getMessage());
        }
        return sources;
    }
    
    // Get all unique destinations
    public Set<String> getAllDestinations() {
        Set<String> destinations = new HashSet<>();
        try {
            String query = "SELECT DISTINCT destination FROM trains";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                destinations.add(rs.getString("destination"));
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error fetching destinations: " + e.getMessage());
        }
        return destinations;
    }
    
    // Search trains by source and destination
    public List<Train> searchTrains(String source, String destination) {
        List<Train> trains = new ArrayList<>();
        try {
            String query = "SELECT * FROM trains WHERE source = ? AND destination = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, source);
            stmt.setString(2, destination);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Train train = new Train(
                    rs.getInt("train_id"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getTime("departure_time"),
                    rs.getTime("arrival_time"),
                    rs.getBigDecimal("fare")
                );
                trains.add(train);
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error searching trains: " + e.getMessage());
        }
        return trains;
    }
    
    // Get all trains
    public List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();
        try {
            String query = "SELECT * FROM trains";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Train train = new Train(
                    rs.getInt("train_id"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getTime("departure_time"),
                    rs.getTime("arrival_time"),
                    rs.getBigDecimal("fare")
                );
                trains.add(train);
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error fetching trains: " + e.getMessage());
        }
        return trains;
    }
    
    // Get train by ID
    public Train getTrainById(int trainId) {
        try {
            String query = "SELECT * FROM trains WHERE train_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, trainId);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Train train = new Train(
                    rs.getInt("train_id"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getTime("departure_time"),
                    rs.getTime("arrival_time"),
                    rs.getBigDecimal("fare")
                );
                stmt.close();
                return train;
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error fetching train: " + e.getMessage());
        }
        return null;
    }
    
    // ============ TICKET OPERATIONS ============
    
    // Book a ticket
    public int bookTicket(int passengerId, int trainId, Date dateOfJourney, String seat, 
                          String passengerName, int age, String gender, String contactNo) {
        try {
            String query = "INSERT INTO tickets (passenger_id, train_id, date_of_journey, booking_status, seat, passenger_name, age, gender, contact_no) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, passengerId);
            stmt.setInt(2, trainId);
            stmt.setDate(3, dateOfJourney);
            stmt.setString(4, "Confirmed");
            stmt.setString(5, seat);
            stmt.setString(6, passengerName);
            stmt.setInt(7, age);
            stmt.setString(8, gender);
            stmt.setString(9, contactNo);
            
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            
            if (keys.next()) {
                int ticketId = keys.getInt(1);
                stmt.close();
                return ticketId;
            }
            stmt.close();
            return -1;
            
        } catch (SQLException e) {
            System.out.println("Error booking ticket: " + e.getMessage());
            return -1;
        }
    }
    
    // Get all tickets for a user
    public List<Ticket> getTicketsByUserId(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        try {
            String query = "SELECT * FROM tickets WHERE passenger_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Ticket ticket = new Ticket(
                    rs.getInt("ticket_id"),
                    rs.getInt("passenger_id"),
                    rs.getInt("train_id"),
                    rs.getDate("date_of_journey"),
                    rs.getString("booking_status"),
                    rs.getString("seat"),
                    rs.getString("passenger_name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("contact_no")
                );
                tickets.add(ticket);
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error fetching tickets: " + e.getMessage());
        }
        return tickets;
    }
    
    // Cancel a ticket
    public boolean cancelTicket(int ticketId) {
        try {
            String query = "DELETE FROM tickets WHERE ticket_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, ticketId);
            
            int result = stmt.executeUpdate();
            stmt.close();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error canceling ticket: " + e.getMessage());
            return false;
        }
    }
    
    // Get ticket by ID
    public Ticket getTicketById(int ticketId) {
        try {
            String query = "SELECT * FROM tickets WHERE ticket_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, ticketId);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Ticket ticket = new Ticket(
                    rs.getInt("ticket_id"),
                    rs.getInt("passenger_id"),
                    rs.getInt("train_id"),
                    rs.getDate("date_of_journey"),
                    rs.getString("booking_status"),
                    rs.getString("seat"),
                    rs.getString("passenger_name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("contact_no")
                );
                stmt.close();
                return ticket;
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error fetching ticket: " + e.getMessage());
        }
        return null;
    }
}
