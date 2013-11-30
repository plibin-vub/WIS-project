package com.github.drinking_buddies.jooq.utils;

import static com.github.drinking_buddies.jooq.Tables.BAR;
import static com.github.drinking_buddies.jooq.Tables.BEER;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.exception.InvalidResultException;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;

public class SearchUtils {   
    //will be null if:
    // - the beer does not exists
    // - the beer name is not complete (so there are multiple matches)
    public static String getBeerURL(String beerName) throws SQLException {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        try {
            Record1<String> b = 
                    dsl
                        .select(BEER.URL)
                        .from(BEER)
                        .where(BEER.NAME.equal(beerName))
                        .fetchOne();
            if (b == null)
                return null;
            else
                return b.value1();
        } catch (InvalidResultException ire) {
            return null;
        } finally {
            DBUtils.closeConnection(conn);
        }
    }
    
    //will be null if:
    // - the bar does not exists
    // - the bar name is not complete (so there are multiple matches)
    public static String getBarURL(String barName) throws SQLException {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        try {
            Record1<String> b = 
                    dsl
                        .select(BAR.URL)
                        .from(BAR)
                        .where(BAR.NAME.equal(barName))
                        .fetchOne();
            if (b == null)
                return null;
            else
                return b.value1();
        } catch (InvalidResultException ire) {
            return null;
        } finally {
            app.closeConnection(conn);
        }
    }
}
