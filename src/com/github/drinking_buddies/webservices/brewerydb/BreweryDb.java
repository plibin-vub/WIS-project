package com.github.drinking_buddies.webservices.brewerydb;

import static com.github.drinking_buddies.jooq.Tables.BEER;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URIUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.github.drinking_buddies.Main;
import com.github.drinking_buddies.config.Database;
import com.github.drinking_buddies.webservices.rest.RestRequest;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;
import com.google.gson.Gson;

public class BreweryDb {

    private static final String API_KEY = "75cecd79b3bd1fdf0e36baab9c696004";
    private static final String HOST = "api.brewerydb.com";
    private static final String BEERS_PATH = "/v2/beers";

    public static void main(String[] args) throws RestException, SQLException {
        loadBeersToDb();
    }

    public static Beer getBeer(String id) throws RestException {
        URI url;
        try {
            url = URIUtils.createURI("http", HOST, 80, BEERS_PATH, "key="
                    + API_KEY + "&ids=" + id, null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("id is not valid", e);
        }
        System.out.println(url.toString());
        String jsonBeer = RestRequest.makeRequest(url.toString());
        Gson gson = new Gson();
        BeerResults beerResults = gson.fromJson(jsonBeer, BeerResults.class);
        return beerResults.getData().get(0);
    }

    public static List<BeerResults> getAllBeers() throws RestException {
        URI url;
        ArrayList<BeerResults> result = new ArrayList<BeerResults>();
        BeerResults beerResults;
        for (int i = 1; i < 9; i++) {
            int page =1;
            do {
                try {
                    url = URIUtils.createURI("http", HOST, 80, BEERS_PATH,
                            "key=" + API_KEY + "&availableId=" + i+"&p="+page, null);
                    System.out.println(url);
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("id is not valid", e);
                }
                String jsonBeer = RestRequest.makeRequest(url.toString());
                Gson gson = new Gson();
                beerResults = gson.fromJson(jsonBeer, BeerResults.class);
                result.add(beerResults);
                page++;
            } while (beerResults.getCurrentPage() != beerResults
                    .getNumberOfPages() && beerResults.getNumberOfPages()!=0);
        }
        return result;
    }
    
    public static void loadBeersToDb() throws RestException, SQLException{
//        BreweryDb b = new BreweryDb();
        List<BeerResults> results = getAllBeers();
        int totalskipped=0;
        Connection conn=getConnection();
        DSLContext dsl = createDSLContext(conn);
        for (BeerResults beerResults : results) {
            if (beerResults.getData()!=null) {
                List<Beer> beers = beerResults.getData();
                for (Beer beer : beers) {
                    boolean success=(addBeerToDb(dsl,beer));
                    if(!success){
                        System.out.println(beer.getName());
                        totalskipped++;
                    }
                }
            }
        }
        conn.commit();
        conn.close();
    }
    
    
    private static boolean addBeerToDb(DSLContext dsl,Beer beer) {
        String beerURL=beer.getName().toLowerCase().replace(" ", "_");
        Record r 
        = dsl
            .select(BEER.WEBSERVICE_NAME)
            .from(BEER)
            .where(BEER.URL.eq(beerURL))
            .fetchAny();
        if(r!=null){
            return false;
        }
        r 
        = dsl
            .select(BEER.ID)
            .from(BEER)
            .orderBy(BEER.ID.desc())
            .fetchAny();
        Integer id=1;
        if(r!=null){
            id=r.getValue(BEER.ID)+1;
        }
        r 
        = dsl
            .select(BEER.NAME)
            .from(BEER)
            .where(BEER.WEBSERVICE_NAME.eq(beer.getId()))
            .fetchAny();
        if(r!=null){
            //TODO update
        }else{
            dsl.insertInto(BEER,BEER.ID,BEER.NAME,BEER.URL,BEER.WEBSERVICE_NAME)
            .values(id,beer.getName(),beerURL,beer.getId()).execute();
        }
        
        return true;
    }

    public static Connection getConnection() throws SQLException {
        Database db = Main.loadConfiguration().getDatabase();
        
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
    
    public static DSLContext createDSLContext(Connection conn) {
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
}
