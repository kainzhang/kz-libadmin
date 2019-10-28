package alertinfo;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Connect;

public class AlertInfoDAO {

    public static ObservableList<AlertInfo> showAlertInfo () throws SQLException {
        String selectStmt = "SELECT * FROM alertinfo ORDER BY AID DESC";
        try {
            ResultSet rsAlertInfo = Connect.exeQuery(selectStmt);
            ObservableList<AlertInfo> alertInfoList = getAlertInfoList(rsAlertInfo);
            return alertInfoList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }
    
    private static ObservableList<AlertInfo> getAlertInfoList(ResultSet rs) throws SQLException {
        ObservableList<AlertInfo> alertInfoList = FXCollections.observableArrayList();
        while (rs.next()) {
            AlertInfo ainfo = new AlertInfo();
            ainfo.setAid(rs.getInt("AID"));
            ainfo.setIsbn(rs.getString("ISBN"));
            ainfo.setDetail(rs.getString("DETAIL"));
            ainfo.setDate(rs.getString("DATE"));
            ainfo.setIsvalid(rs.getInt("ISVALID"));
            alertInfoList.add(ainfo);
        }
        return alertInfoList;
    }

    public static void insertAlertInfo (String isbn, String detail) throws SQLException {
    	String updateStmt =
			" BEGIN;" +
	        " INSERT INTO alertinfo" +
	        " (AID, ISBN, DETAIL, DATE, ISVALID)" +
	        " VALUES" +
	        " (null, '"+isbn+"', '"+detail+"', datetime('now', 'localtime'), "+1+");" +
	        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
    
    public static void changeAlertInfoState(int aid) throws SQLException{
    	String updateStmt =
    			" BEGIN;" +
		        " UPDATE alertinfo SET isvalid="+0+", " +
		        " WHERE aid="+aid+", " +
		        " COMMIT;";
        try {
            Connect.exeUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
    
    public static int getAlertQuantity() throws SQLException{
    	String selectStmt = " SELECT count(*) cou FROM alertinfo; ";
    	ResultSet rs = Connect.exeQuery(selectStmt);
    	if(rs.next())return rs.getInt("cou");
    	return 0;
    }
}