package category;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import user.User;
import util.Connect;

public class CategoryDAO { 
    public static ObservableList<Category> searchCategory (String cname) throws SQLException, ClassNotFoundException {
    	String selectStmt = "SELECT * FROM category WHERE canme LIKE '%"+cname+"%';";
        try {
            ResultSet rsCategories = Connect.exeQuery(selectStmt);
            ObservableList<Category> categoryList = getCategoryList(rsCategories);
            return categoryList;
        } catch (SQLException e) {
            System.out.println("While searching a category with '" + cname + "', an error occurred: " + e);
            throw e;
        }
    }
    
    public static ObservableList<Category> showCategories () throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM category";
        try {
            ResultSet rsCategories = Connect.exeQuery(selectStmt);
            ObservableList<Category> categoryList = getCategoryList(rsCategories);
            return categoryList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    private static ObservableList<Category> getCategoryList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Category> categoryList = FXCollections.observableArrayList();
        while (rs.next()) {
        	Category category = new Category();
        	category.setCid(rs.getInt("CID"));
        	category.setCname(rs.getString("CNAME"));
            categoryList.add(category);
        }
        return categoryList;
    }

    public static void updateCname (int cid, String cname) throws SQLException, ClassNotFoundException {
        String updateStmt =
        				" BEGIN;"+
                        " UPDATE category" +
                        " SET cname ='"+cname+"'" +
                        " WHERE cid = "+cid+";" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void deleteCategoryWithCid (int cid) throws SQLException, ClassNotFoundException {
        String updateStmt =
        				" BEGIN;" +
                        " DELETE FROM category" +
                        " WHERE cid="+cid+";" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public static void insertCategory (String cname) throws SQLException, ClassNotFoundException {
    	String updateStmt =
        				" BEGIN;" +
                        " INSERT INTO category" +
                        " (CID, CNAME)" +
                        " VALUES" +
                        " (null, '"+cname+"');" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
    
    public static String checkCid(String id) throws SQLException{
    	String name=null;
    	String selectStmt = "SELECT * FROM category WHERE CID="+Integer.parseInt(id)+";";
    	ResultSet rs = Connect.exeQuery(selectStmt);
    	if(rs.next())name = rs.getString("CNAME");
    	return name;
    }
}
