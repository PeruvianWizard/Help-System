package HelpSystem;

import java.io.IOException;
import java.net.URL;
import java.rmi.server.LoaderHandler;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.media.VideoTrack;
import javafx.stage.Stage;

public class ViewMessagesWindowController implements Initializable {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
    private ListView<String> MessagesList;

    @FXML
    private Button backButton;
    
    private List<String> messages;
    private List<Integer> idList;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	try {
    		messages = HelpSystem.userDatabaseHelper.getMessages();
    		idList = HelpSystem.userDatabaseHelper.getMessagesIds();
    		
    		MessagesList.setStyle("-fx-font-family: 'Courier New';");
    		MessagesList.getItems().addAll(messages);
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	MessagesList.setOnMouseClicked(event -> {
    		try {
    		int messageId = idList.get(MessagesList.getSelectionModel().getSelectedIndex());
    		String usernamesMessage = HelpSystem.userDatabaseHelper.getSingleUsernameMessage(messageId);
    		String messageBody = HelpSystem.userDatabaseHelper.getSingleMessage(messageId);
    		openMessage(usernamesMessage, messageBody);
    		
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	});
    }
    
    private void openMessage(String username, String messageBody) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayMessageWindow.fxml"));
    	Parent root = loader.load();
    	
    	DisplayMessageWindowController messageWindowController = loader.getController();
    	messageWindowController.setData(username, messageBody);
    	
    	Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    	
    	
    }

    @FXML
    void goBack(ActionEvent event) throws SQLException, IOException {
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
