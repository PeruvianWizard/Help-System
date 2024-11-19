package HelpSystem;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool.ManagedBlocker;

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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ListArticleGroupsController implements Initializable {
	@FXML
	private ListView<String> articleGroupsList;
	
	@FXML
	private Label myLabel;
	
	@FXML
	private Button backButton;
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	private List<String> articleGroupStrings;
	private List<Long> idList;
    
    @FXML
    public void SwitchToAdminWindow(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		setStage(theRoot, event);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			articleGroupStrings = HelpSystem.userDatabaseHelper.getArticleGroupNames();
			idList = HelpSystem.userDatabaseHelper.getArticleIds();
			
			articleGroupsList.setStyle("-fx-font-family: 'Courier New';");
			articleGroupsList.getItems().add("Display all articles");
			articleGroupsList.getItems().addAll(articleGroupStrings);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		articleGroupsList.setOnMouseClicked(event -> {
			String selectedGroup = articleGroupsList.getSelectionModel().getSelectedItem();
			
			// ran into bug with mouse events on my mac so i am converting to actionevent
			ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
			
			try {
				manageListing(selectedGroup, actionEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}
	
	public void manageListing(String selectedGroup, ActionEvent event) throws IOException {
		if(selectedGroup.equals("Display all articles")) {
			theRoot = FXMLLoader.load(getClass().getResource("ListArticlesScreen.fxml"));
			setStage(theRoot, event);
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ListSpecificGroupScreen.fxml"));
			ListSpecificGroupController controller = new ListSpecificGroupController();
			loader.setController(controller);
			
			theRoot = loader.load();
			controller.customInitialize(selectedGroup);
			
			theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			
			theStage.setScene(new Scene(theRoot));
			theStage.show();
		}
	}
	
	@FXML
	public void goBack(ActionEvent event) throws SQLException, IOException { 
		String username = HelpSystem.getUsername();
		String auth = HelpSystem.userDatabaseHelper.checkAuth(username);
		
		if(auth.equals("admin")) {
			// updates screen
    		theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		} else if(auth.equals("student")) {
			// updates screen
    		theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
		} else if(auth.equals("instructor")){ 
			// updates screen
    		theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
		}
		else {
			theRoot = FXMLLoader.load(getClass().getResource("LogInAsRoleWindow.fxml"));
		}
		
		setStage(theRoot, event);
	}
	
	public void setStage(Parent theRoot, ActionEvent event) {
    	theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
