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

public class ListUsersController implements Initializable {
	@FXML
	private ListView<String> userListView;
	
	@FXML
	private Label myLabel;
	
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
    
    @FXML
    public void SwitchToAdminWindow(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
		theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			List<String> users = HelpSystem.userDatabaseHelper.getUsers();
			userListView.getItems().addAll(users);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
