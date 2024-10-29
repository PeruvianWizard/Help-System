package HelpSystem;

import java.io.IOException;

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

public class LogInController {
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	/** The variables logInButton, passwordInput1, passwordInput2, and usernameInput 
	 * come from the LogInWindow.fxml file
	 */
    @FXML
    private Button logInButton;

    @FXML
    private Label passIncorrectLabel;
    
    @FXML
    private PasswordField passwordInput1;

    @FXML
    private TextField usernameInput;

    /** variables to hold inputs */
    String username;
    String password;
    
    boolean passSame = false;
    
    
    /**setUser function activates when the log in button is pressed, it should get the values in the text boxes
     * and store them in variables "username" and "password". We will use these variables to creates users for the system(how? no idea yet)
     * @throws Exception 
     */
    @FXML
    void verifyCredentials(ActionEvent event) throws Exception {
    	username = usernameInput.getText();						//stores the input
    	password = passwordInput1.getText();		//This line gets the password input and converts it to a array of characters
    	
    	// checks if username and password is in the database
    	if(HelpSystem.isUser(username, password)) {
    		
    		// creates a new user who is logged in 
    		User loggedInUser = new User(username, password);
    		HelpSystem.logInUser(loggedInUser);
    		
    		// stores auth as local variable
    		String auth = HelpSystem.userDatabaseHelper.checkAuth(username);
    		
    		//checks if user needs to reset their password
    		if(HelpSystem.userDatabaseHelper.needsPasswordReset(username)) {
    			Parent theRoot = FXMLLoader.load(getClass().getResource("EnterNewPasswordWindow.fxml"));
        		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        		theScene = new Scene(theRoot);
        		theStage.setScene(theScene);
        		theStage.show();
    		}
    		
    		// checks if account is already updated, if not, redirect to finish account screen
    		if(HelpSystem.userDatabaseHelper.isUpdated(loggedInUser.username)) {
    			System.out.println("User already updated!");
    			
    			// display correct screen for specific user. 
    			// Each if-statement will initialize the "theRoot" variable declared  
    			// at the top of the class with the respective window.
        		if(auth.equals("admin")) {
        			// sets session role of a user
        			HelpSystem.setSessionRole(auth);
        			// updates screen
            		theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
            		
        		} else if(auth.equals("student")) {
        			HelpSystem.setSessionRole(auth);
        			// updates screen
            		theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
            		
        		} else if (auth.equals("instructor")){
        			HelpSystem.setSessionRole(auth);
        			// updates screen
            		theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
            		
        		} else {
        			// switch to screen where user will decide who they want to log in as
        			theRoot = FXMLLoader.load(getClass().getResource("LogInAsRoleWindow.fxml"));
        		} 
        		
        		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        		theScene = new Scene(theRoot);
        		theStage.setScene(theScene);
        		theStage.show();
    		} else {
    			// updates screen to finish setting up account
        		Parent theRoot = FXMLLoader.load(getClass().getResource("FinishSettingUpAccountWindow.fxml"));
        		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        		theScene = new Scene(theRoot);
        		theStage.setScene(theScene);
        		theStage.show();
        		
    		}
    		return;
    	} else {
    		passIncorrectLabel.setVisible(true);
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
