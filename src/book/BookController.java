package book;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import util.Connect;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXDrawer;


public class BookController {

    @FXML
    private TextField isbnField, keywordField;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> isbnCol, titleCol, cidCol,authorCol, pressCol, pdateCol;
    @FXML
    private TableColumn<Book, Integer> quantityCol,lowerlimitCol;
    @FXML
    private TableColumn<Book, Float> pricingCol;
    @FXML
    private JFXDrawer drawer;
    	
    private Executor exec;

    @FXML
    private void initialize () throws ClassNotFoundException, SQLException, IOException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        isbnCol.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        pressCol.setCellValueFactory(cellData -> cellData.getValue().pressProperty());
        cidCol.setCellValueFactory(cellData -> cellData.getValue().cidProperty());
        pdateCol.setCellValueFactory(cellData -> cellData.getValue().pdateProperty());
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        pricingCol.setCellValueFactory(cellData -> cellData.getValue().pricingProperty().asObject());
        lowerlimitCol.setCellValueFactory(cellData -> cellData.getValue().lowerlimitProperty().asObject());
        autoFill();
    }

    @FXML
    private void showBooks(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Book> userList = BookDAO.showBooks();
            populateBooks(userList);
        } catch (SQLException e){
            System.out.println("Error occurred while getting books information from DB" + e);
            throw e;
        }
    }

    private void autoFill() throws SQLException, ClassNotFoundException {
        Task<List<Book>> task = new Task<List<Book>>(){
            @Override
            public ObservableList<Book> call() throws Exception{
                return BookDAO.showBooks();
            }
        };
        task.setOnFailed(e-> task.getException().printStackTrace());
        task.setOnSucceeded(e-> tableView.setItems((ObservableList<Book> ) task.getValue()));
        exec.execute(task);
    }

    @FXML
    private void populateBooks (ObservableList<Book> booksData) throws ClassNotFoundException {
        if (!booksData.isEmpty()) {
        	tableView.setItems(booksData);
        } else {
            System.out.println("No result!");
        }
    }
    
    @FXML
    private void searchBookByIsbn (ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
        	ObservableList<Book> usersResult = BookDAO.searchBooks(isbnField.getText(),true);
        	populateBooks(usersResult);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    @FXML
    private void searchBookByKeyword (ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
        	ObservableList<Book> usersResult = BookDAO.searchBooks(keywordField.getText(),false);
        	populateBooks(usersResult);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void deleteBookByIsbn (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            BookDAO.deleteBookByIsbn(isbnField.getText());
        } catch (SQLException e) {
            System.out.println("Problem occurred while deleting user " + e);
            throw e;
        }
        System.out.println("book deleted! isbn: "+ isbnField.getText());
    }
    
    
    @FXML
    private void insertBook(ActionEvent actionEvent) throws IOException {
    	BookDAO.clearBook();
    	BookDAO.actionMark=1;
    	AnchorPane ap= FXMLLoader.load(getClass().getResource("../view/BookDrawer.fxml"));
    	drawer.setSidePane(ap);
    	drawer.toggle();
    }
    
    @FXML
    protected void editBook(ActionEvent actionEvent) throws IOException {
    	ReadOnlyObjectProperty<Book> selectedBook = tableView.getSelectionModel().selectedItemProperty();
    	if(selectedBook.getValue()==null)System.out.println("Please select a book!");
    	else {
    	try{
	    	
	    	BookDAO.setBook(selectedBook);
	    	BookDAO.actionMark=2;
    	} catch(Exception e) {
    		System.out.println("Hi bro");
    	}
    	AnchorPane ap= FXMLLoader.load(getClass().getResource("../view/BookDrawer.fxml"));
    	drawer.setSidePane(ap);
    	drawer.toggle();
    	}
    }

    @FXML
    private void exportAsXls(ActionEvent ae){
        String sql = "SELECT * FROM Book"; 
        Vector columnName = new Vector();
        columnName.add("ISBN");  
        columnName.add("TITLE");  
        columnName.add("AUTHOR");  
        columnName.add("PRESS");  
        columnName.add("CATEGORY");  
        columnName.add("PUBLIC DATE");  
        columnName.add("QUANTITY");  
        columnName.add("PRICING");
        columnName.add("LOWER LIMIT");  
		try {
			 ResultSet rs = Connect.exeQuery(sql); 
//			 Stage stage = new Stage();
//			 FileChooser fileChooser = new FileChooser();
//			 fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xlsx"),
//		                new FileChooser.ExtensionFilter("XLS", "*.xls"), new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
//			 fileChooser.setTitle("Choose Filepath");
			
//			 File file = fileChooser.showOpenDialog(stage);
			 new util.excel().WriteExcel(rs, "D:/export.xls", "EXPORT", columnName);    
		} catch (SQLException e) {
			e.printStackTrace();
		}  
    }
    
    @FXML
    private void exportAsPDF(ActionEvent actionEvent) throws ClassNotFoundException, SQLException{
    	StackPane root = new StackPane();
    	ObservableList<Book> booklist = BookDAO.showBooks();
    	TableView<Book> table = new TableView<Book>();
		
    	TableColumn<Book,String> isbnCol = new TableColumn<Book,String>("ISBN");
    	TableColumn<Book,String> titleCol = new TableColumn<Book,String>("TITLE");
    	TableColumn<Book,String> authorCol = new TableColumn<Book,String>("AUTHOR");
    	TableColumn<Book,String> pressCol = new TableColumn<Book,String>("PRESS");
    	TableColumn<Book,String> cidCol = new TableColumn<Book,String>("CATEGORY");
    	TableColumn<Book,String> pdateCol = new TableColumn<Book,String>("PDATE");
    	TableColumn<Book,Integer> quantityCol = new TableColumn<Book,Integer>("QUANTITY");
    	TableColumn<Book,Float> pricingCol = new TableColumn<Book,Float>("PRICING");
    	TableColumn<Book,Integer> lowerlimitCol = new TableColumn<Book,Integer>("LOWERLIMIT");
    	
    	
    	isbnCol.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        pressCol.setCellValueFactory(cellData -> cellData.getValue().pressProperty());
        cidCol.setCellValueFactory(cellData -> cellData.getValue().cidProperty());
        pdateCol.setCellValueFactory(cellData -> cellData.getValue().pdateProperty());
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        pricingCol.setCellValueFactory(cellData -> cellData.getValue().pricingProperty().asObject());
        lowerlimitCol.setCellValueFactory(cellData -> cellData.getValue().lowerlimitProperty().asObject());
        
    	
        table.getColumns().add(isbnCol);
        table.getColumns().add(titleCol);
        table.getColumns().add(authorCol);
        table.getColumns().add(pressCol);    
        table.getColumns().add(cidCol);
        table.getColumns().add(pdateCol);
        table.getColumns().add(quantityCol);
        table.getColumns().add(pricingCol);
        table.getColumns().add(lowerlimitCol);
        
    	table.setColumnResizePolicy((TableView.CONSTRAINED_RESIZE_POLICY));
    	table.setFocusTraversable(true);
    	
    	table.setEditable(false);
    	table.setMinHeight(5000);
    	root.getChildren().add(table);
    	root.setMinWidth(720);
    	root.setMaxWidth(720);
    	root.getStylesheets().add(getClass().getResource("../application/PDF.css").toExternalForm());
    	
    	Stage stage = new Stage();
    	
    	PrinterJob job = PrinterJob.createPrinterJob();
    	boolean ready = job.showPrintDialog(stage.getOwner());
    	if(ready){
	    	for(int i=0;i<booklist.size();i+=23)
	    	{
	    		int end = (i+23<booklist.size()) ? i+23 : booklist.size();
	    		table.setItems(FXCollections.observableList(booklist.subList(i, end)));
	    		job.printPage(root);
	    	}
	    	job.endJob();
    	}
		       
    }
}
