package HelpSystem;

import java.sql.SQLException;
import java.util.Scanner;

/** The HelpSystem class will hold the UserDatabaseHelper variable*/
public class HelpSystem {
	// This creates an instance of the user database helper. Defined as protected so that it can be used by the GUI Controllers
	protected static final UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
	// This is just a scanner although idk if it has any use here yet
	private static final Scanner scanner = new Scanner(System.in);
	
	// this functions registers the Administrator to the database for the first time
	protected static void setupAdministrator(String username, char[] password) throws SQLException {
		
		userDatabaseHelper.register(username, password, "admin");
		System.out.println("Administrator setup completed.");

	}
	
	// assumed admin, working on creating a checkbox to separate different roles.
	protected static boolean isUser(String username, char[] password) throws SQLException {
		
		String passtemp = password.toString();
		return userDatabaseHelper.login(username, passtemp, "admin");
	}
	
	
}
