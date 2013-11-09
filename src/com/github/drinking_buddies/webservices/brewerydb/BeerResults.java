package com.github.drinking_buddies.webservices.brewerydb;

import java.util.List;



public class BeerResults {

	
	private Number currentPage;
   	private List<Beer> data;
   	private Number numberOfPages;
   	private String status;
	public Number getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Number currentPage) {
		this.currentPage = currentPage;
	}
	public List<Beer> getData() {
		return data;
	}
	public void setData(List<Beer> data) {
		this.data = data;
	}
	public Number getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(Number numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
   	
   	
}
