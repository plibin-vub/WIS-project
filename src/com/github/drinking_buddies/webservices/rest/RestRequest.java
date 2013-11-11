package com.github.drinking_buddies.webservices.rest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.github.drinking_buddies.webservices.rest.exceptions.RestException;


public class RestRequest {
	
	public static String makeRequest(String url) throws RestException {
		DefaultHttpClient  httpClient =null;
		try{
			httpClient=  new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RestException("Response status code is "+response.getStatusLine().getStatusCode());
			}
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (ParseException e) {
			throw new RestException("Request Failed: "+e.getMessage(),e);
		} catch (IOException e) {
			throw new RestException("Request Failed: "+e.getMessage(),e);
		}
		finally{
			if(httpClient!=null){
				httpClient.getConnectionManager().shutdown();;
			}
		}
	}

}
