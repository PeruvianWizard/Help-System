package HelpSystem;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

public class RestoreArticlesController {
	/** These private variables are used to set up the window */
	private Stage theStage;
	private Scene theScene;
	private Parent theRoot;
	
	@FXML
    private Label backupWarning;

    @FXML
    private RadioButton completeRestore;

    @FXML
    private RadioButton mergeRestore;
    
    private boolean completeRestoreCheck = false;
    private boolean mergeRestoreCheck = false;

    @FXML
    void doCompleteRestore(ActionEvent event) {
    	if(completeRestore.isSelected()) {
    		mergeRestore.setSelected(false);
    		completeRestoreCheck = true;
    		mergeRestoreCheck = false;
    	}
    }

    @FXML
    void doMergeRestore(ActionEvent event) {
    	if(mergeRestore.isSelected()) {
    		completeRestore.setSelected(false);
    		mergeRestoreCheck = true;
    		completeRestoreCheck = false;
    	}
    }
    
    @FXML
    public void RestoreBackup(ActionEvent event) throws Exception {
    	if(completeRestoreCheck) {
    		if(HelpSystem.userDatabaseHelper.removeRestoreBackup()) {
    			backupWarning.setText("Backup restored successfully");
    		}
    		else {
    			backupWarning.setText("Backup could not be restored successfully");
    		}
    	}
    	if(mergeRestoreCheck) {
    		if(HelpSystem.userDatabaseHelper.mergeRestoreBackup()) {
    			backupWarning.setText("Backup restored successfully");
    		}
    		else {
    			backupWarning.setText("Backup could not be restored successfully");
    		}
    	}
    }

    
    
    @FXML
    public void SwitchBack(ActionEvent event) throws IOException {
    	theRoot = FXMLLoader.load(getClass().getResource("ArticlesManagementScreen.fxml"));
    	theStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		theScene = new Scene(theRoot);
		theStage.setScene(theScene);
		theStage.show();
    }
}
