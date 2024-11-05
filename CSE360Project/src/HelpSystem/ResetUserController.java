package HelpSystem;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResetUserController {
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
    private Button resetButton;
	
	@FXML
    private TextField usernameInput;
	
	@FXML
    private Label usernameIncorrect;
	
	@FXML
    private Label codeLabel;
	
	// checks if user is in system
	private boolean checkIfUserExist(String username) throws SQLException {
		if(!HelpSystem.userDatabaseHelper.checkAuth(username).equals("-1")) {
			return true;
		} else {
			return false; 
		}
	}
	
	// displays one time code
	public void resetPassword(ActionEvent event) throws Exception {
		String username = usernameInput.getText();
		if(checkIfUserExist(username)) {
			
			OneTimePassword code = new OneTimePassword(); 
			
			String resetCode = code.returnPassword();
			
			codeLabel.setText(resetCode);
			
			HelpSystem.userDatabaseHelper.changePasswordWithCode(username, resetCode);
			
			// TO DO
			// use code when user logs in next time (maybe add a "isBeingReset" boolean column to database?)
			// can use same code for creating one time code for invites
		}
	}
	
	/**This function switches back to the main window when the button "back" is clicked
     */
    public void switchToAdminSystemWindow(ActionEvent event) throws IOException {
		theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
}
