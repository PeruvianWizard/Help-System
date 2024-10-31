package HelpSystem;

import java.sql.SQLException;

//JavaFX imports needed to support the Graphical User Interface
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

public class HelpSystemGUI extends Application{
	
	//Base Constructor
	public HelpSystemGUI() {
	}
	
	/**Note: I looked up a way to be able to go from one window to the next,
	 * and I found that we could implement that using a FXML controller. This controller
	 * will allow us to navigate from one window to the next and so on
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Help System Phase 2");
		
		Parent root;
		
		if(HelpSystem.userDatabaseHelper.isDatabaseEmpty()) {
			root = FXMLLoader.load(getClass().getResource("FirstEverLogInWindow.fxml"));
		}
		else {
			root = FXMLLoader.load(getClass().getResource("HelpSystemLogInWindow.fxml"));
		}
		
		//Parent root = FXMLLoader.load(getClass().getResource("FirstEverLogInWindow.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	
	
	public static void main(String[] args) {
		//launch(args);
		
		try {
			
			HelpSystem.userDatabaseHelper.connectToDatabase();
			launch(args);
			
			//list test cases here (can only do one at a time for now)
			//performTestCase(1, "Article Title", true);
			 
			
		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			System.out.println("Good Bye!!");
			HelpSystem.userDatabaseHelper.closeConnection();
		}
	}
	
	//allows for testing similar to PasswordEvaluationTestingAutomation Homework
	private static void performTestCase(int testCase, String title, boolean expectedPass) {
	    System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
	    System.out.println("Input Title: \"" + title + "\"");
	    System.out.println("______________");
	    System.out.println("\nFinite state machine execution trace:");

	    boolean titleExists = false;

	    try (Connection connection = HelpSystem.userDatabaseHelper.getConnection()) {
	        // Query the database to check for a matching title
	        String query = "SELECT 1 FROM articles WHERE \"title\" = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setString(1, title);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                titleExists = rs.next();
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("SQL error occurred while fetching the title: " + e.getMessage());
	        e.printStackTrace();
	    }

	    System.out.println();

	    // Check if the result is with the expected outcome
	    if (titleExists) {
	        if (expectedPass) {
	            System.out.println("***Success*** The title \"" + title + "\" exists, so this is a pass!");
	        } else {
	            System.out.println("***Failure*** The title \"" + title + "\" exists," + 
	                               "\nBut it was expected not to exist, so this is a failure!");
	        }
	    } else {
	        if (expectedPass) {
	            System.out.println("***Failure*** The title \"" + title + "\" does not exist," +
	                               "\nBut it was expected to exist, so this is a failure!");
	        } else {
	            System.out.println("***Success*** The title \"" + title + "\" does not exist, so this is a pass!");
	        }
	    }
	}
}
