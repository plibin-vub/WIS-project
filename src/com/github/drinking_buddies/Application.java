package com.github.drinking_buddies;

import static com.github.drinking_buddies.jooq.Tables.BAR2_BAR_SCORE;
import static com.github.drinking_buddies.jooq.Tables.BAR_SCORE;
import static com.github.drinking_buddies.jooq.Tables.BEER;
import static com.github.drinking_buddies.jooq.Tables.BEER2_BEER_TAG;
import static com.github.drinking_buddies.jooq.Tables.BEER_TAG;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BEER;
import static com.github.drinking_buddies.jooq.Tables.REVIEW;
import static com.github.drinking_buddies.jooq.Tables.USER;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BAR;
import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.github.drinking_buddies.config.Configuration;
import com.github.drinking_buddies.config.Database;
import com.github.drinking_buddies.entities.Address;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Image;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.entities.Tag;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.BarForm;
import com.github.drinking_buddies.ui.BeerForm;
import com.github.drinking_buddies.ui.StartForm;
import com.github.drinking_buddies.ui.UserForm;
import com.github.drinking_buddies.ui.utils.EncodingUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBootstrapTheme;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WXmlLocalizedStrings;

public class Application extends WApplication {
    private ServletContext servletContext;
    private Configuration configuration;
    
    private Integer loggedInUserId;

    
    public Application(WEnvironment env, ServletContext servletContext) {
        super(env);
        
        this.servletContext = servletContext;
        
        setTheme(new WBootstrapTheme());
        
        WXmlLocalizedStrings resources = new WXmlLocalizedStrings();
        resources.use("/com/github/drinking_buddies/ui/templates");
        resources.use("/com/github/drinking_buddies/i18n/resources");
        this.setLocalizedStrings(resources);
        
        setTitle(tr("app.title"));
        
        this.internalPathChanged().addListener(this, new Signal1.Listener<String>(){
            @Override
            public void trigger(String internalPath) {
                handleInternalPath(internalPath);
            }            
        });

        configuration = Main.loadConfiguration();

        handleInternalPath(getInternalPath());
    }
    
    //TODO remove
    String data_uri = "R0lGODlhLAA2APcAAE1RVFNSVVVVWllVW1tWVVVZXFtaXFVbYVxcYWJcXGFdYmtfXF1gXl1hZGJhZGNhXXJiXGpjY2RkaXNkY2llamppbGtpZHNpZHtqZGxscXNsa3xsanJuc3JxdHtya4NyZYJza3R0eXp0c3l2eoN2dHp5fHt5dIJ5a4N6c4x7a318gYJ8fJB8a4p9eoF+goKBfYOChIqEg4aFiZOFfZiFdIuJhZiJdYuKjJSKfZSLg46MkJqMfJKNi5qNg5COkpSRjJyRhJSSlJyTi6GThJaVmJqVk6GVjJiWmZyZlKOZjZuam6Oak6majaKdm6qdlKCeoaqemaaglayhlaSipKujmqqlorGmoayppKyqq7Oro7msnbKtq66xsrSxrLWxpruxpbSys7uzq7q1s7i2uMG2sby5s8O6rby7u8O8s8m8rsK+u8m+s8XAtcTCu8rCtsTDwsvEu8rGwdDGvM3IvczKw9LKvtXNw9XOyNjOxdjOyNbQxdbRyNnSxtrTyeDWyt3YzOHZzAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAQAAAAALAAAAAAsADYAAAj/APn0GShQ4MCDfATa4fOnzx47Dgse1GPwYJ+EFwla1GgxYUWCHjdeXJiRI0WHHTHawWOHYsWGFz1iHAjR452PBV1ixLiHZkyIKWf2sQN0ZsiORDVKvJgDDZA0dfhQxONwJVWBeuBIQVOQz8KlQjNWlKEigwEKQpJoWSOlx44ZLGjsEFIDQYg3JVPGTFhzYJ6BTQyQeUMHjBIlPmBIcODBhIw3ZzqEgJNXrEGDePR8dXgzToYNd/Tc6bnnoR06Z7jooeNgy0eQfHrKHKjH4h4+d8AYCFNbpMooITZvjG3Uox48xeFIKKI5r0w7Gpp0lbp0r0yXD2naWZGjdEadUtEY/7hSkeFOhH1OCsRzteCeDksS6unp8iKeLAKKSAybsKdGO3d8dZtXcDiQRR119GaHFkbY4YYccDQQhB154IRQeQaRNJUdWRjAgAMfzNCDEFA40MAECDyQggA/WMdRTLLtJBFFdrzQARFKTIEFGFOAAUYbNyjRhA4skiRUdTHddNEeFQ7U4RQY1YbVHprdAccDP3jUkEDZ7USUVwu1RBAaFoRAB3gV6aEZHAYgkRBy3130B0wlIcflSi0YwJVSHtkxIB0GPDHcUX2w51WhxMlnhgE38AFnTHvFBAcBSqTkH6QcXeaVCB0ABZKY8iXEhgCNknTbpWIhWhJuTowHUVJ+Fv+6mVdhFNDBbX2hdyiXGeE6QgZ0tDSgV7ghCmYYAgQxrFG2naeQFQZMoZlXjyrUkkdlsKgTbLNdGJsdMUiw0EkZIRcjRXrEgcAKUw0lY6oReVSHBjDMx+x6MsUGBwIOzKGZn8SqpNOyYUR73W2R4mEaUSMYoAZJ6S353K4eXWEAFuhqhm5XRDW3nQNqOFqupgRBXCgWesIJpnoujctSBSXc1N5lCFdW2xQCUEHVSF9ubCdFcCiwxUP5goTpRH1kW4NsPUE04FdeadaFAFvUdDBfAV+GBx0NWHBmbAgRZeS/JBTw8HEhqZmpjH7ocYMAKIRJ07tS4eGFATJATBKol7r/ZxwcBxBggxlirwT1RZqJYIAYX8o6EML+9Wm0HVgUYEACHxBu5G2afTFBACpApCRyRoINW0zs+XlGECocYAAEClzQwxA9gACCAgI0QESw3fZp+kBbZhiTmmcooUMQEhggQAUVDCCDEmAgx7KdRzGLnmV+ElVCAArA4aeaRlrXbWVGX3THSCfpUQIAndJXH0F72En+i+dpJJseMAgAQ5jgU3ehlOmpjlQ68hD1IG4EAlCCydS0Mc3sjDbzsc+LRmK6gtihAovDDs9uwxL5Xa8/qyqZmoplh4IhIAzwA9ubBmicB6JkLzE6HOJukIEKXME6VivP42gElPspxFn2sUMV5WQgASEMBWpLedTwHocS4lxIQ3iQwgkMUIAAcABgF4rUZSACCEj5RzYEYcIMeIADByCgAzeg4REkQIU6jMZLHINUVzRir4tEaABnUcIbiEKFIthhDDDQABTo4CiMkI6JEXEOr/iiBBnIwAUZiAAGbJAABCygBDeogK1+4AQAvgZG0ynPVRh4mjiMAQtECMJhgsDKDDgADtRD5JIQdqTTAe9FYSoN0S4TL4LUkY6xkshKPjgt58jxO58cyQt16EteYiWEvfFWMi1Sm/N1BHv2kUrG8PCXQnlTJOAMpxa/OLyV8PIiAQEAOw==";
    
    private String[] split(String internalPath) {
        //remove the first slash: 
        //-if the internalpath is empty the array will be empty
        //-if the internalpath is not empty the array will contain all words in the path
        internalPath = internalPath.substring(1);
        return internalPath.split("/");
    }
    
    private void handleInternalPath(String ip) {
        String[] parts = split(ip);
        
        getRoot().clear();
        
        if ("".equals(parts[0])) {
            getRoot().addWidget(new StartForm());
            return;
        }
            
        if ("beers".equals(parts[0])) {
            if (parts.length > 1) {
                Image i = null;
                try {
                    i = new Image(EncodingUtils.base64ToByteArray(data_uri), "image/gif");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                final String beerURL = parts[1];
                
                Integer id = null;
                String beerName = null;
                String beerWSName = null;
                Integer favoredBy = null;
                List<Tag> tags = new ArrayList<Tag>();
                List<Review> reviews = new ArrayList<Review>();
                
                Connection conn = null;
                try {
                    conn = this.getConnection();
                    DSLContext dsl = createDSLContext(conn);
                    
                    Record r 
                        = dsl
                            .select(BEER.ID, BEER.NAME, BEER.WEBSERVICE_NAME)
                            .from(BEER)
                            .where(BEER.URL.eq(beerURL))
                            .fetchOne();
                    id = r.getValue(BEER.ID);
                    beerName = r.getValue(BEER.NAME);
                    beerWSName = r.getValue(BEER.WEBSERVICE_NAME);
                    
                    favoredBy
                        = dsl
                            .select()
                            .from(FAVORITE_BEER)
                            .where(FAVORITE_BEER.BEER_ID.equal(id))
                            .fetchCount();
                    
                    Result<Record2<Integer, String>> results 
                        = dsl
                            .select(BEER_TAG.ID, BEER_TAG.NAME)
                            .from(BEER_TAG)
                            .join(BEER2_BEER_TAG)
                            .on(BEER_TAG.ID.equal(BEER2_BEER_TAG.BEER_TAG_ID))
                            .where(BEER2_BEER_TAG.BEER_ID.equal(id))
                            .orderBy(BEER_TAG.ID)
                            .fetch();
                    
                    for (Record2<Integer, String> r2 : results) {
                        tags.add(new Tag(r2.value1(), r2.value2()));
                    }
                    
                    Result<Record> reviewResults 
                    = dsl
                        .select()
                        .from(REVIEW)
                        .where(REVIEW.BEER_ID.equal(id))
                        .orderBy(REVIEW.POST_TIME)
                        .fetch();
                    
                    for (Record rr : reviewResults) {
                        Record ur = dsl.select().from(USER).where(USER.ID.equal(rr.getValue(REVIEW.ID))).fetchOne();
                        reviews.add(new Review(rr, ur));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    throw new RuntimeException(e);
                } finally {
                    closeConnection(conn);
                }
                
                Beer b = new Beer(id, beerName, "--TODO--", favoredBy, i);
                getRoot().addWidget(new BeerForm(b, tags, reviews));
            } else {
                //show the beer selection form
            }
        } else {
            //show 404
        }
        if ("users".equals(parts[0])) {
            Integer id = null;
            String firstName = null;
            String lastName = null;
                 
                 Connection conn = null;
                 try {
                     conn = this.getConnection();
                     DSLContext dsl = createDSLContext(conn);
                     
                     Record r 
                         = dsl
                             .select(USER.FIRST_NAME)
                             .from(USER)
                             .where(USER.ID.eq(1))
                             .fetchOne();
                     id = r.getValue(USER.ID);
                     firstName = r.getValue(USER.FIRST_NAME);
                     lastName = r.getValue(USER.LAST_NAME);
                     }
                  catch (Exception e) {
                     e.printStackTrace();
                     throw new RuntimeException(e);
                 } finally {
                     closeConnection(conn);
                 }

            User u = new User(id, firstName, lastName);
            getRoot().addWidget(new UserForm(u));
        } else {
            //show 404
        }
        if ("bars".equals(parts[0])) {
            if(parts.length<=1){
                Address address=new Address(5, "TestStraat", "10"
                      , "2800","Mechelen", "Belgie");
                Image barPhoto = null; //TODO
                Bar bar = new Bar(1,"TestBar",5, 6, "www.google.com",barPhoto, address) ;
                getRoot().addWidget(new BarForm(bar));
                show404();
                return;
            }
            final String barURL = parts[1];
              Bar bar=null;
                   
                   Connection conn = null;
                   try {
                       conn = this.getConnection();
                       DSLContext dsl = createDSLContext(conn);
                       
                       Record r 
                           = dsl
                               .select(BAR.ID,BAR.NAME,BAR.WEBSITE,BAR.PHOTO,ADDRESS.ID,ADDRESS.STREET,ADDRESS.NUMBER,ADDRESS.ZIPCODE,ADDRESS.CITY,ADDRESS.COUNTRY)
                               .from(BAR,ADDRESS)
                               .where(BAR.URL.eq(barURL))
                               .and(ADDRESS.ID.eq(BAR.ADDRESS_ID))
                               .fetchOne();
                       if(r==null){
                           show404();
                           return;
                       }
                       int id=r.getValue(BAR.ID);
                       int favoredBy
                       = dsl
                           .select()
                           .from(FAVORITE_BAR)
                           .where(FAVORITE_BAR.BAR_ID.equal(id))
                           .fetchCount();
                       BigDecimal score=dsl.select(BAR_SCORE.SCORE.avg())
                               .from(BAR_SCORE)
                               .join(BAR2_BAR_SCORE)
                               .on(BAR2_BAR_SCORE.BAR_SCORE_ID.equal(BAR_SCORE.ID))
                               .where(BAR2_BAR_SCORE.BAR_ID.eq(id))
                               .fetchOne().getValue(BAR_SCORE.SCORE.avg());
                       Address address=new Address(r.getValue(ADDRESS.ID), r.getValue(ADDRESS.STREET), r.getValue(ADDRESS.NUMBER)
                               , r.getValue(ADDRESS.ZIPCODE), r.getValue(ADDRESS.CITY), r.getValue(ADDRESS.COUNTRY));
                       Image barPhoto = null; //TODO
                       bar = new Bar(id,r.getValue(BAR.NAME),favoredBy, score.doubleValue(), r.getValue(BAR.WEBSITE),barPhoto, address) ;
                       }
                    catch (Exception e) {
                       e.printStackTrace();
                       throw new RuntimeException(e);
                   } finally {
                       closeConnection(conn);
                   }

//              Address address=new Address(5, "TestStraat", "10"
//                    , "2800","Mechelen", "Belgie");
//              Image barPhoto = null; //TODO
//              bar = new Bar(1,"TestBar",5, 6, "www.google.com",barPhoto, address) ;
              getRoot().addWidget(new BarForm(bar));
          } else {
              show404();
          }
    }
    
    
    private void show404() {
        // TODO Auto-generated method stub
        
    }

    public static Application getInstance() {
        return (Application)WApplication.getInstance();
    }
    
    public ServletContext getServletContext() {
        return servletContext;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Connection getConnection() throws SQLException {
        Database db = configuration.getDatabase();
        
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Connection conn = DriverManager.getConnection(db.getJdbcUrl(), db.getUserName(), db.getPassword());
        conn.setAutoCommit(false);
        return conn;
    }
    
    public void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public DSLContext createDSLContext(Connection conn) {
        return DSL.using(conn, SQLDialect.SQLITE);
    }

    public void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public Integer getLoggedInUserId() {
        return loggedInUserId;
    }
    
    public void login(Integer userId) {
        loggedInUserId = userId;
    }
}
