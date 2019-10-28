package snp;

import java.sql.SQLException;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class SnpDrawerController {
	@FXML
	private JFXTextField nameField, ownerField, otitleField, telField, addrField, bankField, bankidField;
	@FXML
	private JFXRadioButton supplier,seller;
	@FXML
	private ToggleGroup snpType; 
	@FXML
	private Text drawerTitle;

    @FXML
    private void initialize () {
    	if(SnpDAO.actionMark==2) {
    		drawerTitle.setText("Edit Information");
	    	nameField.setText(SnpDAO.name);
	    	ownerField.setText(SnpDAO.owner);
	    	otitleField.setText(SnpDAO.otitle);
	    	telField.setText(SnpDAO.tel);
	    	addrField.setText(SnpDAO.addr);
	    	bankField.setText(SnpDAO.bank);
	    	bankidField.setText(SnpDAO.bankid);
	    	supplier.setVisible(false);
	    	seller.setVisible(false);
    	} else {
    		supplier.setVisible(true);
	    	seller.setVisible(true);
    		drawerTitle.setText("Add New Supplier and Seller");
    	}
    	supplier.setUserData(1);
    	seller.setUserData(2);
    }
    
	   
	@FXML
    private void insertSnp(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		String name, owner, otitle, tel, addr, bank, bankid;
    	int sign= 0;
    	if(SnpDAO.actionMark==1)sign=(int)snpType.getSelectedToggle().getUserData();
    	name=nameField.getText();
    	owner=ownerField.getText();
    	otitle=otitleField.getText();
    	tel=telField.getText();
    	addr=addrField.getText();
    	bank=bankField.getText();
    	bankid=bankidField.getText();
    	if(name.equals("")||owner.equals("")||otitle.equals("")||tel.equals("")||addr.equals("")||bank.equals("")||bankid.equals(""))
    	{
    		System.out.println("Wrong Input!");
    	} else {
    		Snp snp = new Snp();
    		snp.setName(name);
    		snp.setOwner(owner);
    		snp.setOtitle(otitle);
    		snp.setTel(tel);
    		snp.setAddr(addr);
    		snp.setBank(bank);
    		snp.setBankid(bankid);
    		snp.setSign(sign);
    		if(sign==0){
    			snp.setId(Integer.parseInt(SnpDAO.id));
    			SnpDAO.updateSnp(snp);
    		}else{
		        try {
		            SnpDAO.insertSnp(snp);
		        } catch (SQLException e) {
		            System.out.println("Problem occurred while inserting snp " + e);
		            throw e;
		        }
    		}
	        System.out.println("success!");
    	}
    }
}
