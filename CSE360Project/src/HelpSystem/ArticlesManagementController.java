package HelpSystem;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ArticlesManagementController {
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
    @FXML
    private Label backupWarning;
	
    public void SwitchToDeleteArticleScreen(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("DeleteArticleScreen.fxml"));
		setStage(theRoot, event);
    }

    public void SwitchToAddArticleScreen(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("CreateArticleScreen.fxml"));
		setStage(theRoot, event);
    }
    
    public void BackupArticles(ActionEvent event) throws Exception {
    	boolean success = HelpSystem.userDatabaseHelper.backupArticles();
    	
    	if(success) {
    		backupWarning.setText("Backup created successfully");
    	} 
    	else {
    		backupWarning.setText("Error: Backup could not be created");;
    	}
    }
    
    public void SwitchToRestoreScreen(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("RestoreArticles.fxml"));
    	setStage(theRoot, event);
    }
    
    public void SwitchToPreviousScreen(ActionEvent event) throws IOException {
    	String sessionRoleString = HelpSystem.getSessionRole();
    	
    	if(sessionRoleString.equals("admin")) {
    		theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
    	} else if(sessionRoleString.equals("instructor")) {
    		theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
    	} else {
    		theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
		}
    	
    	setStage(theRoot, event);	
    }
    
    public void SwitchToEditArticleScreen(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("EditArticlesScreen.fxml"));
    	setStage(theRoot, event);
    }
    
    public void setStage(Parent theRoot, ActionEvent event) {
    	theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }  
}
