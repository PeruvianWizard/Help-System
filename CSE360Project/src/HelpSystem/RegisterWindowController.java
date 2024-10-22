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

public class RegisterWindowController {
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	/** The variables logInButton, passwordInput1, passwordInput2, and usernameInput 
	 * come from the LogInWindow.fxml file
	 */
    @FXML
    private Button registerButton;

    @FXML
    private Label passNotSameLabel;
    
    @FXML
    private PasswordField passwordInput1;

    @FXML
    private PasswordField passwordInput2;

    @FXML
    private TextField usernameInput;
    
    @FXML
    private TextField oneTimeCodeInput;

    /** variables to hold inputs */
    String username;
    String password;
    
    String role1, role2, role3;
    
    
    boolean passSame = false;
    
    /** checkPasswords functions checks that both passwords inputted are the same. */
    @FXML
    void checkPasswords(KeyEvent event) {
    	String pass1 = passwordInput1.getText();
    	String pass2 = passwordInput2.getText();
    	
    	if(pass2.equals(pass1)) {
    		passNotSameLabel.setVisible(false);
    		
    		username = usernameInput.getText();
    		password = pass1;    
    		
    		passSame = true;
    	}
    	else {
    		passNotSameLabel.setVisible(true);
    		
    		passSame = false;
    	}
    }
    
    /** This function registers a user into the database and also switches to the correct screen 
     * @throws Exception */
    @FXML
    public void register(ActionEvent event) throws Exception{
    	if(passSame) {
    		//one time code
    		String OTC = oneTimeCodeInput.getText();
    		
    		// string containing roles of user
    		String roles = HelpSystem.userDatabaseHelper.checkForCode(OTC);
    		
    		// check auth
        	if(roles.equals("admin")) {
        		HelpSystem.setupAdministrator(username, password);
        		
        		HelpSystem.userDatabaseHelper.deleteCode(OTC);
        	} else if(roles.equals("student")) {
        		HelpSystem.setupStudent(username, password);
        		
        		HelpSystem.userDatabaseHelper.deleteCode(OTC);
        	} else if(roles.equals("instructional")) {
        		HelpSystem.setupInstructor(username, password);
        		
        		HelpSystem.userDatabaseHelper.deleteCode(OTC);
        	} else if(roles.equals("admininstructional") || roles.equals("adminstudent")
        			  || roles.equals("instructionalstudent") || roles.equals("admininstructionalstudent")) {
        		HelpSystem.setUpCustom(username, password, roles);
        		
        		HelpSystem.userDatabaseHelper.deleteCode(OTC);
        	}
        	else {
        		System.out.println("Code not valid!");
        		
        		return;
        	}
        	
        	// creates a new user who is logged in 
        	User loggedInUser = new User(username, password);
    		HelpSystem.logInUser(loggedInUser);
    		
    		// stores auth as local variable
    		String auth = HelpSystem.userDatabaseHelper.checkAuth(username);
    		
    		// checks if account is already updated, if not, redirect to finish account screen
    		if(HelpSystem.userDatabaseHelper.isUpdated(loggedInUser.username)) {
    			System.out.println("User already updated!");
    			
    			//display correct screen for specific user
        		if(auth.equals("admin")) {
        			// updates screen
            		Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
            		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            		theScene = new Scene(theRoot);
            		theStage.setScene(theScene);
            		theStage.show();
        		} else if(auth.equals("student")) {
        			// updates screen
            		Parent theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
            		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            		theScene = new Scene(theRoot);
            		theStage.setScene(theScene);
            		theStage.show();
        		} else if(auth.equals("instructor")){ 
        			// updates screen
            		Parent theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
            		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            		theScene = new Scene(theRoot);
            		theStage.setScene(theScene);
            		theStage.show();
        		} else {
        			Parent theRoot = FXMLLoader.load(getClass().getResource("LogInAsRoleWindow.fxml"));
            		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            		theScene = new Scene(theRoot);
            		theStage.setScene(theScene);
            		theStage.show();
        		}
    		} else {
    			// updates screen to finish setting up account
        		Parent theRoot = FXMLLoader.load(getClass().getResource("FinishSettingUpAccountWindow.fxml"));
        		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        		theScene = new Scene(theRoot);
        		theStage.setScene(theScene);
        		theStage.show();
        		
    		}
    		
    		
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
