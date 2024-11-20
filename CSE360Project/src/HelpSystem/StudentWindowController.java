package HelpSystem;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentWindowController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
    void SwitchToMessageScreen(ActionEvent event) throws IOException {
		theRoot = FXMLLoader.load(getClass().getResource("MessageScreen.fxml"));
		setStage(theRoot, event);
    }
	
    @FXML
    void switchToHelpSystemLogInWindow(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("HelpSystemLogInWindow.fxml"));
		setStage(theRoot, event);
    }
    
    @FXML
    public void SwitchToViewArticlesWindow(ActionEvent event) throws IOException{   	
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
