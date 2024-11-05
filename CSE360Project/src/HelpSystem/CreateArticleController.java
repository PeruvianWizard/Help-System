package HelpSystem;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CreateArticleController implements Initializable {
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
	private TextField titleField; 
	
	@FXML
	private TextField descriptionField;
	
	@FXML
	private TextArea bodyField;
	
	@FXML
	private TextField groupField;
	
	@FXML
	private ComboBox<String> levelSelect;
	
	@FXML
	private CheckBox isPrivateCheck;
	
	@FXML
	private Button backButton;
	
	@FXML
	private Button createButton; 

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		levelSelect.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
	}
	
	@FXML
	void createArticle(ActionEvent event) throws SQLException {
		String title = titleField.getText();
		String description = descriptionField.getText();
		String body = bodyField.getText();
		String group = groupField.getText();
		
		int level = findLevel(levelSelect.getSelectionModel().getSelectedItem());
		boolean isPrivate = isPrivateCheck.isSelected();
		
		HelpArticle article = new HelpArticle(isPrivate, level, title, description, body, group);
		
		HelpSystem.userDatabaseHelper.addArticle(article);
	}
	
	@FXML
	void returnToHome(ActionEvent event) throws IOException {
		// updates screen
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
	
	//converts the string level to int level because I can't spell
	private int findLevel(String selection) {
		if(selection.equals("Beginner")) {
			return 0;
		} else if(selection.equals("Intermediate")) {
			return 1;
		} else if(selection.equals("Advanced")) {
			return 2;
		} else {
			return 3;
		}
	}

}
