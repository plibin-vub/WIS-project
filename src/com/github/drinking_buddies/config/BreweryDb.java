package com.github.drinking_buddies.config;

//class that represents the brewery-db web service configuration (which is defined in an XML config file)
public class BreweryDb {
    private String apiKey;

    public BreweryDb(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
