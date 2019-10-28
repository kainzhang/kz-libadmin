package alertinfo;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import menu.MenuController;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import book.Book;



public class AlertInfoController {

	@FXML
	private TextField aidField;
    @FXML
    private TableView<AlertInfo> tableView;
    @FXML
    private TableColumn<AlertInfo, Integer> aidCol;
    @FXML
    private TableColumn<AlertInfo, String> isbnCol, detailCol, dateCol, isvalidCol;

    private Executor exec;
    
    @FXML
    private void initialize () throws ClassNotFoundException, SQLException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        aidCol.setCellValueFactory(cellData -> cellData.getValue().aidProperty().asObject());
        isbnCol.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        detailCol.setCellValueFactory(cellData -> cellData.getValue().detailProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        isvalidCol.setCellValueFactory(cellData -> cellData.getValue().isvalidProperty());
        fillAlertInfoTable();
    }

    @FXML
    private void showAlertInfos(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<AlertInfo> alertInfoList = AlertInfoDAO.showAlertInfo();
            populateAlertInfo(alertInfoList);
        } catch (SQLException e){
            System.out.println("Error occurred while getting users information from DB" + e);
            throw e;
        }
    }

    private void fillAlertInfoTable() throws SQLException, ClassNotFoundException {
        Task<List<AlertInfo>> task = new Task<List<AlertInfo>>(){
            @Override
            public ObservableList<AlertInfo> call() throws Exception{
                return AlertInfoDAO.showAlertInfo();
            }
        };
        task.setOnFailed(e-> task.getException().printStackTrace());
        task.setOnSucceeded(e-> tableView.setItems((ObservableList<AlertInfo> ) task.getValue()));
        exec.execute(task);
    }

    @FXML
    private void populateAlertInfo (ObservableList<AlertInfo> alertInfoData) throws ClassNotFoundException {
        if (!alertInfoData.isEmpty()) {
        	tableView.setItems(alertInfoData);
        } else {
            System.out.println("No result!");
        }
    }
    
    @FXML
    private void fillIdField(MouseEvent me){
    	ReadOnlyObjectProperty<AlertInfo> selectedInfo = tableView.getSelectionModel().selectedItemProperty();
    	aidField.setText(selectedInfo.getValue().getAid().toString());
    }
}