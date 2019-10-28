package order;

import javafx.beans.property.*;
public class Order {
	private IntegerProperty orderid, quantity, sign;
    private StringProperty date, username, isbn, snpid;
    private FloatProperty unitprice, amount;
		
    public Order() {
    	this.orderid = new SimpleIntegerProperty();
        this.isbn = new SimpleStringProperty();
        this.quantity = new SimpleIntegerProperty();
        this.unitprice = new SimpleFloatProperty();
        this.amount = new SimpleFloatProperty();
        this.date = new SimpleStringProperty();
        this.snpid = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.sign = new SimpleIntegerProperty();
    }
    
    //orderid
    public Integer getOrderid() {
        return orderid.get();
    }

    public void setOrderid(Integer orderid){
        this.orderid.set(orderid);
    }

    public IntegerProperty orderidProperty(){
        return orderid;
    }

    //isbn
    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn){
        this.isbn.set(isbn);
    }

    public StringProperty isbnProperty(){
        return isbn;
    }

    //quantity
    public Integer getQuantity () {
        return quantity.get();
    }

    public void setQuantity(Integer quantity){
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    //unitprice
    public Float getUnitprice () {
        return unitprice.get();
    }

    public void setUnitprice(Float unitprice){
        this.unitprice.set(unitprice);
    }

    public FloatProperty unitpriceProperty() {
        return unitprice;
    }
    
    //amount
    public Float getAmount () {
        return amount.get();
    }

    public void setAmount(Float amount){
        this.amount.set(amount);
    }

    public FloatProperty amountProperty() {
        return amount;
    }
    
    //date
    public String getDate () {
        return date.get();
    }

    public void setDate(String date){
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }
    
    //snpid  -> snp
    public String getSnpid () {
        return snpid.get();
    }

    public void setSnpid(String snpid){
        this.snpid.set(snpid);
    }

    public StringProperty snpidProperty() {
        return snpid;
    }
    
    //username
    public String getUsername () {
        return username.get();
    }

    public void setUsername(String username){
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }
    
    //sign
    public Integer getSign () {
        return sign.get();
    }

    public void setSign(Integer sign){
        this.sign.set(sign);
    }

    public IntegerProperty signProperty() {
        return sign;
    }
}