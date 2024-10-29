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
    	Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
