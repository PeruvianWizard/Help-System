package HelpSystem;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyUserRoleWindowController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	ObservableList<String> roleList = FXCollections.observableArrayList("admin", "instructor", "student");
	
    @FXML
    private ChoiceBox roleBox;


    @FXML
    private Label roleUpdate;
    
    @FXML
    private TextField usernameInput;

    @FXML
    private void initialize() {
    	roleBox.setValue("admin");
    	roleBox.setItems(roleList);
    }
    
    public void UpdateAndSwitchToAdminWindow(ActionEvent event) throws Exception {
    	String chosenRole = roleBox.getValue().toString();
    	String username = usernameInput.getText();
    	
    	if(HelpSystem.userDatabaseHelper.modifyRole(username, chosenRole)) {
    		Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
    		System.out.println("User role was modified successfully!");
    	} else {
    		roleUpdate.setVisible(true);
    		System.out.println("Failure! User role could not be modified correctly!");
    	}
    	
    }
    
    public void SwitchToAdminWindow(ActionEvent event) throws IOException {
    	Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
