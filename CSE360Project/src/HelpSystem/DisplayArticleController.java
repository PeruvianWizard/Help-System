package HelpSystem;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class DisplayArticleController implements Initializable{
	@FXML
	private Label titleLabel;
	
	@FXML
	private TextArea bodyArea;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	} 
	
	public void setData(Long uniqueIdentifier) throws SQLException {
		titleLabel.setText(HelpSystem.userDatabaseHelper.getTitle(uniqueIdentifier));
		bodyArea.setText(HelpSystem.userDatabaseHelper.getBody(uniqueIdentifier));
	}
}
