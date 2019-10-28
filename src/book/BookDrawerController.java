package book;

import java.sql.SQLException;

import com.jfoenix.controls.JFXTextField;

import category.CategoryDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import order.OrderDAO;
import util.AlertMaker;

public class BookDrawerController {
	@FXML
	private JFXTextField nisbnField,ntitleField,nauthorField,npressField,ncidField,npdateField,npricingField,nlowerlimitField;
	@FXML
	private Text drawerTitle,cidHint;

	static boolean cidOk=false;
	
    @FXML
    private void initialize () {
    	if(BookDAO.actionMark==2) {
    		drawerTitle.setText("Edit Book Detail");
	    	nisbnField.setText(BookDAO.isbn);
	    	nisbnField.setEditable(false);
	    	
			ntitleField.setText(BookDAO.title);
			nauthorField.setText(BookDAO.author);
			npressField.setText(BookDAO.press);
			ncidField.setText(""+BookDAO.cid);
			npdateField.setText(BookDAO.pdate);
			npricingField.setText(BookDAO.pricing);
			nlowerlimitField.setText(BookDAO.lowerlimit);
    	} else {
    		drawerTitle.setText("Add a New Book");
    	}
    }
    
	   
	@FXML
    private void insertBook(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		String isbn= "",title= "",author= "",press= "",pdate= "",cid= "",lowerlimit = "",pricing = "";
//		int lowerlimit=-1;
//		float pricing = 0;
		int quantity = 0;
		try{
	    	isbn=nisbnField.getText();
	    	title=ntitleField.getText();
	    	author=nauthorField.getText();
	    	press=npressField.getText();
	    	pdate=npdateField.getText();
	    	cid=ncidField.getText();
	    	lowerlimit=nlowerlimitField.getText();
	    	pricing=npricingField.getText();
	    	if(Integer.parseInt(lowerlimit)<=0||Float.parseFloat(pricing)<=0.0)
    			new AlertMaker("Wrong Input");
    		else{
	    		if(cidOk){
		    		Book book = new Book();
		    		book.setIsbn(isbn);
		    		book.setTitle(title);
		    		book.setAuthor(author);
		    		book.setPress(press);
		    		book.setPdate(pdate);
		    		book.setCid(cid);
		    		book.setQuantity(quantity);
		    		book.setLowerlimit(Integer.parseInt(lowerlimit));
		    		book.setPricing(Float.parseFloat(pricing));
			        try {
			            BookDAO.insertBook(book);
			        } catch (SQLException e) {
			            System.out.println("Problem occurred while inserting book " + e);
			            throw e;
			        }
			        cidOk=false;
			        System.out.println("success!");
	    		}else{
	    			new AlertMaker("Please check your category ID");
	    		}
    		}
		} catch (Exception e){
			new util.AlertMaker("Wrong Input");
		}
    	
    }
	
    @FXML
    private void checkCid(ActionEvent actionEvent) throws SQLException {
    	cidHint.setText("");
    	
    	if(!ncidField.getText().equals("")){
	    	String id = ncidField.getText();
	    	String name = CategoryDAO.checkCid(id);
	    	if(name==null){
	    		cidHint.setFill(Color.rgb(200,0,0));
	    		cidHint.setText("The firm doesn't exist");
	    	} else {
	    		cidHint.setFill(Color.rgb(0,120,0));
	    		cidHint.setText("Category "+id+":"+""+name+"");
	    		cidOk=true;
	    	}
    	} else {
    		cidHint.setFill(Color.rgb(200,0,0));
    		cidHint.setText("The category doesn't exist");
    	}
    }
}
