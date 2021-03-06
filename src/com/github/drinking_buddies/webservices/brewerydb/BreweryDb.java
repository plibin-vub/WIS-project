package com.github.drinking_buddies.webservices.brewerydb;

import static com.github.drinking_buddies.jooq.Tables.BEER;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.Configuration;

import org.apache.http.client.utils.URIUtils;
import org.jooq.DSLContext;
import org.jooq.Record;

import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.webservices.rest.RestRequest;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;
import com.google.gson.Gson;

//set of functions to interact with the brewery-db web service
public class BreweryDb {
    private static final String HOST = "api.brewerydb.com";
    private static final String BEERS_PATH = "/v2/beers";

    public static Beer getBeer(String apiKey, String id) throws RestException {
        URI url;
        try {
            url = URIUtils.createURI("http", HOST, 80, BEERS_PATH, "key="
                    + apiKey + "&ids=" + id + "&withBreweries=y", null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("id is not valid", e);
        }
        System.out.println(url.toString());
        String jsonBeer = RestRequest.makeRequest(url.toString());
        Gson gson = new Gson();
        BeerResults beerResults = gson.fromJson(jsonBeer, BeerResults.class);
        return beerResults.getData().get(0);
    }

    public static List<BeerResults> getAllBeers(String apiKey) throws RestException {
        URI url;
        ArrayList<BeerResults> result = new ArrayList<BeerResults>();
        BeerResults beerResults;
        for (int i = 1; i < 9; i++) {
            int page =1;
            do {
                try {
                    url = URIUtils.createURI("http", HOST, 80, BEERS_PATH,
                            "key=" + apiKey + "&availableId=" + i+"&p="+page, null);
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
    
    public static void loadBeersToDb(String apiKey) throws RestException, SQLException{
//        BreweryDb b = new BreweryDb();
        List<BeerResults> results = getAllBeers(apiKey);
        int totalskipped=0;
        Connection conn= DBUtils.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
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
            throw new RuntimeException("Currently updating is not supported!");
        }else{
            dsl.insertInto(BEER,BEER.ID,BEER.NAME,BEER.URL,BEER.WEBSERVICE_NAME)
            .values(id,beer.getName(),beerURL,beer.getId()).execute();
        }
        
        return true;
    }
}
