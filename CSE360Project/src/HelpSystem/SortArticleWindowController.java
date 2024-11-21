package HelpSystem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SortArticleWindowController {

	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	 @FXML
	 private ChoiceBox levelBox;
	 
	 private String group = "";
	 
	 ObservableList<String> levelList = FXCollections.observableArrayList("Beginner", "Intermediate", "Advanced", "Expert");
	 
	 private List<String> articles;
	 
	 @FXML
	 private TextField keywordField;
	 
	 @FXML
	 private void initialize() {
		 levelBox.setValue("Beginner");
		 levelBox.setItems(levelList);
	 }
	 
	 public void setGroup (String group) {
		 this.group = group;
	 }
	 
	 @FXML
	 void SortAndSwitchBack(ActionEvent event) throws IOException, SQLException {
		 int levelChosen = findLevel(levelBox.getValue().toString());
		 
		 if(group.equals("") == true) {
			 articles = HelpSystem.userDatabaseHelper.getArticlesFromGroup(group, levelChosen);
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("ListArticlesScreen.fxml"));
			 Parent root = loader.load();
			 ListArticlesController tempController = loader.getController();
			 tempController.setListView(articles);
			 setStage(root, event);
		 }
			
		 else { 
			 articles = HelpSystem.userDatabaseHelper.getArticlesFromGroup(group, levelChosen);
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("ListSpecificGroupScreen.fxml"));
			 ListSpecificGroupController controller = new ListSpecificGroupController();
			 loader.setController(controller);
			
			 theRoot = loader.load();
			 controller.customInitialize(group);
			 controller.setListView(articles);
			 setStage(theRoot, event);
		 }	 
		 
	 }
	 
	 @FXML
	 void KeywordSearch(ActionEvent event) throws SQLException, IOException {
		 String keyword = keywordField.getText();
		 
		 if(group.equals("") == true) {
			 articles = HelpSystem.userDatabaseHelper.searchArticleWKeyword(keyword, group);
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("ListArticlesScreen.fxml"));
			 Parent root = loader.load();
			 ListArticlesController tempController = loader.getController();
			 tempController.setListView(articles);
			 tempController.changeTitleLabel("Matched articles: " + articles.size());
			 setStage(root, event);
		 }
		 else {
			 articles = HelpSystem.userDatabaseHelper.searchArticleWKeyword(keyword, group);
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("ListSpecificGroupScreen.fxml"));
			 ListSpecificGroupController controller = new ListSpecificGroupController();
			 loader.setController(controller);
			
			 theRoot = loader.load();
			 controller.customInitialize(group);
			 controller.setListView(articles);
			 controller.changeTitleLabel("Matched articles in " + group + " group: " + articles.size());
			 setStage(theRoot, event);
		 }
	 }
	 
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
	 
	 public void setStage(Parent theRoot, ActionEvent event) {
	    	theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			theScene = new Scene(theRoot);
			theStage.setTitle("Sorted Results");
			theStage.setScene(theScene);
			theStage.show();
	    }
}
