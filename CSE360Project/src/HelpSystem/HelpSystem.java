package HelpSystem;

public class HelpSystem {
	/**This function creates a user with a username and password*/
	public void createUser(String username, char[] password) {
		User newUser = new User(username, password);
	}
}
