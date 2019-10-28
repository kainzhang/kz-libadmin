package alertinfo;

import javafx.beans.property.*;

public class AlertInfo {
    private IntegerProperty aid;
    private StringProperty isbn,detail,date,isvalid;

    public AlertInfo() {
    	this.aid = new SimpleIntegerProperty();
        this.isbn = new SimpleStringProperty();
        this.detail = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
        this.isvalid = new SimpleStringProperty();
    }

    //aid
    public Integer getAid() {
        return aid.get();
    }

    public void setAid(Integer aid){
        this.aid.set(aid);
    }

    public IntegerProperty aidProperty(){
        return aid;
    }

    //isbn
    public String getisbn () {
        return isbn.get();
    }

    public void setIsbn(String isbn){
        this.isbn.set(isbn);
    }

    public StringProperty isbnProperty() {
        return isbn;
    }
    
    //detail
    public String getDetail () {
        return detail.get();
    }

    public void setDetail(String detail){
        this.detail.set(detail);
    }

    public StringProperty detailProperty() {
        return detail;
    }
    
  //isbn
    public String getDate () {
        return date.get();
    }

    public void setDate(String date){
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }

    //isvalid
    public String getIsvalid() {
        return isvalid.get();
    }

    public void setIsvalid(int num){
    	String isvalid = null;
    	if(num==0)isvalid="已处理";
    	else if(num==1)isvalid="未处理";
        this.isvalid.set(isvalid);
    }

    public StringProperty isvalidProperty(){
        return isvalid;
    }
}

