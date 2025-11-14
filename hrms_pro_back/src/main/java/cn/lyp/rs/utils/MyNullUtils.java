package cn.lyp.rs.utils;

public class MyNullUtils {
    public static boolean isNull(String str){
        if(str == null || str.equals("") || str.equals("null") || str.equals("undefined")) {
            return true;
        }
        return false;
    }
}
