package HelpSystem;

import java.sql.SQLException;
import java.util.Scanner;

/** The HelpSystem class will hold the UserDatabaseHelper variable*/
public class HelpSystem {
	// This creates an instance of the user database helper. Defined as protected so that it can be used by the GUI Controllers
	protected static UserDatabaseHelper userDatabaseHelper;
	static {
		try {
			userDatabaseHelper = new UserDatabaseHelper();
			System.out.println("UserDatabaseHelper object initialized correctly!");
		} catch (Exception e){
			System.out.println("UserDatabaseHelper object could not be initialized correctly!");
		}
	}
	
	// This is just a scanner although idk if it has any use here yet
	private static final Scanner scanner = new Scanner(System.in);
	
	// Also creating user manager class to help manage users
	protected static final UserManager userManager = new UserManager();
	
	// this functions registers the Administrator to the database for the first time
	protected static void setupAdministrator(String username, String password) throws Exception {
		
		userDatabaseHelper.register(username, password, "admin", "", "");
		System.out.println("Administrator setup completed.");

	}
	
	// this functions registers a student to the database for the first time
	protected static void setupStudent(String username, String password) throws Exception {
			
		userDatabaseHelper.register(username, password, "", "", "student");
		System.out.println("Student setup completed.");

	}
	
	// this functions registers a student to the database for the first time
	protected static void setupInstructor(String username, String password) throws Exception {
				
		userDatabaseHelper.register(username, password, "", "instructor", "");
		System.out.println("Student setup completed.");

	}
	
	protected static void setUpCustom(String username, String password, String code) throws Exception{
		if(code.contains("admininstructional")) {
			userDatabaseHelper.register(username, password, "admin", "instructor", "");
		} else if (code.contains("adminstudent")) {
			userDatabaseHelper.register(username, password, "admin", "", "student");
		} else if (code.contains("instructionalstudent")) {
			userDatabaseHelper.register(username, password, "", "instructor", "student");
		} else {
			userDatabaseHelper.register(username, password, "admin", "instructor", "student");
		} 
		System.out.println("Custom setup completed.");
	}
	
	// this function updates an existing account with new information
	protected static void updateUser(String username, String firstName, String middleName, String lastName, String preferredName, String email) throws SQLException {
		userDatabaseHelper.updateUser(username, firstName, middleName, lastName, preferredName, email);
		System.out.println("Account updated succeffully");
	}
	
	// this function will add a one time code to the code database
	protected static void registerCode(String code, String role1, String role2, String role3) throws SQLException {
		userDatabaseHelper.registerCode(code, role1, role2, role3);
	}
	
	// searches through each role to determine if the entered username and password are in the system, regardless of role. 
	protected static boolean isUser(String username, String password) throws Exception {
		
		if(userDatabaseHelper.login(username, password, "admin", "", "")) {
			return true; 
		} else if(userDatabaseHelper.login(username, password, "", "instructor", "")) { 
			return true; 
		} else if(userDatabaseHelper.login(username, password, "", "", "student")) {
			return true; 
		} else if(userDatabaseHelper.login(username, password, "admin", "instructor", "")) {
			return true;
		} else if(userDatabaseHelper.login(username, password, "admin", "", "student")) {
			return true;
		} else if(userDatabaseHelper.login(username, password, "", "instructor", "student")) {
			return true;
		} else if(userDatabaseHelper.login(username, password, "adming", "instructor", "student")) {
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
