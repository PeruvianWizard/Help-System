package HelpSystem;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminWindowController {
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;

	@FXML
    private Button resetUserAccountButton;
	
	@FXML
    private Button deleteUserAccountButton;
	
	@FXML
    private Button listUserAccounts;
	
	@FXML
    private Button modifyUserRole;
	
	@FXML
	private Button logOut;
	
	// this function resets a user's account and generates a one time password to reset
	@FXML
    void resetUser(ActionEvent event) throws IOException {
		Parent theRoot = FXMLLoader.load(getClass().getResource("ResetUserWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
	
	// switches to reset user window
    public void switchToResetUserWindow(ActionEvent event) throws IOException {
		Parent theRoot = FXMLLoader.load(getClass().getResource("ResetUserWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
    
    // logs out
    public void logOut(ActionEvent event) throws IOException {
    	HelpSystem.userManager.logOut();
    	
    	Parent theRoot = FXMLLoader.load(getClass().getResource("HelpSystemLogInWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
	
}
