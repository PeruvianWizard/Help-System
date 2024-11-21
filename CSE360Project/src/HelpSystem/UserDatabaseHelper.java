package HelpSystem;
import java.awt.image.TileObserver;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bouncycastle.jcajce.provider.asymmetric.RSA;
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
	
	//allows access to connection for test cases
	public Connection getConnection() {
	    return connection;
	}
	
	// Creates the tables for the users with their respective id, username, password, role, email, and names
	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userId INT DEFAULT 0, "
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
				+ "authors VARCHAR(25), "
				+ "\"description\" VARCHAR(25), "
				+ "\"group\" VARCHAR(25), "
				+ "\"body\" CLOB)";
		
		statement.execute(articles);
		
		String message = "CREATE TABLE IF NOT EXISTS messages ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userId INT DEFAULT 0, "
				+ "username VARCHAR(255), "
				+ "messageBody CLOB)";
		statement.execute(message);
		
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
	
	//returns users with access to specific table
	public List<String> getGroupUsers(String groupName) throws SQLException {
		List<String> users = new ArrayList<>();
		try(Statement statement = connection.createStatement()) {
			String query = "SELECT userId FROM " + groupName + "access";
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()) {
				users.add(getUsername(rs.getInt("userId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	// returns username given Id
	public String getUsername(int userId) throws SQLException {
		String username = "";
		String query = "SELECT username FROM users WHERE userId = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					username = rs.getString("username");
				}
			}
		}
		
		return username;
	}
	
	// this function returns userId given username
	public int getUserId(String username) throws SQLException {
		String query = "SELECT userId FROM users WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					return rs.getInt("userId");
				}
			} catch (SQLException se){
				se.printStackTrace();
			}
		}
		return -1; 
	}
	
	// This function will register a new user into the database. 
	public void register(String username, String password, String role1, String role2, String role3) throws Exception {
		// Encrypt the password
		String encryptedPassword = Base64.getEncoder().encodeToString(
				encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(username.toCharArray()))
		);
		
		String insertUser = "INSERT INTO users (userId, username, password, role1, role2, role3) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			int randomNum = ThreadLocalRandom.current().nextInt(10000, 100000);
			
			pstmt.setInt(1, randomNum);
			pstmt.setString(2, username);
			pstmt.setString(3, encryptedPassword);
			pstmt.setString(4, role1);
			pstmt.setString(5, role2);
			pstmt.setString(6, role3);
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
	
	// This function checks group auth
	public boolean checkGroupAuth(String groupName, int userId) throws SQLException {
		String query = "SELECT userId FROM " + groupName + "access WHERE userId = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					return true;
				}
			}
		}
		
		return false; 
	}
	
	//checks if user exists
	public boolean userExists(int userId) throws SQLException {
		String query = "SELECT 1 FROM users WHERE userId = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					return true;
				}
			}
		}
		
		return false; 
	}
	
	// loads message sent by the student into the messages table
	public void sendMessage(String messageBody, String username,int userId) throws SQLException {
		String query = "INSERT INTO messages (userId, username, messageBody) VALUES (?,?,?)";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			pstmt.setString(2, username);
			pstmt.setString(3, messageBody);
			
			pstmt.executeUpdate();
		}
	}
	
	// returns a list with all the help messages, with the format, "userId	username	messageBody"  
	public List<String> getMessages() throws SQLException{
		List<String> messagesList = new ArrayList<>();
		try (Statement statement = connection.createStatement()) {
			String query = "SELECT username, userId,messageBody FROM messages";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
            	StringBuilder result = new StringBuilder();
            	
            	String username = resultSet.getString("username");
            	int userId = resultSet.getInt("userId");
            	String messageBody = resultSet.getString("messageBody");
            	
            	result.append(String.format("%-" + 15 + "s %-" + 15 + "s %s%n", userId, username, messageBody));
            	String formattedString = result.toString();
            	
            	messagesList.add(formattedString);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messagesList;
	}
	
	// returns a list with all ids of the help messages
	public List<Integer> getMessagesIds() {
		List<Integer> idList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT id FROM messages";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	idList.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idList;
	}
	
	// returns the username that sent the help message
	public String getSingleUsernameMessage(int id) throws SQLException {
		String username = "";
		String query = "SELECT username FROM messages WHERE id = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, id);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					username = rs.getString("username");
				}
			}
		}
		return username;
	}
	
	// returns a help message
	public String getSingleMessage(int id) throws SQLException {
		String messageBody = "";
		String query = "SELECT messageBody FROM messages WHERE id = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, id);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					messageBody = rs.getString("messageBody");
				}
			}
		}
		return messageBody;
	}
	
	// checks if article is private
	public boolean isArticlePrivate(Long articleId) throws SQLException {
		boolean isPrivateBool = false;
		String query = "SELECT isPrivate FROM articles WHERE uniqueIdentifier = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, articleId);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					isPrivateBool = rs.getBoolean("isPrivate");
				}
			}
		}
		
		return isPrivateBool;
	}
	
	// adds a user to access table
	public boolean addUserToGroup(String groupName, int userId) throws SQLException {
		if(!userExists(userId)) {
			return false;
		}
		
		String query = "INSERT INTO " + groupName + "access (userId) VALUES (?)";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			
			pstmt.executeUpdate();
		}
		
		return true;
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
	
	// This function returns each table name of the article groups only once
	public List<String> getArticleGroupNames() throws SQLException {
		List<String> names = new ArrayList<>();
		try (Statement statement = connection.createStatement()) {
			String query = "SELECT DISTINCT \"group\" FROM articles";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
            	String groupName = resultSet.getString("group");
            	names.add(groupName);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return names;
	}
	
	// returns a single group name
	public String getSingleArticleGroupNameString(Long uniqueIdentifier) throws SQLException {
		String groupName = "";
		
		String query = "SELECT \"group\" FROM articles WHERE uniqueIdentifier = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, uniqueIdentifier);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					groupName = rs.getString("group");
				}
			}
		}
		
		return groupName;
	}
	
	// This function returns a list of article titles and descriptions
	public List<String> getArticles() throws SQLException {
		List<String> articles = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT \"title\", \"description\", isPrivate FROM articles";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	if(resultSet.getBoolean("isPrivate") == true) {
                		StringBuilder result = new StringBuilder();
                		
    	            	String titleString = resultSet.getString("title");
    	            	String descriptionString = resultSet.getString("description");
    	            	
    	            	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "YES"));
    	            	String formattedString = result.toString();
    	            	
    	            	articles.add(formattedString);
            	}
            	else {
            		StringBuilder result = new StringBuilder();
            		
	            	String titleString = resultSet.getString("title");
	            	String descriptionString = resultSet.getString("description");
	            	
	            	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "NO"));
	            	String formattedString = result.toString();
	            	
	            	articles.add(formattedString);
            	}
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
	}
	
	// This function returns articles from specific group
	public List<String> getArticlesFromGroup(String groupName) throws SQLException {
		List<String> articles = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT \"title\", \"description\", isPrivate FROM " + groupName + "articles";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	if(resultSet.getBoolean("isPrivate") == true) {
                		StringBuilder result = new StringBuilder();
                		
    	            	String titleString = resultSet.getString("title");
    	            	String descriptionString = resultSet.getString("description");
    	            	
    	            	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "YES"));
    	            	String formattedString = result.toString();
    	            	
    	            	articles.add(formattedString);
            	}
            	else {
            		StringBuilder result = new StringBuilder();
            		
	            	String titleString = resultSet.getString("title");
	            	String descriptionString = resultSet.getString("description");
	            	
	            	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "NO"));
	            	String formattedString = result.toString();
	            	
	            	articles.add(formattedString);
            	}
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }
	
	// getArticlesFromGroup overload. Returns a list of articles in a group based on their level
	public List<String> getArticlesFromGroup(String groupName, int level) throws SQLException {
		List<String> articles = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM " + groupName + "articles WHERE level = " + level;
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	if(resultSet.getBoolean("isPrivate") == true) {
                		StringBuilder result = new StringBuilder();
                		
    	            	String titleString = resultSet.getString("title");
    	            	String descriptionString = resultSet.getString("description");
    	            	
    	            	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "YES"));
    	            	String formattedString = result.toString();
    	            	
    	            	articles.add(formattedString);
            	}
            	else {
            		StringBuilder result = new StringBuilder();
            		
	            	String titleString = resultSet.getString("title");
	            	String descriptionString = resultSet.getString("description");
	            	
	            	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "NO"));
	            	String formattedString = result.toString();
	            	
	            	articles.add(formattedString);
            	}
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }
	
	// This function returns ids from a specific group
	public List<Long> getArticleIdsFromGroup(String groupName) throws SQLException {
		List<Long> idList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT uniqueIdentifier FROM " + groupName + "articles";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	idList.add(resultSet.getLong("uniqueIdentifier"));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idList;
    }
	
	// This function returns the body of an article
	public String getBody(Long uniqueIdentifier) throws Exception {
		String body = "";
		
		String query = "SELECT \"body\", isPrivate FROM articles WHERE uniqueIdentifier = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, uniqueIdentifier);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					body = rs.getString("body");
					if(rs.getBoolean("isPrivate") == true && (HelpSystem.userManager.getSessionRole().contains("admin") == true || HelpSystem.userManager.getSessionRole().contains("instructor") == true)) {
						char[] decryptedBody = EncryptionUtils.toCharArray(
								encryptionHelper.decrypt(
										Base64.getDecoder().decode(
												body
										), 
										EncryptionUtils.getInitializationVector(getTitle(uniqueIdentifier).toCharArray())));
						body = new String(decryptedBody);
					}
				}
			
			}
		}
		return body;
	}
	
	// This function returns the title of an article
	public String getTitle(Long uniqueIdentifier) throws SQLException {
		String title = "";
			
		String query = "SELECT \"title\" FROM articles WHERE uniqueIdentifier = ?";
			
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, uniqueIdentifier);
				
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					title = rs.getString("title");
				}
		
			}
		}
		return title;
	}
	
	// Returns the authors of an article
	public String getAuthors(Long uniqueIdentifier) throws SQLException{
		String authors = "";
		
		String query = "SELECT authors FROM articles WHERE uniqueIdentifier = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, uniqueIdentifier);
				
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					authors = rs.getString("authors");
				}
			}
		}
		return authors;
	}
	
	public String getGroup(Long uniqueIdentifier) throws SQLException{
		String group = "";
		
		String selectGroup = "SELECT group FROM articles WHERE uniqueIdentifier = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(selectGroup)){
			pstmt.setLong(1, uniqueIdentifier);
			
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					group = rs.getString("group");
				}
			}
		}
		return group;
	}
	
	// returns a list of articles based on their level
	public List<String> getArticlesBasedOnLevel(int level) {
		List<String> articlesBasedOnLevel = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM articles WHERE level = " + level;
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	if(resultSet.getBoolean("isPrivate") == true) {
            		StringBuilder result = new StringBuilder();
            		
	            	String titleString = resultSet.getString("title");
	            	String descriptionString = resultSet.getString("description");
	            	
	            	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "YES"));
	            	String formattedString = result.toString();
	            	
	            	articlesBasedOnLevel.add(formattedString);
        	}
        	else {
        		StringBuilder result = new StringBuilder();
        		
            	String titleString = resultSet.getString("title");
            	String descriptionString = resultSet.getString("description");
            	
            	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "NO"));
            	String formattedString = result.toString();
            	
            	articlesBasedOnLevel.add(formattedString);
        	}
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articlesBasedOnLevel;
	}
	
	// This function returns the articles ids
	public List<Long> getArticleIds() throws SQLException {
		List<Long> idList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT uniqueIdentifier FROM articles";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	idList.add(resultSet.getLong("uniqueIdentifier"));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idList;
	}
	
	public List<String> searchArticleWKeyword(String keyword, String group) throws SQLException {
		List<String> searchedArticles = new ArrayList<>();
		String searchKeyword = "SELECT * FROM " + group + "articles WHERE CONCAT(uniqueIdentifier, ' ',TRIM(\"title\"), ' ', TRIM(authors), ' ', TRIM(\"description\"), ' ', level) LIKE '%" + keyword + "%'" ;
		try(Statement statement = connection.createStatement()){
			ResultSet rs = statement.executeQuery(searchKeyword);
			while (rs.next()) {
	            if(rs.getBoolean("isPrivate") == true) {
	            	StringBuilder result = new StringBuilder();
	            	
		           	String titleString = rs.getString("title");
		           	String descriptionString = rs.getString("description");
		           	
		           	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "YES"));
		           	String formattedString = result.toString();
		           	
		           	searchedArticles.add(formattedString);
	            }
		        else {
		        	StringBuilder result = new StringBuilder();
		        	
		           	String titleString = rs.getString("title");
		           	String descriptionString = rs.getString("description");
		           	
		           	result.append(String.format("%-" + 19 + "s %-" + 44 + "s %s%n", titleString, descriptionString, "NO"));
		           	String formattedString = result.toString();
		           	
		           	searchedArticles.add(formattedString);
		        }
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
		return searchedArticles;
	}

	// This function adds an article to the articles table
	public void addArticle(HelpArticle article, int userId) throws Exception {
		// grab variables
		String title = article.getTitle();
		String body;
		String description = article.getDescription();
		String group  = article.group();
		String authors = article.getAuthors();
		long identifier  = article.getIdentifier();
		int level = article.getLevel();
		boolean isPrivate = article.isPrivate();
		
		// if new article is private, then encrypt the body of the article
		if(isPrivate) {
			body = Base64.getEncoder().encodeToString(
					encryptionHelper.encrypt(article.getBody().getBytes(), EncryptionUtils.getInitializationVector(title.toCharArray()))
					);
		}
		else {
			body = article.getBody();
		}
		
		// 1st: insert the article in the group table if group was specified. If group specified does not have a table, create one and insert into that table
		String getGroups = "SELECT \"group\" FROM articles";
		try(Statement pstmt = connection.createStatement()){
			ResultSet rs = pstmt.executeQuery(getGroups);
			// if there are existing groups in the group column
			if(rs.next()) {
				boolean added = false;
				// Search the group column to see if another article with the same group column has been created previously
				while(rs.next()) {
					// if program finds a matching group in the group column, and the group's name is not "empty", then insert the new article into that group's table
					if(rs.getString("group").equals(group) == true && group.equals("") == false) {
						String insertArticle = "INSERT INTO "+ group +"articles (\"title\", authors, \"body\", \"description\", \"group\", uniqueIdentifier, level, isPrivate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
						try (PreparedStatement pstmt3 = connection.prepareStatement(insertArticle)) {
							pstmt3.setString(1, title);
							pstmt3.setString(2, authors);
							pstmt3.setString(3, body);
							pstmt3.setString(4, description);
							pstmt3.setString(5, group);
							pstmt3.setLong(6, identifier);
							pstmt3.setInt(7, level);
							pstmt3.setBoolean(8, isPrivate);
							pstmt3.executeUpdate();
							System.out.println("Article created successfully in "+ group +"articles table.");
							added = true;
							break;
						} catch(Exception e) {
							System.out.println("Article could not be created successfully.");
							e.printStackTrace();
						}
					}
				}
				// if the article has not been added yet because the group is not in the group column, and the group's name is not "empty",
				// then create a new table with that group's name and insert the new article into that table
				if(!added && group.equals("") == false) {
					String createGroupTable = "CREATE TABLE IF NOT EXISTS " + group + "articles ("
							+ "id INT AUTO_INCREMENT PRIMARY KEY, "
							+ "uniqueIdentifier BIGINT DEFAULT 0, "
							+ "isPrivate BOOLEAN DEFAULT FALSE, "
							+ "level INT DEFAULT 0, "
							+ "\"title\" VARCHAR(25), "
							+ "authors VARCHAR(25), "
							+ "\"description\" VARCHAR(25), "
							+ "\"group\" VARCHAR(25), "
							+ "\"body\" CLOB)";
					pstmt.execute(createGroupTable);
					
					createAccessTable(group, userId);
					
					String insertArticle = "INSERT INTO "+ group +"articles (\"title\", authors, \"body\", \"description\", \"group\", uniqueIdentifier, level, isPrivate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
					try (PreparedStatement pstmt3 = connection.prepareStatement(insertArticle)) {
						pstmt3.setString(1, title);
						pstmt3.setString(2, authors);
						pstmt3.setString(3, body);
						pstmt3.setString(4, description);
						pstmt3.setString(5, group);
						pstmt3.setLong(6, identifier);
						pstmt3.setInt(7, level);
						pstmt3.setBoolean(8, isPrivate);
						pstmt3.executeUpdate();
						System.out.println("Article created successfully in "+ group +"articles table.");
					} catch(Exception e) {
						System.out.println("Article could not be created successfully.");
						e.printStackTrace();
					}
				}
			}
			// if there are no groups in the group column, create a table with the new article group's name
			else {
				if(group.equals("") == false) {
					String createGroupTable = "CREATE TABLE IF NOT EXISTS " + group + "articles ("
							+ "id INT AUTO_INCREMENT PRIMARY KEY, "
							+ "uniqueIdentifier BIGINT DEFAULT 0, "
							+ "isPrivate BOOLEAN DEFAULT FALSE, "
							+ "level INT DEFAULT 0, "
							+ "\"title\" VARCHAR(25), "
							+ "authors VARCHAR(25), "
							+ "\"description\" VARCHAR(25), "
							+ "\"group\" VARCHAR(25), "
							+ "\"body\" CLOB)";
					pstmt.execute(createGroupTable);
					
					//createAccessTable(group, userId);
					
					String insertArticle = "INSERT INTO "+ group +"articles (\"title\", authors, \"body\", \"description\", \"group\", uniqueIdentifier, level, isPrivate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
					try (PreparedStatement pstmt3 = connection.prepareStatement(insertArticle)) {
						pstmt3.setString(1, title);
						pstmt3.setString(2, authors);
						pstmt3.setString(3, body);
						pstmt3.setString(4, description);
						pstmt3.setString(5, group);
						pstmt3.setLong(6, identifier);
						pstmt3.setInt(7, level);
						pstmt3.setBoolean(8, isPrivate);
						pstmt3.executeUpdate();
						System.out.println("Article created successfully in "+ group +"articles table.");
					} catch(Exception e) {
						System.out.println("Article could not be created successfully in " + group + "articles table.");
						e.printStackTrace();
					}
				}
			}
			
			// 2nd: insert the article in the general articles table
			String insertArticle = "INSERT INTO articles (\"title\", authors, \"body\", \"description\", \"group\", uniqueIdentifier, level, isPrivate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt1 = connection.prepareStatement(insertArticle)) {
				
				pstmt1.setString(1, title);
				pstmt1.setString(2, authors);
				pstmt1.setString(3, body);
				pstmt1.setString(4, description);
				pstmt1.setString(5, group);
				pstmt1.setLong(6, identifier);
				pstmt1.setInt(7, level);
				pstmt1.setBoolean(8, isPrivate);
				pstmt1.executeUpdate();
				System.out.println("Article created successfully in general articles table.");
			} catch(Exception e) {
				System.out.println("Article could not be created successfully.");
				e.printStackTrace();
			}
		}
	}
	
	// This function creates a table that contains all users that have access to a specific group
	public void createAccessTable(String groupName, int userId) throws SQLException {
		String createAccessTable = "CREATE TABLE IF NOT EXISTS " + groupName + "access ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userId INT DEFAULT 0)";
		
		Statement stmt = connection.createStatement();
		stmt.execute(createAccessTable);
		
		String insertCreator = "INSERT INTO " + groupName + "access (userId) VALUES (?)";
		
		try(PreparedStatement pstmt = connection.prepareStatement(insertCreator)) {
			pstmt.setInt(1, userId);
			
			pstmt.executeUpdate();
		}
	}
	
	// This function deletes an article from the article table
	public boolean deleteArticle(String title, long identifier) throws Exception{
		
		String sql = "DELETE FROM articles WHERE uniqueIdentifier = ? AND \"title\" = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.setLong(1, identifier);
			pstmt.setString(2, title);
			pstmt.executeUpdate();
			System.out.println("Article deleted successfully.");
			return true;
		} catch(Exception e) {
			System.out.println("Article could not be deleted.");
			return false;
		}
	}
	
	// Backs up the articles table into a zip file and stores it in the local user folder
	public boolean backupArticles() throws Exception {
		// Gets the session role of the user that's currently logged, gets the users folder path, 
		// and sets the name of the backup file with the user's session role
		String role = HelpSystem.getSessionRole();
		String path = System.getProperty("user.home");
		String backupPath = path + "/" + role + "ArticlesBackup.zip";
		
		Statement stmt = connection.createStatement();
		
		// 1st create a new temporary table with the properties of the articles table
		String tempTable = "CREATE TABLE IF NOT EXISTS articles_backup ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "uniqueIdentifier BIGINT DEFAULT 0, "
				+ "isPrivate BOOLEAN DEFAULT FALSE, "
				+ "level INT DEFAULT 0, "
				+ "\"title\" VARCHAR(25), "
				+ "\"description\" VARCHAR(25), "
				+ "\"group\" VARCHAR(25), "
				+ "\"body\" CLOB)";
		stmt.execute(tempTable);
		
		// 2nd copy the data from the articles table into the temporary articles_backup table
		String copyData = "INSERT INTO articles_backup SELECT * FROM articles";
		stmt.execute(copyData);
		
		// fix id AUTO_INCREMENT in backup table
		int currentID;
		String query = "SELECT MAX(id) FROM articles_backup";
		try(ResultSet rs = stmt.executeQuery(query)){
			if(rs.next()) {
				currentID = rs.getInt(1);
				
				String fixIdBackup = "ALTER TABLE articles_backup ALTER COLUMN id RESTART WITH " + (currentID + 1);
				
				stmt.execute(fixIdBackup);
			}
		}
		
		// 3rd backup is created from articles_backup table. The name of the backed up table is articles_backup
		String backup = "SCRIPT TO '" + backupPath + "' COMPRESSION ZIP TABLE articles_backup";	// Backs up the articles table
		stmt.executeQuery(backup);
		
		//4th articles_backup table is removed from the database
		String removeTable = "DROP TABLE IF EXISTS articles_backup";
		stmt.execute(removeTable);
		
		// File class will be used to check if the backup was created successfully
		File file = new File(backupPath);
		
		if(file.exists()) {
			System.out.println("Backup created successfully.");
			return true;
		}
		else {
			System.out.println("Failure! Backup was not created.");
			return false;
		}
	}

	// Restores the database from the zip file created in the backupArticles functions
	public boolean removeRestoreBackup() throws Exception {
		String role = HelpSystem.getSessionRole();
		String path = System.getProperty("user.home");
		String backupPath = path + "/" + role + "ArticlesBackup.zip";
		
		String removeTable = "DROP TABLE IF EXISTS articles";	// Empties the current articles table
		String restoreBackup = "RUNSCRIPT FROM '" + backupPath + "' COMPRESSION ZIP";	// Restores the backed up articles table
		String renameTableString = "ALTER TABLE articles_backup RENAME TO articles";
		
		try(Statement stmt = connection.createStatement()){
			stmt.execute(removeTable); 			// removes pre-existing tables before restoring backup
			stmt.execute(restoreBackup);		// restores the table called articles_backup
			stmt.execute(renameTableString); 	// renames "articles_backup" table to "articles"
			System.out.println("Backup was restored successfully."); 
			return true;
		
		} catch (Exception e){
			System.out.println("Failure! Backup could not be restored.");
			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	// Restores the general articles table and merges it with the current general articles table
	public boolean mergeRestoreBackup() throws Exception {		
		String role = HelpSystem.getSessionRole();
		String path = System.getProperty("user.home");
		String backupPath = path + "/" + role + "ArticlesBackup.zip";
		
		// Runs the backup file
		String LoadBackup = "RUNSCRIPT FROM '" + backupPath + "' COMPRESSION ZIP";
		// This SQL command merges the backup to the articles table except for the repeated articles
		String mergeTable = "INSERT INTO articles (\"title\", \"body\", \"description\", \"group\", uniqueIdentifier, level, isPrivate) "
				+ "SELECT \"title\", \"body\", \"description\", \"group\", uniqueIdentifier, level, isPrivate "
				+ "FROM articles_backup "
				+ "WHERE uniqueIdentifier NOT IN (SELECT uniqueIdentifier FROM articles)";
		// Drop the backup table 
		String removeBackupTable = "DROP TABLE articles_backup";
		
		try(Statement stmt = connection.createStatement()) {
			stmt.execute(LoadBackup);
			stmt.execute(mergeTable);
			stmt.execute(removeBackupTable);
			System.out.println("Back up merged successfully.");
			return true;
		} catch (Exception e) {
			statement.execute(removeBackupTable);
			System.out.println("Failure! Backup could not be merged."); 
			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
			return false;
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
