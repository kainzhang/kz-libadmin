package application;


import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.User;

public class Launcher extends Application {
	static Stage stage;
	static Scene scene;
	static Parent root;
	private static User user;
	
    @Override
    public void start(Stage stage) throws Exception {
    	Launcher.stage=stage;
//        Parent root = FXMLLoader.load(getClass().getResource("../view/ChatClientView.fxml"));
    	Parent root = FXMLLoader.load(getClass().getResource("../view/StartView.fxml"));
        scene=new Scene(root,1280,720);
        root.getStylesheets().add(Launcher.class.getResource("Start.css").toExternalForm());
        stage.setTitle("LOKKA's BIS");
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        
    }
    
    public static void toPage(String page) throws Exception {
    	root = FXMLLoader.load(Launcher.class.getResource("../view/"+page+".fxml"));
	    scene=new Scene(root,scene.getWidth(),scene.getHeight());
	    if(page.equals("StartView"))scene.getStylesheets().add(Launcher.class.getResource("Start.css").toExternalForm());
	    else scene.getStylesheets().add(Launcher.class.getResource("Main.css").toExternalForm());
	    stage.setScene(scene);
    }

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		Launcher.user = user;
	}
}