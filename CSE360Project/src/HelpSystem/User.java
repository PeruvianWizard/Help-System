package HelpSystem;

import java.sql.SQLException;

public class User {
	private String password;
	private String firstName;
	private String lastName;
	private String middleName;
	private String preferredName;
	private boolean flag;
	private int passwordTimer;
	private String passwordDate;
	private String currentRole;
	private int userId = -1;
	private String messageString;
	
	public String email;
	public String username;
	
	/** Base constructor*/
	public User() {
	}
	/** Constructor to create users with a username and a password
	 * @throws SQLException */
	public User(String username, String password) throws SQLException {
		this.username = username;
		this.password = password;
		
		this.userId = HelpSystem.userDatabaseHelper.getUserId(username);
	}
	
	/** Getter methods */
	public String getFirstName() {
		return firstName;
	}
	public String getCurrentRole() {
		return currentRole;
	}
	
	public int getUserId() {
		return userId;
	}
	
	/** Setter methods */
	public void setPreffered(String preferredName) {
		this.preferredName = preferredName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}
}
