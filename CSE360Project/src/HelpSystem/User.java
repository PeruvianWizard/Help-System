package HelpSystem;

public class User {
	private String password;
	private String firstName;
	private String lastName;
	private String middleName;
	private String preferredName;
	private boolean flag;
	private int passwordTimer;
	private String passwordDate;
	private boolean isStudent = false;
	private boolean isAdmin = false;
	private boolean isInstructionalTeam = false;
	
	public String email;
	public String username;
	
	/** Base constructor*/
	public User() {
	}
	/** Constructor to create users with a username and a password*/
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/** Getter methods */
	public String getFirstName() {
		return firstName;
	}
	public boolean isStudent() {
		return isStudent;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public boolean isInstructionalTeam() {
		return isInstructionalTeam;
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
	public void setIsStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public void setIsInstructionalTeam(boolean isInstructionalTeam) {
		this.isInstructionalTeam = isInstructionalTeam;
	}
}
