package com.github.drinking_buddies.webservices.brewerydb;

import java.util.Map;

//Beer POJO used to combine all data received from the brewery db web service
public class Beer {
    class Brewery {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
	private String id;
    private String name;
	private String description;
    private double abv;
    private Map<String,String> labels;
    private Brewery [] breweries;

    public String getId() {
  		return id;
  	}
  	public void setId(String id) {
  		this.id = id;
  	}
  	public String getName() {
  		return name;
  	}
  	public void setName(String name) {
  		this.name = name;
  	}
  	public String getDescription() {
  		return description;
  	}
  	public void setDescription(String description) {
  		this.description = description;
  	}
  	public double getAbv() {
  		return abv;
  	}
  	public void setAbv(double abv) {
  		this.abv = abv;
  	}
  	public Map<String, String> getLabels() {
  		return labels;
  	}
  	public void setLabels(Map<String, String> labels) {
  		this.labels = labels;
  	}
    public Brewery[] getBreweries() {
        return breweries;
    }
    public void setBreweries(Brewery[] breweries) {
        this.breweries = breweries;
    }
    
    public String getMainBrewery() {
        if (breweries.length > 0)
            return breweries[0].getName();
        return null;
    }
    
    public String getMediumLabelUrl() {
        return this.getLabels().get("medium");
    }
}
