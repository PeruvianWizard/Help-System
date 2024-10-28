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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
	
    @FXML
    private Label oneTimeCodeLabel;
	
	// These checkboxes will be used to generate a one-time code invitation
    @FXML
    private CheckBox newAdmin;

    @FXML
    private CheckBox newInstructional;

    @FXML
    private CheckBox newStudent;
    
    // These variables will work with the checkboxes
    private String willBeAdmin = "";
    private String willBeInstructional = "";
    private String willBeStudent = "";
	
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
    
    // switches to 
    public void SwitchToDeleteUserWindow(ActionEvent event) throws IOException {
    	Parent theRoot = FXMLLoader.load(getClass().getResource("DeleteUserWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
    
    public void SwitchToModifyUserRoleWindow(ActionEvent event) throws IOException {
    	Parent theRoot = FXMLLoader.load(getClass().getResource("ModifyUserRoleWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
    
    public void SwitchToListUsersScreen(ActionEvent event) throws IOException {
    	Parent theRoot = FXMLLoader.load(getClass().getResource("ListUserScreen.fxml"));
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
    
    /** Functions AdminBox, InstructionalBox, and StudentBox will set the values of willBeAdmin, willBeInstructional, and willBeStudent
     *  which then will be used by the generateOneTimeCode function below to generate an invitation code to a new User
     */
    @FXML
    void AdminBox(ActionEvent event) {
    	if(newAdmin.isSelected()) {
    		willBeAdmin = "admin";
    	}
    }

    @FXML
    void InstructionalBox(ActionEvent event) {
    	if(newInstructional.isSelected()) {
    		willBeInstructional = "instructional";
    	}
    }

    @FXML
    void StudentBox(ActionEvent event) {
    	if(newStudent.isSelected()) {
    		willBeStudent = "student";
    	}
    }

    @FXML
    void generateOneTimeCode(ActionEvent event) throws SQLException {
    	// Using oneTimePassword class to generate a one time code
    	OneTimePassword invCode = new OneTimePassword();
    	String code = invCode.returnPassword(); 
    	
    	// Change label text to display new oneTimeCode
    	oneTimeCodeLabel.setText(willBeAdmin + willBeInstructional + willBeStudent + code);
    	
    	// Set label to visible
    	oneTimeCodeLabel.setVisible(true);
    	
    	//add code to the database
    	HelpSystem.registerCode(code, willBeAdmin, willBeInstructional, willBeStudent);
    	
    	willBeAdmin = "";
		willBeInstructional = "";
		willBeStudent = "";
    }
	
}
