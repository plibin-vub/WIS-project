package com.github.drinking_buddies.webservices.brewerydb;

import java.util.Map;

public class Beer {
	private String id;
    private String name;
	private String description;
    private String abv;
    private String ibu;
    private Map<String,String> labels;
    private String icon;
    private String label;
    
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
  	public String getAbv() {
  		return abv;
  	}
  	public void setAbv(String abv) {
  		this.abv = abv;
  	}
  	public String getIbu() {
  		return ibu;
  	}
  	public void setIbu(String ibu) {
  		this.ibu = ibu;
  	}
  	public Map<String, String> getLabels() {
  		return labels;
  	}
  	public void setLabels(Map<String, String> labels) {
  		this.labels = labels;
  	}
  	public String getIcon() {
  		return icon;
  	}
  	public void setIcon(String icon) {
  		this.icon = icon;
  	}
  	public String getLabel() {
  		return label;
  	}
  	public void setLabel(String label) {
  		this.label = label;
  	}
    
}
