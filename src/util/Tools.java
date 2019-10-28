package util;

import java.lang.reflect.Method;

public class Tools {
	public static String getFieldValueByName(String fieldName, Object obj) {  
        try {    
            String firstLetter = fieldName.substring(0, 1).toUpperCase();    
            String getter = "get" + firstLetter + fieldName.substring(1);    
            Method method = obj.getClass().getMethod(getter, new Class[] {});    
            String value = (String) method.invoke(obj, new Object[] {});    
            return value;    
        } catch (Exception e) {     
            return null;    
        }    
    }  
	
//	public static boolean isE
}
