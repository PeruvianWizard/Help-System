package HelpSystem;

import java.io.IOException;
import java.sql.SQLException;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LogInAsRoleWindowController {
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	
    @FXML
    private ImageView explosion;

    @FXML
    private Label explosionLabel;

    @FXML
    private Label warningLabel;
	
    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
    PauseTransition pause2 = new PauseTransition(Duration.seconds(1.5));
    PauseTransition pause3 = new PauseTransition(Duration.seconds(1.5));
    
	@FXML
    void SwitchToAdminWindow(ActionEvent event) throws IOException, SQLException {
		if(HelpSystem.userDatabaseHelper.checkAuth(HelpSystem.getUsername()).contains("admin")) {
			HelpSystem.setSessionRole("admin");
			
			theRoot = FXMLLoader.load(getClass().getResource("AdminWindow.fxml"));
			setStage(theRoot, event);
		}
		else {
			warningLabel.setVisible(false);
			explosionLabel.setVisible(true);
			explosion.setVisible(true);
			
			pause.setOnFinished(e-> explosion.setVisible(false));
			pause2.setOnFinished(e-> explosionLabel.setVisible(false));
			pause3.setOnFinished(e-> warningLabel.setVisible(true));
			
			pause.play();
			pause2.play();
			pause3.play();
			
		}
    }

    @FXML
    void SwitchToInstructionalWindow(ActionEvent event) throws IOException, SQLException {
    	if(HelpSystem.userDatabaseHelper.checkAuth(HelpSystem.getUsername()).contains("instructor")) {
    		HelpSystem.setSessionRole("instructor");
    		
	    	theRoot = FXMLLoader.load(getClass().getResource("InstructionalWindow.fxml"));
			setStage(theRoot, event);
    	}
    	else {
    		warningLabel.setVisible(false);
			explosionLabel.setVisible(true);
			explosion.setVisible(true);
			
			pause.setOnFinished(e-> explosion.setVisible(false));
			pause2.setOnFinished(e-> explosionLabel.setVisible(false));
			pause3.setOnFinished(e-> warningLabel.setVisible(true));
			
			pause.play();
			pause2.play();
			pause3.play();
    	}
    }

    @FXML
    void SwitchToStudentWindow(ActionEvent event) throws IOException, SQLException {
    	if(HelpSystem.userDatabaseHelper.checkAuth(HelpSystem.getUsername()).contains("student")) {
    		HelpSystem.setSessionRole("student");
    		
	    	theRoot = FXMLLoader.load(getClass().getResource("StudentWindow.fxml"));
			setStage(theRoot, event);
    	}
    	else {
    		warningLabel.setVisible(false);
			explosionLabel.setVisible(true);
			explosion.setVisible(true);
			
			pause.setOnFinished(e-> explosion.setVisible(false));
			pause2.setOnFinished(e-> explosionLabel.setVisible(false));
			pause3.setOnFinished(e-> warningLabel.setVisible(true));
			
			pause.play();
			pause2.play();
			pause3.play();
    	}
    }
    
    public void setStage(Parent theRoot, ActionEvent event) {
    	theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
