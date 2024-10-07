package HelpSystem;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//We'll use this class to switch windows, or "scenes" 
public class SceneController {

	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	/** This function will allow us to switch to the main/first window */
	public void switchToHelpSystemWindow(ActionEvent event) throws IOException {
		Parent theRoot = FXMLLoader.load(getClass().getResource("HelpSystemWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
	
	/** This function will allow us to switch to the log in window */
	public void switchToLogInWindow(ActionEvent event) throws IOException {
		Parent theRoot = FXMLLoader.load(getClass().getResource("LogInWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
	
	
}
