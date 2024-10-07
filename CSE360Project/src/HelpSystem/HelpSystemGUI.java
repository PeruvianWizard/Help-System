package HelpSystem;

import java.sql.SQLException;

//JavaFX imports needed to support the Graphical User Interface
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

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
		stage.setTitle("Help System Phase 1");
		
		
		
		Parent root = FXMLLoader.load(getClass().getResource("HelpSystemLogInWindow.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	
	
	public static void main(String[] args) {
		//launch(args);
		
		try {
			
			HelpSystem.userDatabaseHelper.connectToDatabase();
			launch(args);
			
			
		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			System.out.println("Good Bye!!");
			HelpSystem.userDatabaseHelper.closeConnection();
		}
	}
	
	
}
