package HelpSystem;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//We'll use this class to switch windows, or "scenes," and perform operations on these scenes
public class SceneController {

	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	/** This function will allow us to switch to the register window when the button "Register" is clicked*/
	public void switchToRegisterWindow(ActionEvent event) throws IOException {
		theRoot = FXMLLoader.load(getClass().getResource("RegisterWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
	
	public void switchToLogInWindow(ActionEvent event) throws IOException {
		theRoot = FXMLLoader.load(getClass().getResource("LogInWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
	
}
