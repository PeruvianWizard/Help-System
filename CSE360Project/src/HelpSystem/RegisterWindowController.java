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

    /** variables to hold inputs */
    String username;
    char[] password;
    
    /** checkPasswords functions checks that both passwords inputted are the same. */
    @FXML
    void checkPasswords(KeyEvent event) {
    	String pass1 = passwordInput1.getText();
    	String pass2 = passwordInput2.getText();
    	
    	if(pass2.equals(pass1)) {
    		passNotSameLabel.setVisible(false);
    	}
    	else {
    		passNotSameLabel.setVisible(true);
    	}
    }
    
    /**setUser function activates when the log in button is pressed, it should get the values in the text boxes
     * and store them in variables "username" and "password". We will use these variables to creates users for the system(how? no idea yet)
     */
    @FXML
    void setUser(MouseEvent event) {
    	username = usernameInput.getText();						//stores the input
    	password = passwordInput1.getText().toCharArray();		//This line gets the password input and converts it to a array of characters
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
