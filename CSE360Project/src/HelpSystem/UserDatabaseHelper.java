package HelpSystem;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** This function will create an in-memory database with H2.
 *  I used some variables and functions from the simpleDatabase class Activities Module04
 *  */
class UserDatabaseHelper {
	
	//JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/userDatabase"; 
	
	//Database credentials
	private final String USER = "CSE360project";
	private final String PASS = "";
	
	private Connection connection = null;
	private Statement statement = null;
	
	// Establishes initial connection to the database
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}
	
	// Creates the tables for the users with their respective id, username, password, and role
	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "username VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(20))";
		statement.execute(userTable);
	}
	
	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	// This function will register a new user into the database
	public void register(String username, char[] password, String role) throws SQLException {
		String insertUser = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			
			String passtemp = password.toString();
			
			pstmt.setString(1, username);
			pstmt.setString(2, passtemp);
			pstmt.setString(3, role);
			pstmt.executeUpdate();
		}
	}
	
	// This function will log in users into the database
	public boolean login(String username, String password, String role) throws SQLException {
		String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, role);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Closes the connection to the database
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
	
	//Have to implement rest of the functions after testing functions above
}
