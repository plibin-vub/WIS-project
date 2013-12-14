package com.github.drinking_buddies.webservices.servlet;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.github.drinking_buddies.config.Configuration;
import com.github.drinking_buddies.config.Database;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Address;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.jooq.queries.BarQueries;
import com.github.drinking_buddies.jooq.utils.SearchUtils;
import com.github.drinking_buddies.ui.utils.EncodingUtils;
import com.google.gson.Gson;


public class BarsRestServlet extends RestServlet {
    private Configuration configuration;
    
    public BarsRestServlet() {
        super();
    }

    @Override
    public void streamOutput(OutputStream os, Format format, String pathInfo) throws IOException {
        if (format != Format.JSON) {
            appendUTF8(os, "Error: we only support the JSON output format!");
        }
        
        String[] pathParts = pathInfo.substring(1).split("/");
        if (pathParts.length == 1 && pathParts[0].equals("bars")) {
            streamBars(os, format);
        } else if (pathParts.length == 2 && pathParts[0].equals("bars")) {
            String barUrl = pathParts[1];
            streamBar(os, format, barUrl);
        } else {
            appendUTF8(os, "Error: don't know how to fetch for the path: \"" + pathInfo + "\"");
        }
    }
    
    private static class SimpleBar {
        public String url;
        public String name;
    }
    private void streamBars(OutputStream os, Format format) {
        Connection conn = null;
        try {
            try {
                conn = DBUtils.getConnection();
                DSLContext dsl = DBUtils.createDSLContext(conn);
                
                Result<Record2<String, String>> results 
                    = dsl
                        .select(BAR.URL, BAR.NAME)
                        .from(BAR)
                        .orderBy(BAR.URL)
                        .fetch();
                
                Gson gson = new Gson();
                appendUTF8(os, "{\"bars\": \n [ \n");
                for (Record2<String, String> r : results) {
                    SimpleBar b = new SimpleBar();
                    b.name = r.getValue(BAR.NAME);
                    b.url = r.getValue(BAR.URL);
                    appendUTF8(os, "  " + gson.toJson(b) + "\n");
                }
                appendUTF8(os, " ]\n}");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null)
                    conn.close();
            } 
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }

    private static class Image {
        public Image(byte[] data, String mimeType) {
            this.uri = new DataURI(EncodingUtils.byteArrayToBase64(data));
            this.mimeType = mimeType;
        }
        
        private DataURI uri;
        private String mimeType;
    }
    private static class DataURI {
        public DataURI(String base64Data) {
            uri = "data:image/png;base64," + base64Data;
        }
        private String uri;
    }
    private static class FullBar {
        private String url;
        private String name;
        private String webSite;
        private String averageScore;
        private Address address;
        private Image image;
        private List<Comment> comments = new ArrayList<Comment>();
    }
    private void streamBar(OutputStream os, Format format, String barURL) {
        Connection conn = null;
        try {
            try {
                conn = DBUtils.getConnection();
                DSLContext dsl = DBUtils.createDSLContext(conn);
                
                Record r 
                    = dsl
                        .select(BAR.ID,BAR.NAME,BAR.URL,BAR.WEBSITE,BAR.PHOTO, BAR.PHOTO_MIME_TYPE, ADDRESS.ID,ADDRESS.STREET,ADDRESS.NUMBER,ADDRESS.ZIPCODE,ADDRESS.CITY,ADDRESS.COUNTRY)
                        .from(BAR,ADDRESS)
                        .where(BAR.URL.eq(barURL))
                        .and(ADDRESS.ID.eq(BAR.ADDRESS_ID))
                        .fetchOne();
                
                if (r == null) {
                    appendUTF8(os, "Error: could not fetch a bar with URL \"" + barURL + "\"");
                    return;
                }
                
                Address address=new Address(r.getValue(ADDRESS.ID), r.getValue(ADDRESS.STREET), r.getValue(ADDRESS.NUMBER)
                        , r.getValue(ADDRESS.ZIPCODE), r.getValue(ADDRESS.CITY), r.getValue(ADDRESS.COUNTRY));
                
                FullBar fb = new FullBar();
                fb.name = r.getValue(BAR.NAME);
                fb.url = r.getValue(BAR.URL);
                fb.address = address;
                fb.webSite = r.getValue(BAR.WEBSITE);
                fb.averageScore = formatScore(BarQueries.getAvgScore(dsl, r.getValue(BAR.ID)).doubleValue());
                fb.comments = SearchUtils.getBarComments(dsl, r.getValue(BAR.ID));
                fb.image = new Image(r.getValue(BAR.PHOTO), r.getValue(BAR.PHOTO_MIME_TYPE));
                
                Gson gson = new Gson();
                appendUTF8(os, gson.toJson(fb));
                
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null)
                    conn.close();
            } 
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    private static final DecimalFormat formatter = new DecimalFormat("#.0"); 
    private String formatScore(double score) {
        return formatter.format(score);
    }
    
    private void appendUTF8(OutputStream os, String s) throws UnsupportedEncodingException, IOException {
        os.write(s.getBytes("UTF-8"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        this.configuration = Configuration.loadConfiguration();
    }
}
