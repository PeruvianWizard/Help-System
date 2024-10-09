package HelpSystem;

import java.sql.SQLException;
import java.util.Scanner;

/** The HelpSystem class will hold the UserDatabaseHelper variable*/
public class HelpSystem {
	// This creates an instance of the user database helper. Defined as protected so that it can be used by the GUI Controllers
	protected static final UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
	// This is just a scanner although idk if it has any use here yet
	private static final Scanner scanner = new Scanner(System.in);
	
	// Also creating user manager class to help manage users
	protected static final UserManager userManager = new UserManager();
	
	// this functions registers the Administrator to the database for the first time
	protected static void setupAdministrator(String username, char[] password) throws SQLException {
		
		userDatabaseHelper.register(username, password, "admin");
		System.out.println("Administrator setup completed.");

	}
	
	// this function updates an existing account with new information
	protected static void updateUser(String username, String firstName, String middleName, String lastName, String preferredName, String email) throws SQLException {
		userDatabaseHelper.updateUser(username, firstName, middleName, lastName, preferredName, email);
		System.out.print("Account updated succeffully");
	}
	
	// searches through each role to determine if the entered username and password are in the system, regardless of role. 
	protected static boolean isUser(String username, char[] password) throws SQLException {
		
		String passtemp = new String(password);
		if(userDatabaseHelper.login(username, passtemp, "admin")) {
			return true; 
		} else if(userDatabaseHelper.login(username, passtemp, "student")) {
			return true; 
		} else if(userDatabaseHelper.login(username, passtemp, "instructor")) {
			return true; 
		} else {
			return false;
		}
	}
	
	// sets the current logged in user
	protected static void logInUser(User user) {
		userManager.LogInUser(user);
		
		return; 
	}
	
	protected static String getUsername() {
		return userManager.getUsername(); 
	}
	
	
}
