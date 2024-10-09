package HelpSystem;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FirstLogInWindowController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
    @FXML
    private Label passNotSameLabel;
	
    @FXML
    private PasswordField passInput1;

    @FXML
    private PasswordField passInput2;

    @FXML
    private TextField usernameInput;

    String username;
    char[] password;
    
    // blocks log in button until pw is the same.
    boolean passSame = false;
    
    /** Updates passSame variable by checking if pw is the same on every keystroke */ 
    @FXML
    void checkPasswords(KeyEvent event) {
    	String p1 = passInput1.getText();
    	String p2 = passInput2.getText();
    	
    	if(p1.equals(p2)) {
    		passNotSameLabel.setVisible(false);
    		
    		passSame = true;
    	} else {
    		passNotSameLabel.setVisible(true);
    		
    		passSame = false;
    	}
    }
    
    /* Switches to main screen once the username and password are stored */
    @FXML
    public void updateAndSwitchToRegisterWindow(ActionEvent event) throws IOException, SQLException {
    	if(passSame == false) {
    		//display pw not same label
    		passNotSameLabel.setVisible(true);
    		
    		return; 
    	} else {
    		// update username and password variables
    		username = usernameInput.getText();						
        	password = passInput1.getText().toCharArray();
        	
        	// setup an admin in the database with correct credentials
        	HelpSystem.setupAdministrator(username, password);
    		
        	// update scene
    		Parent theRoot = FXMLLoader.load(getClass().getResource("HelpSystemLogInWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
    	}
	}

}
