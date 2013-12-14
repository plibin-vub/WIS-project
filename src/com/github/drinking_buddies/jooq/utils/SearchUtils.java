package com.github.drinking_buddies.jooq.utils;

import static com.github.drinking_buddies.jooq.Tables.BAR;
import static com.github.drinking_buddies.jooq.Tables.BAR2_BAR_COMMENT;
import static com.github.drinking_buddies.jooq.Tables.BAR_COMMENT;
import static com.github.drinking_buddies.jooq.Tables.BEER;
import static com.github.drinking_buddies.jooq.Tables.USER;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.exception.InvalidResultException;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.utils.DateUtils;

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
    
    public static Integer getBeerId(String beerName) throws SQLException {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        try {
            Record1<Integer> b = 
                    dsl
                        .select(BEER.ID)
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
    
    public static List<Comment> getBarComments(DSLContext dsl, int barId) {
        List<Comment> comments = new ArrayList<Comment>();
            Result<Record> commentResults 
            = dsl
                .select()
                .from(BAR_COMMENT,BAR2_BAR_COMMENT)
                .where(BAR2_BAR_COMMENT.BAR_ID.equal(barId)).and(BAR2_BAR_COMMENT.BAR_COMMENT_ID.eq(BAR_COMMENT.ID))
                .orderBy(BAR_COMMENT.TIMESTAMP, BAR_COMMENT.ID)
                .fetch();
        
        for (Record comment : commentResults) {
            String text = comment.getValue(BAR_COMMENT.TEXT);
            Date postDate = DateUtils.sqliteDateToJavaDate(comment.getValue(BAR_COMMENT.TIMESTAMP));
            
            Record userRecord
            = dsl
                .select()
                .from(USER)
                .where(USER.ID.eq(comment.getValue(BAR_COMMENT.USER_ID)))
                .fetchOne();   
            User poster = new User(userRecord);
            
            comments.add(new Comment(text, poster, postDate));
        }
        return comments;
    }
}
