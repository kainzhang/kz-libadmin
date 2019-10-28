package snp;

import javafx.beans.property.*;
public class Snp {
	private IntegerProperty id, sign;
    private StringProperty name, owner, otitle, tel, addr, bank, bankid;

    public Snp() {
    	this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.owner = new SimpleStringProperty();
        this.otitle = new SimpleStringProperty();
        this.tel = new SimpleStringProperty();
        this.addr = new SimpleStringProperty();
        this.bank = new SimpleStringProperty();
        this.bankid = new SimpleStringProperty();
        this.sign = new SimpleIntegerProperty();
    }
    
    //id
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id){
        this.id.set(id);
    }

    public IntegerProperty idProperty(){
        return id;
    }


    //name
    public String getName() {
        return name.get();
    }

    public void setName(String name){
        this.name.set(name);
    }

    public StringProperty nameProperty(){
        return name;
    }

    //owner
    public String getOwner () {
        return owner.get();
    }

    public void setOwner(String owner){
        this.owner.set(owner);
    }

    public StringProperty ownerProperty() {
        return owner;
    }

    //otitle
    public String getOtitle () {
        return otitle.get();
    }

    public void setOtitle(String otitle){
        this.otitle.set(otitle);
    }

    public StringProperty otitleProperty() {
        return otitle;
    }
    
    //tel
    public String getTel () {
        return tel.get();
    }

    public void setTel(String tel){
        this.tel.set(tel);
    }

    public StringProperty telProperty() {
        return tel;
    }
    
    //addr
    public String getAddr () {
        return addr.get();
    }

    public void setAddr(String addr){
        this.addr.set(addr);
    }

    public StringProperty addrProperty() {
        return addr;
    }
    
    //bank
    public String getbank () {
        return bank.get();
    }

    public void setBank(String bank){
        this.bank.set(bank);
    }

    public StringProperty bankProperty() {
        return bank;
    }
    
    //bankid
    public String getBankid () {
        return bankid.get();
    }

    public void setBankid(String bankid){
        this.bankid.set(bankid);
    }

    public StringProperty bankidProperty() {
        return bankid;
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