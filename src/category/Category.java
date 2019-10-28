package category;

import javafx.beans.property.*;

public class Category {
    private IntegerProperty cid;
    private StringProperty cname;

    public Category() {
    	this.cid = new SimpleIntegerProperty();
        this.cname = new SimpleStringProperty();
    }

    //cid
    public Integer getCid() {
        return cid.get();
    }

    public void setCid(Integer cid){
        this.cid.set(cid);
    }

    public IntegerProperty cidProperty(){
        return cid;
    }

    //cname
    public String getCname () {
        return cname.get();
    }

    public void setCname(String cname){
        this.cname.set(cname);
    }

    public StringProperty cnameProperty() {
        return cname;
    }
}

