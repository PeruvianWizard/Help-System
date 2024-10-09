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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FinishSettingUpAccountWindowController {
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	/** The variables logInButton, passwordInput1, passwordInput2, and usernameInput 
	 * come from the LogInWindow.fxml file
	 */
    @FXML
    private Button updateButton;
    
    @FXML
    private TextField firstNameInput;

    @FXML
    private TextField lastNameInput;

    @FXML
    private TextField middleNameInput;
    
    @FXML
    private TextField emailInput;
    
    @FXML
    private TextField preferredNameInput;

    /** variables to hold inputs */
    String username, email, firstName, middleName, lastName, preferredName; 
    
    @FXML
    public void updateUser(ActionEvent event) throws IOException, SQLException{
    	username = HelpSystem.getUsername();
    	email = emailInput.getText();
    	firstName = firstNameInput.getText();
    	middleName = middleNameInput.getText();
    	lastName = lastNameInput.getText();
    	preferredName = preferredNameInput.getText();
    	
    	// updates the user with new information and stores in new columns
    	HelpSystem.updateUser(username, firstName, middleName, lastName, preferredName, email);
    	
    	String auth = HelpSystem.userDatabaseHelper.checkAuth(username);
    	//display correct screen for specific user
		if(auth.equals("admin")) {
			// updates screen
    		Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
		} else if(auth.equals("student")) {
			// display student screen
			// 
			// WIP
		} else { 
			// display instructor screen
			//
			// WIP
		}
    }
    
    /**This function switches back to the main window when the button "back" is clicked
     */
    public void switchToHelpSystemWindow(ActionEvent event) throws IOException {
		Parent theRoot = FXMLLoader.load(getClass().getResource("HelpSystemLogInWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
    
}
