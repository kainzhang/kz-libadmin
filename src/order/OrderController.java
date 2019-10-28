package order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import util.Tools;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXRadioButton;


public class OrderController {
	
    @FXML
    private TableView<Order> tableView;
    @FXML
    private TableColumn<Order, Integer> orderidCol, quantityCol;
    @FXML
    private TableColumn<Order, Float> unitpriceCol, amountCol;
    @FXML
    private TableColumn<Order, String> isbnCol, dateCol, usernameCol, snpidCol;
    @FXML
    private ComboBox<String> yearCombo,monthCombo;
    @FXML
    private JFXRadioButton saleRadio,purchaseRadio;
    @FXML
    private ToggleGroup searchGroup;
    @FXML
    private Text searchHint;
    @FXML
    private PieChart pieChart;
    @FXML
    private AnchorPane chartPane;
    @FXML
    private JFXDrawer drawer;
    
    private Executor exec1;

    @FXML
    private void initialize () throws ClassNotFoundException, SQLException {
        exec1 = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setPriority(10);
            t.setDaemon(true);
            return t;
        });
        orderidCol.setCellValueFactory(cellData -> cellData.getValue().orderidProperty().asObject());
        isbnCol.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        unitpriceCol.setCellValueFactory(cellData -> cellData.getValue().unitpriceProperty().asObject());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        snpidCol.setCellValueFactory(cellData -> cellData.getValue().snpidProperty());
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        autoFill(1);
        
        saleRadio.setUserData(1);
        purchaseRadio.setUserData(2);
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=1990;i<=year;i++){
        	yearCombo.getItems().add(""+i);
        }
        for(int i=1;i<13;i++){
        	if(i<10)
        		monthCombo.getItems().add("0"+i);
        	else 
        		monthCombo.getItems().add(""+i);
        }
        tableView.setVisible(true);
    }

    
    @FXML
    private void showOrders(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    	int sign = 1;
    	Object obj = actionEvent.getSource();
    	String signId= Tools.getFieldValueByName("id",obj);
    	if(signId.equals("showS"))sign=1;
    	if(signId.equals("showP"))sign=2;
        try {
            ObservableList<Order> orderList = OrderDAO.showOrders(sign);
            populateOrders(orderList);
        } catch (SQLException e){
            System.out.println("Error occurred while getting snps information from DB" + e);
            throw e;
        }
    }
    
    private void autoFill(int sign) throws SQLException, ClassNotFoundException {
        Task<List<Order>> task = new Task<List<Order>>(){
            @Override
            public ObservableList<Order> call() throws Exception{
                return OrderDAO.showOrders(sign);  // sign=1 sale sign=2 purchase
            }
        };
        task.setOnFailed(e-> task.getException().printStackTrace());
        task.setOnSucceeded(e-> tableView.setItems((ObservableList<Order> ) task.getValue()));
        exec1.execute(task);
    }

    @FXML
    private void insertOrder (ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
    	AnchorPane ap= FXMLLoader.load(getClass().getResource("../view/OrderDrawer.fxml"));
    	drawer.setSidePane(ap);
    	drawer.toggle();
    }

    @FXML
    private void populateOrders (ObservableList<Order> orderData) throws ClassNotFoundException {
        if (!orderData.isEmpty()) {
        	tableView.setItems(orderData);
        } else {
            System.out.println("No result!");
            tableView.setItems(null);
        }
    }
    
    @FXML
    private void searchOrder (ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
    	chartPane.setVisible(false);
    	pieChart.setVisible(false);
    	tableView.setVisible(true);
    	searchHint.setText("");
    	int sign = (int) searchGroup.getSelectedToggle().getUserData();
    	if(yearCombo.getValue()!=null&&monthCombo.getValue()!=null){
	    	try {
	        	ObservableList<Order> orderResult = OrderDAO.searchOrdersByMonth(yearCombo.getValue(),monthCombo.getValue(),sign);
	        	populateOrders(orderResult);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw e;
	        }
    	} else {
    		searchHint.setText("Please select year and month！");
    		searchHint.setFill(Color.rgb(255,255,255));
    	}
    }
    
    @FXML 
    private void showPieChart (ActionEvent ae) throws ClassNotFoundException, SQLException{
	    tableView.setVisible(false);                          //添加外部PANE
	    chartPane.setVisible(false);
	    String[] monthName = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
    	int sign = (int) searchGroup.getSelectedToggle().getUserData();
    	if(yearCombo.getValue()!=null){
	    	ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
	    	
	    	for(int i=1;i<=12;i++)
	    		if(i<10)
	    			pieChartData.add(new PieChart.Data(monthName[i-1], OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "0"+i, sign)));
	    		else
	    			pieChartData.add(new PieChart.Data(monthName[i-1], OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), ""+i, sign)));
	        pieChart.setData(pieChartData);
	        pieChart.setLegendSide(Side.LEFT);
	        if(sign==1)pieChart.setTitle("Sale Order of "+yearCombo.getValue());
	        if(sign==2)pieChart.setTitle("Purchase Order of "+yearCombo.getValue());
	        
    	}else {
    		searchHint.setText("Please select year！");
    		searchHint.setFill(Color.rgb(255,255,255));
    	}
    	pieChart.setVisible(true);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
    private void showLineChart (ActionEvent ae) throws SQLException{   //改边距
		chartPane.getChildren().clear();
		int sign = (int) searchGroup.getSelectedToggle().getUserData();
    	tableView.setVisible(false);
    	pieChart.setVisible(false);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        yAxis.setLabel("RMB");
        final LineChart<String,Number> lineChart = 
                new LineChart<String,Number>(xAxis,yAxis);
        if(sign==1)lineChart.setTitle("Sale Order of "+yearCombo.getValue());
        if(sign==2)lineChart.setTitle("Purchase Order of "+yearCombo.getValue());
        
        XYChart.Series series = new XYChart.Series();
        series.setName("LOKKA");
        series.getData().add(new XYChart.Data("Jan", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "01", sign)));
        series.getData().add(new XYChart.Data("Feb", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "02", sign)));
        series.getData().add(new XYChart.Data("Mar", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "03", sign)));
        series.getData().add(new XYChart.Data("Apr", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "04", sign)));
        series.getData().add(new XYChart.Data("May", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "05", sign)));
        series.getData().add(new XYChart.Data("June", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "06", sign)));
        series.getData().add(new XYChart.Data("July", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "07", sign)));
        series.getData().add(new XYChart.Data("Aug", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "08", sign)));
        series.getData().add(new XYChart.Data("Sept", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "09", sign)));
        series.getData().add(new XYChart.Data("Oct", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "10", sign)));
        series.getData().add(new XYChart.Data("Nov", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "11", sign)));
        series.getData().add(new XYChart.Data("Dec", OrderDAO.getAmountByYearAndMonth(yearCombo.getValue(), "12", sign)));
        
        chartPane.getChildren().add(lineChart);
        lineChart.getData().add(series);
//        chartPane.setBottomAnchor(chartPane, 0.0);
//        chartPane.setLeftAnchor(chartPane, 0.0);
//        chartPane.setRightAnchor(chartPane, 0.0);
        chartPane.setTopAnchor(chartPane, 200.0);
        lineChart.setMinHeight(520);
//        lineChart.setPrefHeight(520);
        lineChart.setMinWidth(1030);
//        lineChart.setPrefWidth(1030);
        chartPane.setVisible(true);
    }
	
//    @FXML
//    private void deleteOrderWithOrderid (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
//        try {
//            OrderDAO.deleteOrderWithOrderId(Integer.parseInt(orderidField.getText()));
//        } catch (SQLException e) {
//            System.out.println("Problem occurred while deleting order " + e);
//            throw e;
//        }
//        System.out.println("order deleted! isbn: "+ orderidField.getText());
//    }

}