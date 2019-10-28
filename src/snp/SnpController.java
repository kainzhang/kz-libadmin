package snp;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXRadioButton;

public class SnpController {
	
    @FXML
    private TextField idField, keywordField;
    @FXML
    private TableView<Snp> tableView;
    @FXML
    private TableColumn<Snp, Integer> idCol;
    @FXML
    private TableColumn<Snp, String> nameCol, ownerCol, otitleCol, telCol, addrCol, bankCol, bankidCol;
    @FXML
    private JFXRadioButton supplier,seller;
    @FXML
    private ToggleGroup searchGroup;
    @FXML
    private JFXDrawer drawer;
    
    private Executor exec;

    @FXML
    private void initialize () throws ClassNotFoundException, SQLException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        ownerCol.setCellValueFactory(cellData -> cellData.getValue().ownerProperty());
        otitleCol.setCellValueFactory(cellData -> cellData.getValue().otitleProperty());
        telCol.setCellValueFactory(cellData -> cellData.getValue().telProperty());
        addrCol.setCellValueFactory(cellData -> cellData.getValue().addrProperty());
        bankCol.setCellValueFactory(cellData -> cellData.getValue().bankProperty());
        bankidCol.setCellValueFactory(cellData -> cellData.getValue().bankidProperty());
        autoFill(1);
        supplier.setUserData(1);
        seller.setUserData(2);
    }

    @FXML
    private void showSnps(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    	int sign = (int)searchGroup.getSelectedToggle().getUserData();
        try {
            ObservableList<Snp> snpList = SnpDAO.showSnps(sign);
            populateSnps(snpList);
        } catch (SQLException e){
            System.out.println("Error occurred while getting snps information from DB" + e);
            throw e;
        }
    }

    private void autoFill(int sign) throws SQLException, ClassNotFoundException {
        Task<List<Snp>> task = new Task<List<Snp>>(){
            @Override
            public ObservableList<Snp> call() throws Exception{
                return SnpDAO.showSnps(sign);  // sign=1 销售商   sign=2 供货商
            }
        };
        task.setOnFailed(e-> task.getException().printStackTrace());
        task.setOnSucceeded(e-> tableView.setItems((ObservableList<Snp> ) task.getValue()));
        exec.execute(task);
    }
    
//    @FXML
//    private void insertSnp (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
//    	int sign =(int)(searchGroup.getSelectedToggle().getUserData());
//    	
//    	
//    	Snp snp = new Snp();
//    	snp.setName(nameField);
//    	order.setIsbn(isbnField.getText());
//    	order.setQuantity(quantity);
//    	order.setUnitprice(unitprice);
//    	order.setAmount(quantity*unitprice);
//    	order.setSnpid(Integer.parseInt(snpidField.getText()));
//    	order.setSign(sign);
//    	
//    	int flag=1;
//        try {
//            SnpDAO.insertSnp(snp);
//        } catch (SQLException e) {
//            System.out.println("Problem occurred while inserting snp " + e);
//            throw e;
//        }
//    }
    
    @FXML
    private void populateSnps (ObservableList<Snp> snpsData) throws ClassNotFoundException {
        if (snpsData.isEmpty()){
        	System.out.println("No result!");
        }
        	tableView.setItems(snpsData);
//        } else {
//            System.out.println("No result!");
//        }
    }
    
    @FXML
    private void searchSnpByKeyword (ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
    	int sign = (int)searchGroup.getSelectedToggle().getUserData();
    	try {
        	ObservableList<Snp> snpResult = SnpDAO.searchSnpsByKeyword(keywordField.getText(),sign);
        	populateSnps(snpResult);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void deleteSnpById (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            SnpDAO.deleteSnpById(Integer.parseInt(idField.getText()));
        } catch (SQLException e) {
            System.out.println("Problem occurred while deleting user " + e);
            throw e;
        }
        System.out.println("book deleted! id: "+ idField.getText());
    }
    
    @FXML
    private void insertSnp(ActionEvent actionEvent) throws IOException {
    	SnpDAO.clearSnp();
    	SnpDAO.actionMark=1;
    	AnchorPane ap= FXMLLoader.load(getClass().getResource("../view/SnpDrawer.fxml"));
    	drawer.setSidePane(ap);
    	drawer.toggle();
    }
    
    @FXML
    protected void editSnp(ActionEvent actionEvent) throws IOException {
    	ReadOnlyObjectProperty<Snp> selectedSnp = tableView.getSelectionModel().selectedItemProperty();
    	if(selectedSnp.getValue()==null)System.out.println("Please select a firm!");
    	else {
	    	try{
		    	SnpDAO.setSnp(selectedSnp);
		    	SnpDAO.actionMark=2;
	    	} catch(Exception e) {
	    		System.out.println("Hi bro");
    	}
    	AnchorPane ap= FXMLLoader.load(getClass().getResource("../view/SnpDrawer.fxml"));
    	drawer.setSidePane(ap);
    	drawer.toggle();
    	}
    }
    
}