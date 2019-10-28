package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertMaker {
	public AlertMaker(String message){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText("Look, an Error Dialog");
		alert.setContentText(""+message+"");
		alert.showAndWait();
	}
}
