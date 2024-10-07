package HelpSystem;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FirstLogInWindowController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	

    @FXML
    private PasswordField passInput1;

    @FXML
    private PasswordField passInput2;

    @FXML
    private TextField usernameInput;

    @FXML
    void SetUser(MouseEvent event) {
    		
    }

}
