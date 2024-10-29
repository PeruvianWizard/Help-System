package HelpSystem;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.bouncycastle.util.Arrays;

import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

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
	
	// This variable will be used to encrypt passwords
	private EncryptionHelper encryptionHelper;
	
	public UserDatabaseHelper() throws Exception {
		encryptionHelper = new EncryptionHelper();
	}
	
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
	
	// Creates the tables for the users with their respective id, username, password, role, email, and names
	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "username VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role1 VARCHAR(25), "						// Admin column
				+ "role2 VARCHAR(25), "						// Instructional column
				+ "role3 VARCHAR(25), "						// Student column
				+ "email VARCHAR(255), "
				+ "firstName VARCHAR(255), "
				+ "middleName VARCHAR(255), "
				+ "lastName VARCHAR(255), "
				+ "lostPass BOOLEAN DEFAULT FALSE, "		// flag
				+ "preferredName VARCHAR(255))";
		statement.execute(userTable);
		
		// Creates the table to store one time passwords.
		String passwordTable = "CREATE TABLE IF NOT EXISTS codes ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "oneTimePassword VARCHAR(255), "
				+ "role1 VARCHAR(20), "
				+ "role2 VARCHAR(20), "
				+ "role3 VARCHAR(20))";
		
		statement.execute(passwordTable);
		
		String articles = "CREATE TABLE IF NOT EXISTS articles ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "uniqueIdentifier BIGINT DEFAULT 0, "
				+ "isPrivate BOOLEAN DEFAULT FALSE, "
				+ "level INT DEFAULT 0, "
				+ "\"title\" VARCHAR(25), "
				+ "\"description\" VARCHAR(25), "
				+ "\"group\" VARCHAR(25), "
				+ "\"body\" CLOB)";
		
		statement.execute(articles);
		
		System.out.println("Tables created successfully.");
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
	
	//returns a list of all users
	public List<String> getUsers() throws SQLException {
		List<String> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT username FROM users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                users.add(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
	 }
	
	// This function will register a new user into the database. 
	public void register(String username, String password, String role1, String role2, String role3) throws Exception {
		// Encrypt the password
		String encryptedPassword = Base64.getEncoder().encodeToString(
				encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(username.toCharArray()))
		);
		
		String insertUser = "INSERT INTO users (username, password, role1, role2, role3) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			
			pstmt.setString(1, username);
			pstmt.setString(2, encryptedPassword);
			pstmt.setString(3, role1);
			pstmt.setString(4, role2);
			pstmt.setString(5, role3);
			pstmt.executeUpdate();
		}
	}
	
	// This function adds an article to the articles table
	public void addArticle(HelpArticle article) throws SQLException {
		// grab variables
		String title = article.getTitle();
		String body = article.getBody();
		String description = article.getDescription();
		String group = article.group();
		long identifier = article.getIdentifier();
		int level = article.getLevel();
		boolean isPrivate = article.isPrivate();
		
		String insertArticle = "INSERT INTO articles (\"title\", \"body\", \"description\", \"group\", uniqueIdentifier, level, isPrivate) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertArticle)) {
			
			pstmt.setString(1, title);
			pstmt.setString(2, body);
			pstmt.setString(3, description);
			pstmt.setString(4, group);
			pstmt.setLong(5, identifier);
			pstmt.setInt(6, level);
			pstmt.setBoolean(7, isPrivate);
			pstmt.executeUpdate();
		}
	}
	
	// This function will add a code to the one time code database 
	public void registerCode(String code, String role1, String role2, String role3) throws SQLException {
		String insertCode = "INSERT INTO codes (oneTimePassword, role1, role2, role3) VALUES (?, ?, ?, ?)";
		try(PreparedStatement pstmt = connection.prepareStatement(insertCode)) {
			String fullCode = role1 + role2 + role3 + code; 
			pstmt.setString(1, fullCode);
			pstmt.setString(2, role1);
			pstmt.setString(3, role2);
			pstmt.setString(4, role3);
			pstmt.executeUpdate();
		}
	}
	
	// This function will update a users account with additional information
	public void updateUser(String username, String firstName, String middleName, String lastName, String preferredName, String email) throws SQLException {
		String query = "UPDATE users SET firstname = ?, middlename = ?, lastname = ?, preferredname = ?, email = ? WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, firstName);
			pstmt.setString(2, middleName);
			pstmt.setString(3, lastName);
			pstmt.setString(4, preferredName);
			pstmt.setString(5, email);
			pstmt.setString(6, username);
			
			pstmt.executeUpdate();
		}
	}
	
	// This function deletes a user from the database
	public void deleteUser(String username) throws SQLException {
		String sql = "DELETE FROM users WHERE username = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.setString(1, username);
			pstmt.executeUpdate();
		}
	}
	
	// This function checks if the user exists in the table
	public boolean UserExists(String username) {
		String query = "SELECT COUNT(*) FROM users WHERE username = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	// This function will log in users into the database
	public boolean login(String username, String password, String role1, String role2, String role3) throws Exception {
		// Encrypt password passed as parameter to compare it with the encrypted password in the database
		String encryptedPassword = Base64.getEncoder().encodeToString(
				encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(username.toCharArray()))
		);
		
		String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role1 = ? AND role2 = ? AND role3 = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, encryptedPassword);
			pstmt.setString(3, role1);
			pstmt.setString(4, role2);
			pstmt.setString(5, role3);
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					return rs.getInt(1) > 0;
				}
			} catch (SQLException se){
				se.printStackTrace();
			}
		}
		return false; 
	}
	
	// This function checks whether a one time code exists in the database, and returns the role
	public String checkForCode(String code) throws SQLException {
		String query = "SELECT role1, role2, role3 FROM codes WHERE oneTimePassword = ?";
		String fullCode = "";
		
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, code);
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					String role1 = rs.getString("role1");
					String role2 = rs.getString("role2");
					String role3 = rs.getString("role3");
					
					fullCode = "" + role1 + role2 + role3; 
					
				}
			}
		}
		return fullCode; 
	}
	
	// This function deletes a code
	public void deleteCode(String code) throws SQLException {
		String query = "DELETE FROM codes WHERE oneTimePassword = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, code);
			pstmt.executeUpdate();
		}
	}
	
	// this function checks if user account is fully updated
	public boolean isUpdated(String username) throws SQLException {
		String query = "SELECT * FROM users WHERE username = ? AND email IS NOT NULL";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				// return if user has email
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false; 
	}
	
	// this function returns the auth as a string
	public String checkAuth(String username) throws SQLException {
		String query = "SELECT role1, role2, role3 FROM users WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1,  username);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					String role1 =  rs.getString("role1");
					String role2 =  rs.getString("role2");
					String role3 =  rs.getString("role3");
					
					String roles = "" + role1 + role2 + role3;
					
					return roles;
					
				} else {
					return "-1"; 
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "-1";
	}
	
	// this function modifies a user's role (Note: if you find a better way to do this function, please change it)
	public boolean modifyRole(String username, String role) throws Exception {
		String query = "SELECT role1, role2, role3 FROM users WHERE username = ?";			// first query to get user's current roles
		boolean changed = false;
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {				
			pstmt.setString(1, username);
			
			try(ResultSet rs = pstmt.executeQuery()){					// execute first query
				String role1;
				String role2;
				String role3;
						
				if(rs.next()) {
					role1 =  rs.getString("role1");
					role2 =  rs.getString("role2");
					role3 =  rs.getString("role3");					
				} else {
					return false; 
				}
				
				
				String update = "UPDATE users SET role1 = ?, role2 = ?, role3 = ? WHERE username = ?";		// second query to update the user's roles
				
				try(PreparedStatement pstmt2 = connection.prepareStatement(update)){
					pstmt2.setString(4, username);
					if(!role1.contains("admin") && role.equals("admin")) {				// if current user is not admin 
						pstmt2.setString(1, role);								// update the target role and leave the other roles as is
						pstmt2.setString(2, role2);
						pstmt2.setString(3, role3);
						changed = true;
					} else if(!role2.contains("instructor") && role.equals("instructor")) {	// if current user is not instructor
						pstmt2.setString(1, role1);								// update the target role and leave the other roles as is
						pstmt2.setString(2, role);
						pstmt2.setString(3, role3);
						changed = true;
					} else if(!role3.contains("student") && role.equals("student")) {		// if current user is not student
						pstmt2.setString(1, role1);								// update the target role and leave the other roles as is
						pstmt2.setString(2, role2);
						pstmt2.setString(3, role);
						changed = true;
					}
					
					pstmt2.executeUpdate();
					return changed;
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("ModifyRole Function second query failed!");
				}
						
			}
							
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		
		return false;
	}
	
	// this function changes the password of an existing user with a reset code
	public void changePasswordWithCode(String username, String newPassword) throws Exception {
		// Encrypts the new password
		String newEncryptedPassword = Base64.getEncoder().encodeToString(
				encryptionHelper.encrypt(newPassword.getBytes(), EncryptionUtils.getInitializationVector(username.toCharArray()))
		);
		
		String query = "UPDATE users SET password = ?, lostPass = ? WHERE username = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, newEncryptedPassword);
			pstmt.setBoolean(2, true);
			pstmt.setString(3, username);
			
			pstmt.executeUpdate();
		}
	}
	
	// this function changes the password of an existing user
		public void changePassword(String username, String newPassword) throws Exception {
			// Encrypts the new password
			String newEncryptedPassword = Base64.getEncoder().encodeToString(
					encryptionHelper.encrypt(newPassword.getBytes(), EncryptionUtils.getInitializationVector(username.toCharArray()))
			);
			
			String query = "UPDATE users SET password = ?, lostPass = ? WHERE username = ?";
			try(PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, newEncryptedPassword);
				pstmt.setBoolean(2, false);
				pstmt.setString(3, username);
					
				pstmt.executeUpdate();
			}
		}
	
	// This function checks for a reset password if needed
	public boolean needsPasswordReset(String username) throws SQLException {
		String query = "SELECT * FROM users WHERE username = ? AND lostPass = TRUE";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					return true;
				} else {
					return false; 
				}
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
