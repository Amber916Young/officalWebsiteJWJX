package com.web.utils.database;

public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder =new ThreadLocal<>();
    public static void setDbType(String dbType){
        contextHolder.set(dbType);
    }

    public static String getDbType(){
        return ((String)contextHolder.get());
    }
    public static void clearDbType(){
        contextHolder.remove();
    }
}
