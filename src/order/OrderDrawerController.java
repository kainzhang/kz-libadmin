package order;

import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;

import book.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import util.AlertMaker;

public class OrderDrawerController {
	@FXML
	private TextField isbnField, quantityField, unitpriceField, snpidField;
	@FXML
	private Text isbnHint, quantityHint;
	@FXML
	private Text snpidHint;
	@FXML
	private Text submitHint;
	@FXML
	private JFXButton submitBtn;
	@FXML
	private ToggleGroup orderType;
    @FXML
    private JFXRadioButton orderS,orderP;
    
    @FXML
    private void initialize (){
    	orderS.setUserData(1);
        orderP.setUserData(2);
    }
    
    static boolean isbnOk=false,snpidOk=false,quantityOk=false;
	   
    @FXML
    private void insertOrder (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    	if(isbnOk&&snpidOk){
	    	int quantity;
	    	float unitprice;
	    	int sign =(int)(orderType.getSelectedToggle().getUserData());
	    	quantity = Integer.parseInt(quantityField.getText());
	    	unitprice = Float.parseFloat(unitpriceField.getText());
	    	
	    	if(quantity<0){
	    		quantityField.setText("");
	    		new AlertMaker("Wrong Input");
	    	}else{
	    	
		    	Order order = new Order();
		    	order.setIsbn(isbnField.getText());
		    	order.setQuantity(quantity);
		    	order.setUnitprice(unitprice);
		    	order.setAmount(quantity*unitprice);
		    	order.setSnpid(snpidField.getText());
		    	order.setSign(sign);
		    	
		        try {
		            int flag = OrderDAO.insertOrder(order);
		            if(flag == 1||flag == 3){
		            	System.out.println("order inserted!");
		            	submitHint.setFill(Color.rgb(0,120,0));
		            	submitHint.setText("Insert Successfully");
		            	clearAll();
		            }
		            else if(flag ==2){
		            	submitHint.setFill(Color.rgb(200,0,0));
		            	submitHint.setText("Items are not available");
		            	clearAll();
		            }
		            if(flag==3)new AlertMaker("A new alert need to settle!");
		        } catch (SQLException e) {
		            System.out.println("Problem occurred while inserting user " + e);
		            throw e;
		        }
	    	}
    	} else {
			new AlertMaker("Please check the isbn field and firm id field");
    	}
    }
    
    @FXML
    private void checkIsbn(ActionEvent actionEvent) throws SQLException{
    	
    	isbnHint.setText("");
    	quantityHint.setText("");
    	String isbn = isbnField.getText();
    	Book book = null;
    	book = OrderDAO.checkIsbn(isbn);
    	if(book==null){
    		isbnHint.setFill(Color.rgb(200,0,0));
    		isbnHint.setText("The ISBN doesn't exist");
    		isbnOk = false;
    	} else {
    		isbnHint.setFill(Color.rgb(0,120,0));
    		isbnHint.setText("Book: "+""+book.getTitle()+"");
    		quantityHint.setText("Inventory:"+book.getQuantity()+"");
    		isbnOk=true;
    	}
    	
    }
    
    @FXML
    private void checkSnpid(ActionEvent actionEvent) throws SQLException {
    	snpidHint.setText("");
    	
    	if(!snpidField.getText().equals("")){  	//改正则表达式
	    	int id = Integer.parseInt(snpidField.getText());
	    	String name = OrderDAO.checkSnpid(id);
	    	if(name==null){
	    		snpidHint.setFill(Color.rgb(200,0,0));
	    		snpidHint.setText("The firm doesn't exist");
	    		snpidOk = false;
	    	} else {
	    		snpidHint.setFill(Color.rgb(0,120,0));
	    		snpidHint.setText("Firm "+id+":"+""+name+"");
	    		snpidOk = true;
	    	}
    	} else {
    		snpidHint.setFill(Color.rgb(200,0,0));
    		snpidHint.setText("The firm doesn't exist");
    	}
    }
    
    public void clearAll(){
    	snpidHint.setText("");
    	isbnHint.setText("");
    	quantityHint.setText("");
    	isbnOk=false;
    	snpidOk=false;
    }
}
