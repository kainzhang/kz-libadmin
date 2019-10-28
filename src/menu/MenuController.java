package menu;

import java.sql.SQLException;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXButton;

import application.Launcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MenuController {
	@FXML
	AnchorPane contentPane;
	@FXML
	JFXButton Book,Order,Category,Snp,AlertInfo,User,Client;
	
    @FXML
    private void initialize () throws ClassNotFoundException, SQLException {
    	if(Launcher.getUser().getPosition().equals("Staff"))
    		User.setDisable(true);
    }
	
	
	@SuppressWarnings({ "static-access" })
	@FXML
    protected void toContent(ActionEvent event) throws Exception {
		clearBtnStyle(Book);
    	clearBtnStyle(Order);
    	clearBtnStyle(Category);
    	clearBtnStyle(Snp);
    	clearBtnStyle(AlertInfo);
    	clearBtnStyle(User);
		
		Object obj = event.getSource();
		String btnid = util.Tools.getFieldValueByName("id", obj);
		if(!btnid.equals("Client"))changeBtnStyle((JFXButton) event.getSource());
		contentPane.getChildren().removeAll(contentPane.getChildren());
    	AnchorPane ap= FXMLLoader.load(Launcher.class.getResource("../view/"+btnid+"View.fxml"));
    	ap.getStylesheets().add(getClass().getResource("../application/Launcher.css").toExternalForm());

    	ap.setBottomAnchor(ap, 0.0);
    	ap.setRightAnchor(ap, 0.0);
    	ap.setTopAnchor(ap, 0.0);
    	ap.setLeftAnchor(ap, 0.0);
    	contentPane.getChildren().add(ap);
    }
	
	public static void clearBtnStyle(JFXButton btn){
		String clearStyle ="-fx-text-fill: #bbb;";
		btn.setStyle(clearStyle);
	}
	
	public static void changeBtnStyle(JFXButton btn){
		String pressedStyle = "-fx-border-width: 0 0 0 4; "+
							  "-fx-border-color: rgb(245,108,45); "+
							  "-fx-background-color: rgb(75,82,89);"+
							  "-fx-text-fill: #fff; ";
		btn.setStyle(pressedStyle);
	}
	
    @FXML
    protected void toStart(ActionEvent event) throws Exception {
    	Launcher.toPage("StartView");
//    	Launcher.setUser(null);
    	System.out.println("  - - [ LOGGED OUT ] - - " + "\n");
    }
}
