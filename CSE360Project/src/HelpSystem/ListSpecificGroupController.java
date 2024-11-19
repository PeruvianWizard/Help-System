package HelpSystem;

import java.awt.Event;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ListSpecificGroupController implements Initializable {
	@FXML
	private ListView<String> articlesList;
	
	@FXML
	private Label groupNameTitle;
	
	@FXML
	private Button backButton;
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	private List<String> articleStrings;
	private List<Long> idList;
	
	private String groupName = "Error";
    
    @FXML
    public void SwitchToAdminWindow(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		setStage(theRoot, event);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void customInitialize(String newGroup) {
		this.groupName = newGroup;
		groupNameTitle.setText("Articles in " + newGroup + ":");
		
		try {
			articleStrings = HelpSystem.userDatabaseHelper.getArticlesFromGroup(this.groupName);
			idList = HelpSystem.userDatabaseHelper.getArticleIdsFromGroup(this.groupName);
			
			articlesList.setStyle("-fx-font-family: 'Courier New';");
			articlesList.getItems().addAll(articleStrings);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		articlesList.setOnMouseClicked(event -> {
			Long articleId = idList.get(articlesList.getSelectionModel().getSelectedIndex());
			openArticle(articleId);
		});
	}
	
	private void openArticle(Long uniqueIdentifier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayArticleWindow.fxml"));
            Parent root = loader.load();

            // Pass the article data and ranking to the details controller
            DisplayArticleController displayController = loader.getController();
            displayController.setData(uniqueIdentifier);

            // Create a new scene and display it in a new stage
            Stage stage = new Stage();
            stage.setTitle(HelpSystem.userDatabaseHelper.getTitle(uniqueIdentifier));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	public void goBack(ActionEvent event) throws SQLException, IOException { 
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
