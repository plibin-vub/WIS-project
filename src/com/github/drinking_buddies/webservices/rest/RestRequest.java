package com.github.drinking_buddies.webservices.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

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
	
	public static String argMapToString(Map<String, String> args) {
	    StringBuilder sb = new StringBuilder();
	    for (String k : args.keySet()) {
	       if (sb.length() > 0)
	           sb.append("&");
	       try {
	           sb.append(k + "=" + URLEncoder.encode(args.get(k), "UTF8"));
           } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
           }
	    }
	    return sb.toString();
	}

}
