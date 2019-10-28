package book;

import javafx.beans.property.*;

public class Book {
    private StringProperty isbn, title, author, press, pdate, cid;
    private IntegerProperty quantity,lowerlimit;
    private FloatProperty pricing;
    public Book() {
        this.isbn = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.press = new SimpleStringProperty();
        this.cid = new SimpleStringProperty();
        this.pdate = new SimpleStringProperty();
        this.quantity = new SimpleIntegerProperty();
        this.pricing = new SimpleFloatProperty();
        this.lowerlimit = new SimpleIntegerProperty();
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

    //title
    public String getTitle () {
        return title.get();
    }

    public void setTitle(String title){
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    //author
    public String getAuthor () {
        return author.get();
    }

    public void setAuthor(String author){
        this.author.set(author);
    }

    public StringProperty authorProperty() {
        return author;
    }
    
    //press
    public String getPress () {
        return press.get();
    }

    public void setPress(String press){
        this.press.set(press);
    }

    public StringProperty pressProperty() {
        return press;
    }
    
    //cid -> categroy
    public String getCid () {
        return cid.get();
    }

    public void setCid(String cid){
        this.cid.set(cid);
    }

    public StringProperty cidProperty() {
        return cid;
    }
    
    //pdate
    public String getPdate () {
        return pdate.get();
    }

    public void setPdate(String pdate){
        this.pdate.set(pdate);
    }

    public StringProperty pdateProperty() {
        return pdate;
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
    
    //price
    public Float getPricing () {
        return pricing.get();
    }

    public void setPricing(Float pricing){
        this.pricing.set(pricing);
    }

    public FloatProperty pricingProperty() {
        return pricing;
    }
    
    //lowerlimit
    public Integer getLlowerlimit () {
        return lowerlimit.get();
    }

    public void setLowerlimit(Integer lowerlimit){
        this.lowerlimit.set(lowerlimit);
    }

    public IntegerProperty lowerlimitProperty() {
        return lowerlimit;
    }
}
