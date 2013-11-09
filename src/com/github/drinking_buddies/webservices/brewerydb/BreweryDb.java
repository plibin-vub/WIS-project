package com.github.drinking_buddies.webservices.brewerydb;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

import com.github.drinking_buddies.webservices.rest.RestRequest;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;
import com.google.gson.Gson;

public class BreweryDb {

	
	private static final String API_KEY="d6e1bfa7e16fac014a6df6f39866b308";
	private static final String HOST="api.brewerydb.com";
	private static final String BEERS_PATH="/v2/beers";
	
	
	public Beer getBeer(String id) throws RestException{
		URI url;
		try {
			url = new URIBuilder()
					.setScheme("http")
			        .setHost(HOST)
			        .setPath(BEERS_PATH)
			        .setParameter("key", API_KEY)
			        .setParameter("ids", id)
			        .build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Id is not valid",e);
		}
		System.out.println(url.toString());
		String jsonBeer=RestRequest.makeRequest(url.toString());
		Gson gson = new Gson();
		BeerResults beerResults = gson.fromJson(jsonBeer, BeerResults.class);
		return beerResults.getData().get(0);
	}
	
	
}
