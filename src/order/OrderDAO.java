package order;

import java.sql.ResultSet;
import java.sql.SQLException;

import alertinfo.AlertInfoDAO;
import application.Launcher;
import book.Book;
import book.BookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Connect;

public class OrderDAO {
    public static ObservableList<Order> searchOrdersByMonth (String year,String month, int sign) throws SQLException, ClassNotFoundException {
    	try {
    		 String select =
    			        " SELECT o.*, b.title, s.name "+
    			        " FROM orderinfo o, book b, snp s "+
    			        " WHERE o.isbn=b.isbn "+
    			        " AND o.snpid=s.snpid "+
    			        " AND o.sign="+sign+" "+
    			        " AND date LIKE '"+year+"-"+month+"-%' "+
    			        " AND o.sign="+sign+" "+
    			        " ORDER BY o.orderid DESC;";
    		 String select2 ="SELECT * FROM orderinfo WHERE date LIKE '"+year+"-"+month+"-%' AND sign="+sign+" ORDER BY ORDERID DESC;";
            ResultSet rsOrder = Connect.exeQuery(select);
            ObservableList<Order>orderList = getOrderList(rsOrder);
            return orderList;
        } catch (SQLException e) {
            System.out.println("While searching a snp by month, an error occurred: " + e);
            throw e;
        }
    }


    public static ObservableList<Order> showOrders (int sign) throws SQLException, ClassNotFoundException {
//        String selectStmt = "SELECT * FROM Orderinfo WHERE sign="+sign+" ORDER BY ORDERID DESC;";
        String select =
        " SELECT o.*, b.title, s.name "+
        " FROM orderinfo o, book b, snp s "+
        " WHERE o.isbn=b.isbn "+
        " AND o.snpid=s.snpid "+
        " AND o.sign="+sign+" "+
        " ORDER BY o.orderid DESC;";
        try {
            ResultSet rsOrders = Connect.exeQuery(select);
            ObservableList<Order> orderList = getOrderList(rsOrders);
            return orderList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }


    private static ObservableList<Order> getOrderList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Order> orderList = FXCollections.observableArrayList();
        while (rs.next()) {
        	Order order = new Order();
            order.setOrderid(rs.getInt("ORDERID"));
            order.setIsbn(rs.getString("title"));
            order.setQuantity(rs.getInt("QUANTITY"));
            order.setUnitprice(rs.getFloat("UNITPRICE"));
            order.setAmount(rs.getFloat("AMOUNT"));
            order.setDate(rs.getString("DATE"));
            order.setSnpid(rs.getString("name"));
            order.setUsername(rs.getString("USERNAME"));
            order.setSign(rs.getInt("SIGN"));
            orderList.add(order);
        }
        return orderList;
    }


    public static void deleteOrderByOrderId (int orderId) throws SQLException, ClassNotFoundException {
        String updateStmt =
        				" BEGIN;"+
                        " DELETE FROM orderinfo" +
                        " WHERE orderid="+orderId+";" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
    
    public static int insertOrder (Order order) throws SQLException, ClassNotFoundException {
    	int quantity ,currentQuantity,lowerlimit,sign;
    	quantity = order.getQuantity();
    	sign = order.getSign();
    	
    	String updateStmt1 =
        				" BEGIN;"+
                        " INSERT INTO orderinfo" +
                        " (ORDERID, ISBN, QUANTITY, UNITPRICE, AMOUNT, DATE, SNPID, USERNAME, SIGN)" +
                        " VALUES" +
                        " (null, '"+order.getIsbn()+"', "+order.getQuantity()+", "+order.getUnitprice()+", " +
                        " "+order.getAmount()+", datetime('now','localtime'), "+Integer.parseInt(order.getSnpid())+", " + 
                        " '"+Launcher.getUser().getUsername()+"', "+sign+"); "+
                        " COMMIT;";
    	
    	ResultSet rs = Connect.exeQuery(" SELECT * FROM book WHERE ISBN='"+order.getIsbn()+"';");
    	
    	int flag =1;
    	
    	if(rs.next()){
    		currentQuantity=rs.getInt("QUANTITY");
    		lowerlimit=rs.getInt("LOWERLIMIT");
    		if (sign == 1){                               
    			if (currentQuantity>=quantity){
    				Connect.exeUpdate(updateStmt1);       
    				quantity = currentQuantity-quantity;  
    				if (quantity <lowerlimit ){
    					AlertInfoDAO.insertAlertInfo(order.getIsbn(), "The quantity is below the lower limit");
    					flag=3;
    				}
    				String updateStmt2 =
        					" BEGIN;"+
        					" UPDATE book SET quantity="+quantity+" "+
        					" WHERE isbn='"+order.getIsbn()+"';"+ 
        					" COMMIT;";
    				Connect.exeUpdate(updateStmt2);       
    			} else {
    				flag=2;
    				return flag;                       	  
    			}
    		} else {
    			Connect.exeUpdate(updateStmt1);           
    			quantity = quantity+currentQuantity;
    			String updateStmt4 =                             
    				    " BEGIN;"+
    				    " UPDATE alertinfo SET isvalid="+0+" "+
    				    " WHERE isbn = '"+order.getIsbn()+"'; " +
    				    " COMMIT;" ;
    			if(quantity >= lowerlimit){
    				Connect.exeUpdate(updateStmt4);
    			}
    			String updateStmt2 =
    					" BEGIN;"+
    					" UPDATE book SET quantity="+quantity+" "+
    					" WHERE isbn='"+order.getIsbn()+"';"+ 
    					" COMMIT;";
    			Connect.exeUpdate(updateStmt2);         
    		}
    	} 
    	return flag;
    }
    
    public static float getAmountByYearAndMonth(String year,String month,int sign) throws SQLException{
        ResultSet rs = Connect.exeQuery("SELECT sum(amount) monthamount FROM orderinfo WHERE date LIKE '"+year+"-"+month+"-%' AND sign="+sign+" ORDER BY ORDERID DESC;");
		if(rs.next())
			return rs.getFloat("monthamount");
        return 0;

    }
    
    public static Book checkIsbn(String isbn) throws SQLException{
    	String selectStmt = "SELECT * FROM BOOK WHERE ISBN='"+isbn+"';";
    	ResultSet rs = Connect.exeQuery(selectStmt);
    	if(rs.next()){
    		Book book = new Book();
    		book.setTitle(rs.getString("TITLE"));
    		book.setQuantity(rs.getInt("QUANTITY"));
    		return book;
    	}
		return null;
    }
    
    public static String checkSnpid(int id) throws SQLException{
    	String firm=null;
    	String selectStmt = "SELECT * FROM SNP WHERE SNPID="+id+";";
    	ResultSet rs = Connect.exeQuery(selectStmt);
    	if(rs.next())firm = rs.getString("NAME");
    	return firm;
    }
}