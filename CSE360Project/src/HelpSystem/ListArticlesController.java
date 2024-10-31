package HelpSystem;

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

public class ListArticlesController implements Initializable {
	@FXML
	private ListView<String> articlesList;
	
	@FXML
	private Label myLabel;
	
	@FXML
	private Button backButton;
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
    
    @FXML
    public void SwitchToAdminWindow(ActionEvent event) throws IOException {
    	Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			List<String> articleStrings = HelpSystem.userDatabaseHelper.getArticles();
			articlesList.getItems().addAll(articleStrings);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void goBack(ActionEvent event) throws SQLException, IOException { 
		String username = HelpSystem.getUsername();
		String auth = HelpSystem.userDatabaseHelper.checkAuth(username);
		
		if(auth.equals("admin")) {
			// updates screen
    		Parent theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
		} else if(auth.equals("student")) {
			// updates screen
    		Parent theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
		} else if(auth.equals("instructor")){ 
			// updates screen
    		Parent theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
		}
		else {
			Parent theRoot = FXMLLoader.load(getClass().getResource("LogInAsRoleWindow.fxml"));
    		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		theScene = new Scene(theRoot);
    		theStage.setScene(theScene);
    		theStage.show();
		}
	}
}
