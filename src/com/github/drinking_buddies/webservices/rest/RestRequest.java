package com.github.drinking_buddies.webservices.rest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.github.drinking_buddies.webservices.rest.exceptions.RestException;


public class RestRequest {
	
	public static String makeRequest(String url) throws RestException {
		CloseableHttpClient  httpClient =null;
		try{
			httpClient= HttpClients.createDefault();
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
				try {
					httpClient.close();
				} catch (IOException e) {
					//TODO check how to handle
				}
			}
		}
	}

}
