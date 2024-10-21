package HelpSystem;

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
import javafx.stage.Stage;

public class EnterNewPasswordWindowController {
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
	private Button reset;
	
	@FXML
	private PasswordField newPassword1;
	
	@FXML
	private PasswordField newPassword2;
	
	@FXML
    private Label passIncorrectLabel;
	
	char[] password;
	
	boolean passSame = false; 
	
	@FXML
    void checkPasswords(KeyEvent event) {
    	String pass1 = newPassword1.getText();
    	String pass2 = newPassword2.getText();
    	
    	if(pass2.equals(pass1)) {
    		passIncorrectLabel.setVisible(false);
    		
    		password = pass1.toCharArray();    
    		
    		passSame = true;
    	}
    	else {
    		passIncorrectLabel.setVisible(true);
    		
    		passSame = false;
    	}
    }
	
	// this funtion resets a users password
	public void reset(ActionEvent e) throws Exception {
		if(passSame) {
			String username = HelpSystem.userManager.getUsername(); 
			
			HelpSystem.userDatabaseHelper.changePassword(username, newPassword1.getText());
			
			// switch to main menu
			Parent theRoot = FXMLLoader.load(getClass().getResource("HelpSystemLogInWindow.fxml"));
			theStage = (Stage)((Node)e.getSource()).getScene().getWindow();
			theScene = new Scene(theRoot);
			theStage.setScene(theScene);
			theStage.show();
		}
	}
}
