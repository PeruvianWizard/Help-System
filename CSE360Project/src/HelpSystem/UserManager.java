package HelpSystem;

import java.util.ArrayList; 
import java.util.List;

// this class manages and checks who is logged in, so the application can display the correct screens
public class UserManager {
	private User loggedInUser;
	
	public UserManager() {
		
	}
	
	public void LogInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser; 
		return; 
	}
	
	public String getUsername() {
		return loggedInUser.username;
	}
}
