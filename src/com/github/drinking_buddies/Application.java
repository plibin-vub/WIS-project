package com.github.drinking_buddies;

import static com.github.drinking_buddies.jooq.Tables.*;
import static org.jooq.impl.DSL.count;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.github.drinking_buddies.config.Configuration;
import com.github.drinking_buddies.config.Database;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Image;
import com.github.drinking_buddies.entities.Tag;
import com.github.drinking_buddies.jooq.Tables;
import com.github.drinking_buddies.ui.BeerForm;
import com.github.drinking_buddies.ui.utils.EncodingUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WXmlLocalizedStrings;

public class Application extends WApplication {
    private ServletContext servletContext;
    private Configuration configuration;
    
    public Application(WEnvironment env, ServletContext servletContext) {
        super(env);
        
        this.servletContext = servletContext;
        
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

        XStream xstream = new XStream(new DomDriver()); 
        xstream.alias("configuration", Configuration.class);
        xstream.alias("database", Database.class);
        //TODO: allow for the configuration of this file location
        configuration = (Configuration) xstream.fromXML(new File("./drinking-buddies-config.xml"));
        System.err.println(configuration.getDatabase().getJdbcUrl());
        
        handleInternalPath(getInternalPath());
    }
    
    //TODO remove
    String data_uri = "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";
    
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
        
        if (parts.length == 0) {
            getRoot().addWidget(new WText("Tata: the drinking buddies main page!"));
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
                
                String beerName = null;
                String beerWSName = null;
                int favoredBy = 0;
                
                Connection conn = null;
                try {
                    conn = this.getConnection();
                    DSLContext dsl = createDSLContext(conn);
                    
                    Integer id = null;
                    
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
                            .where(FAVORITE_BEER.ID_BEER.equal(id))
                            .fetchCount();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    closeConnection(conn);
                }
                
                Beer b = new Beer(beerName, "Westmalle klooster", favoredBy, 4.5, i);
                final List<Tag> tags = new ArrayList<Tag>();
                tags.add(new Tag("belgian"));
                tags.add(new Tag("9deg"));
                tags.add(new Tag("fermented in bottles"));
                getRoot().addWidget(new BeerForm(b, tags));
            } else {
                //show the beer selection form
            }
        } else {
            //show 404
        }
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
        return DriverManager.getConnection(db.getJdbcUrl(), db.getUserName(), db.getPassword());
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
}
