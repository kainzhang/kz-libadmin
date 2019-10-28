package util;

import java.io.File;   
import jxl.*;   
import jxl.write.*;  
import jxl.write.biff.RowsExceededException;  
import java.sql.*;  
import java.util.*;  
  
public class excel {  
    public void WriteExcel(ResultSet rs, String filePath, String sheetName, Vector columnName) {  
        WritableWorkbook workbook = null;  
        WritableSheet sheet = null;  
        int rowNum = 1;
        try {  
            workbook = Workbook.createWorkbook(new File(filePath));
            sheet = workbook.createSheet(sheetName, 0); 
            this.writeCol(sheet, columnName, 0);
            while(rs.next()) {  
                Vector col = new Vector(); 
                for(int i = 1; i <= columnName.size(); i++) { 
                    col.add(rs.getString(i));  
                }  
                this.writeCol(sheet, col, rowNum++);  
            }  
              
        }catch(Exception e) {  
            e.printStackTrace();  
        }  
        finally {  
            try {  
                workbook.write();  
                workbook.close();  
                rs.close();  
            }catch(Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    private void writeCol(WritableSheet sheet, Vector col, int rowNum) throws RowsExceededException, WriteException {  
        int size = col.size(); 
        for(int i = 0; i < size; i++) { 
            Label label = new Label(i, rowNum, (String) col.get(i));   
            sheet.addCell(label);  
        }  
    }  
}  