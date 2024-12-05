package HelpSystem;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditArticlesScreenController {
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
    private TextArea bodyField;

    @FXML
    private TextField inputField;

    @FXML
    private Label titleLabel;

    long uniqueIdentifier;
    
    
    @FXML
    void getArticle(ActionEvent event) throws Exception {
    	uniqueIdentifier = Long.parseLong(inputField.getText());
    	titleLabel.setText(HelpSystem.userDatabaseHelper.getTitle(uniqueIdentifier));
    	titleLabel.setVisible(true);
    	bodyField.setText(HelpSystem.userDatabaseHelper.getBody(uniqueIdentifier));
    }

    @FXML
    void updateArticle(ActionEvent event) throws Exception {
    	HelpSystem.userDatabaseHelper.updateArticle(bodyField.getText(), HelpSystem.userDatabaseHelper.getGroup(uniqueIdentifier), uniqueIdentifier);
    }
    
    @FXML
    void goBack(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("ArticlesManagementScreen.fxml"));
    	theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
