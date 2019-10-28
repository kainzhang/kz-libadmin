package user;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXButton;



public class UserController {

    @FXML
    private TextField addUsernameField, passwordField,oldpositionField, positionField, usernameField, newpasswordField;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> usernameCol, passwordCol, positionCol;
    @FXML
    private JFXButton exportPDF;

    private Executor exec;

    @FXML
    private void initialize () throws ClassNotFoundException, SQLException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        passwordCol.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        positionCol.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        fillUserTable();
        usernameField.setEditable(false);
    }
	
	@FXML
    private void exportAsPDF(ActionEvent actionEvent) throws ClassNotFoundException, SQLException{
    	StackPane root = new StackPane();
    	ObservableList<User> userlist = UserDAO.backup;
    	TableView<User> table = new TableView<User>();
		
    	TableColumn<User,String> usernameCol = new TableColumn<User,String>("USERNAME");
    	usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
    	TableColumn<User,String> passwordCol = new TableColumn<User,String>("PASSWORD");
        passwordCol.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        TableColumn<User,String> positionCol = new TableColumn<User,String>("POSITION");
        positionCol.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
    	
        table.getColumns().add(usernameCol);
        table.getColumns().add(passwordCol);
        table.getColumns().add(positionCol);
        
    	table.setColumnResizePolicy((TableView.CONSTRAINED_RESIZE_POLICY));
    	table.setFocusTraversable(true);
    	table.setEditable(false);
    	table.setMinHeight(5000);
    	root.getChildren().add(table);
    	root.setMinWidth(487);
    	root.setMaxWidth(487);
    	root.getStylesheets().add(getClass().getResource("../application/PDF.css").toExternalForm());
    	
    	Stage stage = new Stage();
    	PrinterJob job = PrinterJob.createPrinterJob();
    	boolean ready = job.showPrintDialog(stage.getOwner());
    	if(ready){
	    	for(int i=0;i<userlist.size();i+=23)
	    	{
	    		int end = (i+23<userlist.size()) ? i+23 : userlist.size();
	    		table.setItems(FXCollections.observableList(userlist.subList(i, end)));
	    		job.printPage(root);
	    	}
	    	job.endJob();
    	}
    }
	

    @FXML
    private void showUsers(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<User> userList = UserDAO.showUsers();
            populateUsers(userList);
        } catch (SQLException e){
            System.out.println("Error occurred while getting users information from DB" + e);
            throw e;
        }
    }

    private void fillUserTable() throws SQLException, ClassNotFoundException {
        Task<List<User>> task = new Task<List<User>>(){
            @Override
            public ObservableList<User> call() throws Exception{
                return UserDAO.showUsers();
            }
        };
        task.setOnFailed(e-> task.getException().printStackTrace());
        task.setOnSucceeded(e-> tableView.setItems((ObservableList<User> ) task.getValue()));
        exec.execute(task);
    }

    @FXML
    private void populateUsers (ObservableList<User> usersData) throws ClassNotFoundException {
        if (!usersData.isEmpty()) {
        	tableView.setItems(usersData);
        } else {
            System.out.println("No result!");
        }
    }

    @FXML
    private void updateUser (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            UserDAO.updateUser(usernameField.getText(),newpasswordField.getText(),oldpositionField.getText());
            System.out.println("updated for user: " + usernameField.getText());
        } catch (SQLException e) {
            System.out.println("Problem occurred while updating: " + e);
            throw e;
        }
    }
    
    @FXML
    private void searchUser (ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
        	ObservableList<User> usersResult = UserDAO.searchUser(usernameField.getText());
        	populateUsers(usersResult);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void insertUser (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    	String username,password,position;
    	username=addUsernameField.getText();
    	password=passwordField.getText();
    	position=positionField.getText();
    	if(username.equals("")||password.equals("")||position.equals(""))
    	{
    		System.out.println("Wrong Input!");
    	} else {
	        try {
	            UserDAO.insertUser(username,password,Integer.parseInt(position));
	            System.out.println("user inserted!");
	        } catch (SQLException e) {
	            System.out.println("Problem occurred while inserting user " + e);
	            throw e;
	        }
    	}
    }

    @FXML
    private void deleteUser (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            UserDAO.deleteUserWithUsername(usernameField.getText());
            System.out.println("User deleted! username: " + usernameField.getText());
        } catch (SQLException e) {
            System.out.println("Problem occurred while deleting user " + e);
            throw e;
        }
    }
    
    @FXML
    private void fillTheBlanks(MouseEvent event) {
    	try{
	    	ReadOnlyObjectProperty<User> selectedUser = tableView.getSelectionModel().selectedItemProperty();
	    	usernameField.setText(selectedUser.getValue().getUsername());
	    	newpasswordField.setText(selectedUser.getValue().getPassword());
	    	String position = selectedUser.getValue().getPosition();
	    	int positionLevel = 0;
	    	if(position.equals("Superuser"))positionLevel = 2;
	    	else if (position.equals("Admin"))positionLevel = 1;
	    	oldpositionField.setText(""+positionLevel);
    	} catch(Exception e) {
    		System.out.println("Hi bro");
    	}
    }
    
    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }
}