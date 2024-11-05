package HelpSystem;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeleteArticleController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;

    @FXML
    private TextField articleName;

    @FXML
    private TextField uniqueIdentifier;
    
    public void DeleteArticle(ActionEvent event) throws NumberFormatException, Exception {
    	HelpSystem.userDatabaseHelper.deleteArticle(articleName.getText(), Long.parseLong(uniqueIdentifier.getText()));
    }

    public void SwitchBackToPreviousScreen(ActionEvent event) throws IOException {
    	if(HelpSystem.getSessionRole().equals("admin")) {
    		theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
    	}
    	else if(HelpSystem.getSessionRole().equals("instructor")) {
    		theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
    	}
    	else {
    		theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
    	}
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
