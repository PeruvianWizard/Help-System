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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyAccessScreenController {
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
	private Label titleLabel;
	
	@FXML
	private Label successLabel;
	
	@FXML
	private TextField userTextInput;
	
	@FXML
	private Button updateButton;
	
	@FXML
	private Button backButton;
	
	private String groupNameString;
	
	public void customInitialize(String groupName) {
		titleLabel.setText("Modify Access for: " + groupName);
		
		this.groupNameString = groupName; 
	}
	
	@FXML
	public void updateUsers() throws SQLException {
		String userToAdd = userTextInput.getText();
		
		int userId = HelpSystem.userDatabaseHelper.getUserId(userToAdd);
		
		HelpSystem.userDatabaseHelper.addUserToGroup(groupNameString, userId);
		
		successLabel.setVisible(true);
	}
	
	@FXML
	public void returnToGroupScreen(ActionEvent event) throws IOException {
		// updates screen
    	theRoot = FXMLLoader.load(getClass().getResource("ListArticleGroupScreen.fxml"));
		
		setStage(theRoot, event);
	}
	
	public void setStage(Parent theRoot, ActionEvent event) {
    	theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
