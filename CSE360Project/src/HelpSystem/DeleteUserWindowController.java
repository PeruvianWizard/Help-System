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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeleteUserWindowController {
    
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
    private TextField usernameInput;

    String username;
    
    @FXML
    public void DeleteAndSwitchToAdminWindow(ActionEvent event) throws SQLException, IOException {
    	username = usernameInput.getText();
    	
    	if(HelpSystem.userDatabaseHelper.UserExists(username)) {
    		HelpSystem.userDatabaseHelper.deleteUser(username);
    		System.out.println("User Deleted Successfully!");
    		
    		Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
    	}
    	else {
    		System.out.println("Failure! User Was Not Deleted!");
    	}	
    }
    
    @FXML
    public void SwitchToAdminWindow(ActionEvent event) throws IOException {
    	Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
