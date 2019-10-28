package start;

import java.sql.SQLException;
import application.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import user.User;
import user.UserDAO;

public class StartController {
    @FXML
    private Text usernameHint_1,passwordHint_1,usernameHint_2,passwordHint_2,passagainHint;
    @FXML
    private TextField usernameField_1,usernameField_2;
    @FXML
    private PasswordField passwordField_1,passwordField_2,passagainField;
    @FXML
    private Pane signUpPane, signInPane;

    @FXML
    public void Login(ActionEvent event) throws Exception {
    	usernameHint_1.setText("");
    	passwordHint_1.setText("");
		String inputuser=usernameField_1.getText();
		String inputpass=passwordField_1.getText();
		if(inputuser.length()<6||inputuser.length()>12) {
			usernameHint_1.setText("Wrong format!");
			usernameField_1.setText("");
		} else {
			if(UserDAO.userExist(inputuser)){
				User user = UserDAO.passwordCorrect(inputuser, inputpass);
				if(user!=null){
					usernameField_1.setText("");
		       		passwordField_1.setText("");
		       		usernameHint_1.setText("");
		        	passwordHint_1.setText("");
    				Launcher.setUser(user);
		       		System.out.println("  - - [ LOGGED IN! ] - - "+"\n");
		       		Launcher.toPage("MenuView");
				} else {
					passwordHint_1.setText("Wrong Password!");
				}
			} else {
				usernameHint_1.setText("User doesn't exist!");
		    	usernameField_1.setText("");
			}
		}
			
    }
    
    @FXML
    protected void Rigister(ActionEvent event) throws SQLException {
    	usernameHint_2.setText("");
    	passwordHint_2.setText("");
    	passagainHint.setText("");
		String inputuser=usernameField_2.getText();
		String inputpass=passwordField_2.getText();
		String inputtass=passagainField.getText();
		boolean exist=false,ok=true;
		if(inputuser.length()<6||inputuser.length()>16) {
			usernameHint_2.setText("At least 6-16 letters!");
		} 
		else if(UserDAO.userExist(inputuser)){
			System.out.println("User already exists!");
			usernameField_2.setText("");
			usernameHint_2.setText("User already exists!");
			exist=true;
		}
		if(inputtass.length()<8||inputtass.length()>16){
			passwordHint_2.setText("At least 8-16 letters");
			passwordField_2.setText("");
			passagainField.setText("");
		}else if(inputtass.equals(inputpass)==false){
			System.out.println("Confirmed password doesn't match");
			passagainField.setText("");
			passagainHint.setText("Doesn't match!");
		}else if(inputtass.equals(inputpass)&&!exist) {
		    try {
				UserDAO.insertUser(inputuser,inputpass,0);
			} catch (ClassNotFoundException e) {
				ok=false;
				System.out.println("Rigister failed");
				e.printStackTrace();
			} finally {
				if(ok){
			    	usernameField_2.setText("");
					passwordField_2.setText("");
					passagainField.setText("");
					usernameHint_2.setText("");
					passwordHint_2.setText("");
					passagainHint.setText("");
		    	}
			}
			System.out.println("insert:"+inputuser+" "+inputpass+" "+inputtass);
		}
	
		}
    
    @FXML
    protected void ToSignUp(ActionEvent event) throws Exception {
    	signInPane.setVisible(false);
    	signUpPane.setVisible(true);
    }
    
    @FXML
    protected void ToSignIn(ActionEvent event) throws Exception {
    	signInPane.setVisible(true);
    	signUpPane.setVisible(false);
    }
}