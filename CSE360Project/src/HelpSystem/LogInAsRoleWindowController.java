package HelpSystem;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogInAsRoleWindowController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	String username;
	
	
	@FXML
    void SwitchToAdminWindow(ActionEvent event) throws IOException {
		Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }

    @FXML
    void SwitchToInstructionalWindow(ActionEvent event) throws IOException {

    	Parent theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }

    @FXML
    void SwitchToStudentWindow(ActionEvent event) throws IOException {
    	Parent theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
