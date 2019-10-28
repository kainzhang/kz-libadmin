package user;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Connect;

public class UserDAO {
	public static ObservableList<User> backup;
	
    public static ObservableList<User> searchUser (String username) throws SQLException, ClassNotFoundException {
        try {
            ResultSet rsSearchUsers = Connect.exeQuery("SELECT * FROM user WHERE username LIKE '%"+username+"%';");
            ObservableList<User>searchUserList = getUserList(rsSearchUsers);
            backup = searchUserList;
            return searchUserList;
        } catch (SQLException e) {
            System.out.println("While searching a user with '" + username + "', an error occurred: " + e);
            throw e;
        }
    }


    public static ObservableList<User> showUsers () throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM user";
        try {
            ResultSet rsUsers = Connect.exeQuery(selectStmt);
            ObservableList<User> userList = getUserList(rsUsers);
            backup = userList;
            return userList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    private static ObservableList<User> getUserList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<User> userList = FXCollections.observableArrayList();
        while (rs.next()) {
            User user = new User();
            user.setUsername(rs.getString("USERNAME"));
            user.setPassword(rs.getString("PASSWORD"));
            user.setPosition(rs.getInt("POSITION"));
            userList.add(user);
        }
        return userList;
    }

    public static void updateUser (String username, String password, String position) throws SQLException, ClassNotFoundException {
        String updateStmt =
        				" BEGIN;"+
                        " UPDATE user" +	
                        " SET PASSWORD ='"+password+"'," +
                        " POSITION ="+Integer.parseInt(position)+" "+
                        " WHERE USERNAME ='"+username+"';" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void deleteUserWithUsername (String username) throws SQLException, ClassNotFoundException {
        String updateStmt =
        				" BEGIN;" +
                        " DELETE FROM user" +
                        " WHERE username='"+username+"';" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public static void insertUser (String username, String password, int position) throws SQLException, ClassNotFoundException {
    	String updateStmt =
        				" BEGIN;" +
                        " INSERT INTO user" +
                        " (USERNAME, PASSWORD, POSITION)" +
                        " VALUES" +
                        " ('"+username+"', '"+password+"',"+position+");" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
    
	public static boolean userExist(String username) throws SQLException{
		ResultSet rs = Connect.exeQuery("SELECT * FROM user WHERE username='"+username+"';");
		if(rs.next())
			return true;
		else 
			return false;
	}
	
	public static User passwordCorrect(String username, String password) throws SQLException{
		ResultSet rs = Connect.exeQuery("SELECT * FROM USER WHERE username='"+username+"' AND password='"+password+"';");
		if(rs.next()){
			User user = new User();
	   		user.setUsername(rs.getString("USERNAME"));
	   		user.setPassword(rs.getString("PASSWORD"));
	   		user.setPosition(rs.getInt("POSITION"));
			return user;
		} else 
			return null;
	}
}