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

public class LogInWindowController {
	
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	/**
	 * The variables logInButton, passwordInput1, passwordInput2, and usernameInput 
	 * come from the LogInWindow
	 */
    @FXML
    private Button logInButton;

    @FXML
    private Label passNotSameLabel;
    
    @FXML
    private PasswordField passwordInput1;

    @FXML
    private PasswordField passwordInput2;

    @FXML
    private TextField usernameInput;

    //variables to hold inputs
    String username;
    char[] password;
    
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
    
    @FXML
    void setUser(MouseEvent event) {
    	username = usernameInput.getText();						//stores the input
    	password = passwordInput1.getText().toCharArray();		//This line gets the password input and converts it to a array of characters
    }
    
    public void switchToHelpSystemWindow(ActionEvent event) throws IOException {
		Parent theRoot = FXMLLoader.load(getClass().getResource("HelpSystemWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
	}
}
