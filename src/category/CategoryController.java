package category;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import application.Launcher;

public class CategoryController {

    @FXML
    private TextField cidField, cnameField, newCnameField;
    @FXML
    private TableView<Category> tableView;
    @FXML
    private TableColumn<Category, Integer> cidCol;
    @FXML
    private TableColumn<Category, String> cnameCol;
    
    private Executor exec;

    @FXML
    private void initialize () throws ClassNotFoundException, SQLException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        cidCol.setCellValueFactory(cellData -> cellData.getValue().cidProperty().asObject());
        cnameCol.setCellValueFactory(cellData -> cellData.getValue().cnameProperty());
        fillCategoryTable();
       
    }

    @FXML
    private void showCategories(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Category> categoryList = CategoryDAO.showCategories();
            populateCategories(categoryList);
        } catch (SQLException e){
            System.out.println("Error occurred while getting users information from DB" + e);
            throw e;
        }
    }

    private void fillCategoryTable() throws ClassNotFoundException {
        Task<List<Category>> task = new Task<List<Category>>(){
            @Override
            public ObservableList<Category> call() throws Exception{
                return CategoryDAO.showCategories();
            }
        };
        task.setOnFailed(e-> task.getException().printStackTrace());
        task.setOnSucceeded(e-> tableView.setItems((ObservableList<Category> ) task.getValue()));
        exec.execute(task);
    }

    @FXML
    private void populateCategories (ObservableList<Category> categoryData){
        if (!categoryData.isEmpty()) {
        	tableView.setItems(categoryData);
        } else {
            System.out.println("No result!");
        }
    }

    @FXML
    private void updateCname (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            CategoryDAO.updateCname(Integer.parseInt(cidField.getText()),cnameField.getText());
            System.out.println("Cname has been updated for category: " + cidField.getText());
        } catch (SQLException e) {
            System.out.println("Problem occurred while updating cname: " + e);
            throw e;
        }
    }
    
    @FXML
    private void searchCategory (ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
        	ObservableList<Category> categoryResult = CategoryDAO.searchCategory(cnameField.getText());
        	populateCategories(categoryResult);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void insertCategory (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    	String cname = newCnameField.getText();
    	if(cname.equals(""))
    	{
    		System.out.println("Wrong Input!");
    	} else {
	        try {
	            CategoryDAO.insertCategory(cname);
	            System.out.println("Category inserted!");
	        } catch (SQLException e) {
	            System.out.println("Problem occurred while inserting category " + e);
	            throw e;
	        }
    	}
    }

    @FXML
    private void deleteCategory (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            CategoryDAO.deleteCategoryWithCid(Integer.parseInt(cidField.getText()));
            System.out.println("Category deleted! cid: " +cidField.getText());
        } catch (SQLException e) {
            System.out.println("Problem occurred while deleting category " + e);
            throw e;
        }
    }
    
    
    @FXML
    protected void fillTheBlanks(MouseEvent event) {
    	try{
	    	ReadOnlyObjectProperty<Category> selectedCategory = tableView.getSelectionModel().selectedItemProperty();
	    	cidField.setText(selectedCategory.getValue().getCid().toString());
	    	cnameField.setText(selectedCategory.getValue().getCname());
    	} catch(Exception e) {
    		System.out.println("Hi bro");
    	}
    }
    
}