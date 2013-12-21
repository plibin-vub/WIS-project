package com.github.drinking_buddies.ui.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Date utility functions
public class DateUtils {
    static DateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    //convert sqlite date (which is just a string) to a Java date
    public static Date sqliteDateToJavaDate(String sqliteDate) {
        try {
            return sqliteDateFormat.parse(sqliteDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    //convert a Java date to a sqlite date (which is just a string)
    public static String javaDateToSqliteFormat(Date d) {
        return sqliteDateFormat.format(d);
    }
}
