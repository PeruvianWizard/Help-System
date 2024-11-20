package HelpSystem;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MessageScreenController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
    private ChoiceBox inquiryList;

	ObservableList<String> genericInquires = FXCollections.observableArrayList("How to View Articles", "How to Log Out", "How To Contact Help Team");
	
    @FXML
    private TextArea textField;
    
    @FXML
    private void initialize() {
    	inquiryList.setValue("How to View Articles");
    	inquiryList.setItems(genericInquires);
    }
    
    @FXML
    void SendGenericMessage(ActionEvent event) throws SQLException {
    	String genericMessage = inquiryList.getValue().toString();
    	
    	HelpSystem.userDatabaseHelper.sendMessage(genericMessage, HelpSystem.getUsername(), HelpSystem.getUserId());
    	System.out.println("Help message sent successfully.");
    }

    @FXML
    void SendSpecificMessage(ActionEvent event) throws SQLException {
    	String specificMessage = textField.getText();
    	
    	HelpSystem.userDatabaseHelper.sendMessage(specificMessage, HelpSystem.getUsername(), HelpSystem.getUserId());
    	System.out.println("Help message sent successfully.");
    }
    
    @FXML
	public void goBack(ActionEvent event) throws SQLException, IOException { 
		String username = HelpSystem.getUsername();
		String auth = HelpSystem.userDatabaseHelper.checkAuth(username);
		
		if(auth.equals("admin")) {
			// updates screen
    		theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		} else if(auth.equals("student")) {
			// updates screen
    		theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
		} else if(auth.equals("instructor")){ 
			// updates screen
    		theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
		}
		else {
			theRoot = FXMLLoader.load(getClass().getResource("LogInAsRoleWindow.fxml"));
		}
		
		setStage(theRoot, event);
	}
    
    public void setStage(Parent theRoot, ActionEvent event) {
    	theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }

}
