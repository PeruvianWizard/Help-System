package HelpSystem;

import java.io.IOException;
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
    
    @FXML
    void SetUser(MouseEvent event) {
    	if(passSame == false) { 
    		// throw some error
    		
    		return;
    	} else {
    		username = usernameInput.getText();						
        	password = passInput1.getText().toCharArray();
    	}
    }
    
    @FXML
    public void updateAndSwitchToRegisterWindow(ActionEvent event) throws IOException {
    	if(passSame == false) {
    		passNotSameLabel.setVisible(true);
    		
    		return; 
    	} else {
    		Parent theRoot = FXMLLoader.load(getClass().getResource("HelpSystemLogInWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
    	}
	}

}
