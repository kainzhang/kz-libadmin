package book;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Connect;

public class BookDAO {
	public static int actionMark=1,cid;
    public static String isbn,title,author,press,pdate,quantity,pricing,lowerlimit;
	
    public static ObservableList<Book> searchBooks (String info,boolean mark) throws SQLException, ClassNotFoundException {
    	String query;
    	if(mark) query = "SELECT b.*,c.cname FROM book b, category c WHERE b.cid=c.cid AND b.isbn='"+info+"';";       // search by ISBN
    	
    	else query = "SELECT b.*,c.cname FROM book b, category c WHERE (title LIKE '%"+info+"%' OR b.author LIKE '%"+info+"%' OR b.press LIKE '%"+info+"%' OR c.cname LIKE '%"+info+"%' )AND  b.cid=c.cid;";    // search by keyword
        try {
        	ResultSet rsSearchBooks = Connect.exeQuery(query);
            ObservableList<Book>searchBookList = getBookList(rsSearchBooks);
            return searchBookList;
        } catch (SQLException e) {
            System.out.println("While searching a book with '" + info + "', an error occurred: " + e);
            throw e;
        }
    }

    public static ObservableList<Book> showBooks () throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT b.*,c.cname FROM book b, category c WHERE b.cid=c.cid";
        try {
            ResultSet rsBooks = Connect.exeQuery(selectStmt);
            ObservableList<Book> bookList = getBookList(rsBooks);
            return bookList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    
    private static ObservableList<Book> getBookList(ResultSet rs) throws SQLException {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        while (rs.next()) {
            Book book = new Book();
            book.setIsbn(rs.getString("ISBN"));
            book.setTitle(rs.getString("TITLE"));
            book.setAuthor(rs.getString("AUTHOR"));
            book.setPress(rs.getString("PRESS"));
            book.setCid(rs.getString("cname"));
            book.setPdate(rs.getString("PDATE"));
            book.setQuantity(rs.getInt("QUANTITY"));
            book.setPricing(rs.getFloat("PRICING"));
            book.setLowerlimit(rs.getInt("LOWERLIMIT"));
            bookList.add(book);
        }
        return bookList;
    }

    public static void deleteBookByIsbn (String isbn) throws SQLException {
        String updateStmt =
        				" BEGIN;"+
                        " DELETE FROM book" +
                        " WHERE isbn='"+isbn+"';" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public static void insertBook (Book book) throws SQLException, ClassNotFoundException {
    	String selectStmt = " SELECT * FROM BOOK WHERE isbn='"+book.getIsbn()+"';";
    	ResultSet rs= Connect.exeQuery(selectStmt);
    	if(rs.next())updateBook(book);
    	else {
	    	String updateStmt =
	        				" BEGIN;" +
	                        " INSERT INTO book" +
	                        " (ISBN,TITLE,AUTHOR,PRESS,CID,PDATE,QUANTITY,PRICING,LOWERLIMIT)" +
	                        " VALUES" +
	                        " ('"+book.getIsbn()+"', '"+book.getTitle()+"','"+book.getAuthor()+"', "+
	                        " '"+book.getPress()+"', "+Integer.parseInt(book.getCid())+", '"+book.getPdate()+"', " +
	                        " "+book.getQuantity()+", "+book.getPricing()+","+book.getLlowerlimit()+");" +
	                        " COMMIT;";
	        try {
	            Connect.exeUpdate(updateStmt);
	        } catch (SQLException e) {
	            System.out.print("Error occurred while INSERT Operation: " + e);
	            throw e;
	        }
    	}
    }
    
    public static void updateBook (Book book) throws SQLException, ClassNotFoundException {
    	String updateStmt =
        				" BEGIN;" +
                        " UPDATE book" +
                        " SET " +
                        " title='"+book.getTitle()+"', author='"+book.getAuthor()+"', "+
                        " press='"+book.getPress()+"', cid="+book.getCid()+", pdate='"+book.getPdate()+"', " +
                        " quantity="+book.getQuantity()+", pricing="+book.getPricing()+", lowerlimit="+book.getLlowerlimit()+" " +
                        " WHERE isbn='"+book.getIsbn()+"';" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
    
    
    public static void setBook(ReadOnlyObjectProperty<Book> book) throws SQLException {
    	ReadOnlyObjectProperty<Book> selectedBook = book;
    	isbn=selectedBook.getValue().getIsbn();
		title=selectedBook.getValue().getTitle();
		author=selectedBook.getValue().getAuthor();
		press=selectedBook.getValue().getPress();
		String select = "SELECT cid FROM category WHERE CNAME='"+selectedBook.getValue().getCid()+"';";
		ResultSet rs = Connect.exeQuery(select);
		if(rs.next())cid=rs.getInt("CID");
		pdate=selectedBook.getValue().getPdate();
		quantity=selectedBook.getValue().getQuantity().toString();
		pricing=selectedBook.getValue().getPricing().toString();
		lowerlimit=selectedBook.getValue().getLlowerlimit().toString();
    }
    
    public static void clearBook() {
    	isbn="";
    	title="";
    	author="";
    	press="";
    	cid=0;
    	pdate="";
    	quantity="";
    	pricing="";
    	lowerlimit="";
    }
}