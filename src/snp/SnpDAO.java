package snp;

import java.sql.ResultSet;
import java.sql.SQLException;

import book.Book;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Connect;

public class SnpDAO {
	public static int actionMark=1;
	public static String id, name, owner, otitle, tel, addr, bank, bankid;
	
    public static ObservableList<Snp> searchSnpsByKeyword (String keyword,int sign) throws SQLException, ClassNotFoundException {
    	try {
            ResultSet rsSnp = Connect.exeQuery("SELECT * FROM snp WHERE "+
            		 						   "(name LIKE '%"+keyword+"%' "+
            		 						   " OR owner LIKE '%"+keyword+"%' "+
            		 						   " OR otitle LIKE '%"+keyword+"%') AND sign="+sign+";");
            ObservableList<Snp>snpList = getSnpList(rsSnp);
            return snpList;
        } catch (SQLException e) {
            System.out.println("While searching a snp with '" + name + "', an error occurred: " + e);
            throw e;
        }
    }


    public static ObservableList<Snp> showSnps (int sign) throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM snp WHERE sign="+sign+";";
        try {
            ResultSet rsSnps = Connect.exeQuery(selectStmt);
            ObservableList<Snp> snpList = getSnpList(rsSnps);
            return snpList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

    private static ObservableList<Snp> getSnpList(ResultSet rs) throws SQLException {
        ObservableList<Snp> snpList = FXCollections.observableArrayList();
        while (rs.next()) {
            Snp snp = new Snp();
            snp.setId(rs.getInt("SNPID"));
            snp.setName(rs.getString("NAME"));
            snp.setOwner(rs.getString("OWNER"));
            snp.setOtitle(rs.getString("OTITLE"));
            snp.setTel(rs.getString("TEL"));
            snp.setAddr(rs.getString("ADDR"));
            snp.setBank(rs.getString("BANK"));
            snp.setBankid(rs.getString("BANKID"));
            snp.setSign(rs.getInt("SIGN"));
            snpList.add(snp);
        }
        return snpList;
    }

    public static void deleteSnpById (int id) throws SQLException, ClassNotFoundException {
        String updateStmt =
        				" BEGIN;"+
                        " DELETE FROM snp" +
                        " WHERE SNPID="+id+";" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public static void insertSnp (Snp snp) throws SQLException, ClassNotFoundException {
    	String selectStmt = "SELECT * FROM snp WHERE SNPID='"+snp.getId()+"';";
    	ResultSet rs = Connect.exeQuery(selectStmt);
    	if(rs.next()){
    		updateSnp(snp);
    	} else {
    		String updateStmt =
    				" BEGIN;"+
                    " INSERT INTO snp" +
                    " (SNPID, NAME, OWNER, OTITLE, TEL, ADDR, BANK, BANKID, SIGN)" +
                    " VALUES" +
                    " (null,'"+snp.getName()+"', '"+snp.getOwner()+"','"+snp.getOtitle()+"', '"+snp.getTel()+"'," +
                    " '"+snp.getAddr()+"', '"+snp.getbank()+"', '"+snp.getBankid()+"', "+snp.getSign()+"); " +
                    " COMMIT;";
	
		    try {
		        Connect.exeUpdate(updateStmt);
		    } catch (SQLException e) {
		        System.out.print("Error occurred while INSERT Operation: " + e);
		        throw e;
		    }
    	}
    	
    }
    
    public static void updateSnp (Snp snp) throws SQLException, ClassNotFoundException {
    	String updateStmt =
        				" BEGIN;" +
                        " UPDATE snp" +
                        " SET " +
                        " NAME='"+snp.getName()+"', OWNER='"+snp.getOwner()+"', "+
                        " OTITLE='"+snp.getOtitle()+"', TEL='"+snp.getTel()+"', ADDR='"+snp.getAddr()+"',"+
                        " BANK='"+snp.getbank()+"', BANKID='"+snp.getBankid()+"' "+
                        " WHERE SNPID="+snp.getId()+";" +
                        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
    
    public static void setSnp(ReadOnlyObjectProperty<Snp> snp) {
    	ReadOnlyObjectProperty<Snp> selectedSnp = snp;
    	id=selectedSnp.getValue().getId().toString();
    	name=selectedSnp.getValue().getName();
    	owner=selectedSnp.getValue().getOwner();
    	otitle=selectedSnp.getValue().getOtitle();
    	tel=selectedSnp.getValue().getTel();
    	addr=selectedSnp.getValue().getAddr();
    	bank=selectedSnp.getValue().getbank();
    	bankid=selectedSnp.getValue().getBankid();
    }
    
    public static void clearSnp() {
    	id="";
    	name="";
    	owner="";
    	otitle="";
    	tel="";
    	addr="";
    	bank="";
    	bankid="";
    }
}
