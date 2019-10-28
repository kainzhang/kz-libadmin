package user;

import javafx.beans.property.*;

public class User {
    private StringProperty username, password, position;

    public User() {
    	this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.position = new SimpleStringProperty();
    }
    
    //username
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username){
        this.username.set(username);
    }

    public StringProperty usernameProperty(){
        return username;
    }

    //password
    public String getPassword () {
        return password.get();
    }

    public void setPassword(String password){
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    //position
    public String getPosition() {
        return position.get();
    }

    public void setPosition(int num){
    	String position;
    	if(num==0)position="Staff";
    	else if(num==1)position="Admin";
    	else position="Superuser";
        this.position.set(position);
    }

    public StringProperty positionProperty(){
        return position;
    }
}

