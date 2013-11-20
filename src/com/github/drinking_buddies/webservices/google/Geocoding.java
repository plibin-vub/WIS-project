package com.github.drinking_buddies.webservices.google;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.utils.URIUtils;

import com.github.drinking_buddies.entities.Address;
import com.github.drinking_buddies.webservices.google.Geocoding.GoogleGeoCodeResponse.location;
import com.github.drinking_buddies.webservices.rest.RestRequest;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;
import com.google.gson.Gson;

public class Geocoding {
    public static class GoogleGeoCodeResponse {
        static class results {
            public String formatted_address;
            public geometry geometry;
            public String[] types;
            public address_component[] address_components;
        }

        static class geometry {
            public bounds bounds;
            public String location_type;
            public location location;
            public bounds viewport;
        }

        static class bounds {
            public location northeast;
            public location southwest;
        }

        static class location {
            public String lat;
            public String lng;
        }

        static class address_component {
            public String long_name;
            public String short_name;
            public String[] types;
        }

        public String status;
        public results[] results;

        public GoogleGeoCodeResponse() {

        }
    }
    
    private static String createAddressString(Address address) {
        return address.getNumber() + " " + address.getStreet() + "," + address.getCity() + " " + address.getZip() + "," + address.getCountry();
    }
    
    public static location addressToLocation(Address address) throws RestException {
        URI url = null;
        try {
            Map<String, String> args = new HashMap<String, String>();
            args.put("address", createAddressString(address));
            args.put("sensor", "false");
            
            url = URIUtils.createURI("https", "maps.googleapis.com", 443, 
                    "/maps/api/geocode/json",
                    RestRequest.argMapToString(args),
                    null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println(url.toString());
        String json=RestRequest.makeRequest(url.toString());

        Gson gson = new Gson();
        GoogleGeoCodeResponse ggcr = gson.fromJson(json, GoogleGeoCodeResponse.class);
        
        if (ggcr != null 
                && ggcr.results.length > 0 
                && ggcr.results[0].geometry != null)
            return ggcr.results[0].geometry.location;
        else
            return null;
    }
}
