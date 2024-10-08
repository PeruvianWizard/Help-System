package HelpSystem;

import java.sql.SQLException;
import java.util.Scanner;

/** The HelpSystem class will hold the UserDatabaseHelper variable*/
public class HelpSystem {
	// This creates an instance of the user database helper. Defined as protected so that it can be used by the GUI Controllers
	protected static final UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
	// This is just a scanner although idk if it has any use here yet
	private static final Scanner scanner = new Scanner(System.in);
	
	
	protected static void setupAdministrator(String username, char[] password) throws SQLException {
		
		
		userDatabaseHelper.register(username, password, "admin");
		System.out.println("Administrator setup completed.");

	}
	
	
}
