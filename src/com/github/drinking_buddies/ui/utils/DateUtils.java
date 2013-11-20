package com.github.drinking_buddies.ui.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    static DateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Date sqliteDateToJavaDate(String sqliteDate) {
        try {
            return sqliteDateFormat.parse(sqliteDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String javaDateToSqliteFormat(Date d) {
        return sqliteDateFormat.format(d);
    }
}
