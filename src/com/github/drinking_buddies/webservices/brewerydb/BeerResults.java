package com.github.drinking_buddies.webservices.brewerydb;

import java.util.List;



public class BeerResults {

	
	private int currentPage;
   	private List<Beer> data;
   	private int numberOfPages;
   	private String status;
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public List<Beer> getData() {
		return data;
	}
	public void setData(List<Beer> data) {
		this.data = data;
	}
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
   	
   	
}
