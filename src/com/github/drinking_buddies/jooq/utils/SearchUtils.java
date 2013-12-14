package com.github.drinking_buddies.jooq.utils;

import static com.github.drinking_buddies.jooq.Tables.BAR;
import static com.github.drinking_buddies.jooq.Tables.BAR2_BAR_COMMENT;
import static com.github.drinking_buddies.jooq.Tables.BAR_COMMENT;
import static com.github.drinking_buddies.jooq.Tables.BEER;
import static com.github.drinking_buddies.jooq.Tables.USER;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.exception.InvalidResultException;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.geolocation.GeoLocation;
import com.github.drinking_buddies.ui.utils.DateUtils;

import eu.webtoolkit.jwt.WGoogleMap.Coordinate;

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
    
    public static Integer getClosesedBarId(double lat, double lng) {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        double radius = 1;
        try {
            GeoLocation location=GeoLocation.fromDegrees(lat, lng);
            GeoLocation[] boundingCoordinates=location.boundingCoordinates(radius, GeoLocation.RADIUS_EARTH);
            Coordinate rightBottom=new Coordinate(boundingCoordinates[1].getLatitudeInDegrees(),boundingCoordinates[1].getLongitudeInDegrees());
            Coordinate topLeft=new Coordinate(boundingCoordinates[0].getLatitudeInDegrees(),boundingCoordinates[0].getLongitudeInDegrees());
            Condition condition1=BAR.LOCATION_X.ge(new Float(boundingCoordinates[0].getLatitudeInRadians())).and(BAR.LOCATION_X.le(new Float(boundingCoordinates[1].getLatitudeInRadians())));
            Condition condition2;
            if(boundingCoordinates[0].getLongitudeInRadians() >
            boundingCoordinates[1].getLongitudeInRadians()){
                condition2=BAR.LOCATION_Y.ge(new Float(boundingCoordinates[0].getLatitudeInRadians())).or(BAR.LOCATION_Y.le(new Float(boundingCoordinates[1].getLatitudeInRadians())));
            }else{
                condition2=BAR.LOCATION_Y.ge(new Float(boundingCoordinates[0].getLongitudeInRadians())).and(BAR.LOCATION_Y.le(new Float(boundingCoordinates[1].getLongitudeInRadians())));
            }
            Condition condition3=BAR.LOCATION_X.sin().multiply(Math.sin(location.getLatitudeInRadians()))
                    .plus(BAR.LOCATION_X.cos().multiply(Math.cos(location.getLatitudeInRadians())).
                            multiply(BAR.LOCATION_Y.subtract(location.getLongitudeInRadians()).cos())).le(new BigDecimal(Math.cos(radius / GeoLocation.RADIUS_EARTH)));

            Record1<Integer> r = dsl.select(BAR.ID)
                    .from(BAR)
                    .where(condition1.and(condition2).and(condition3))
                    .fetchAny();
            if(r!=null){
               return r.getValue(BAR.ID);
            }else{
               return null;
            }
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
            
    }
}
