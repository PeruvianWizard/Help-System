package HelpSystem;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DisplayMessageWindowController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
    private TextArea messageOutput;

    @FXML
    private Label usernameLabel;
    
    public void setData(String username, String messageBody) {
		usernameLabel.setText(username);
		messageOutput.setText(messageBody);
	}
}
