package com.github.drinking_buddies.webservices.facebook;

import java.util.List;



public class FriendsPage {
	private List<Friend> data;
	private PagingData paging;
	public List<Friend> getData() {
		return data;
	}
	public void setData(List<Friend> data) {
		this.data = data;
	}
	public PagingData getPaging() {
		return paging;
	}
	public void setPaging(PagingData paging) {
		this.paging = paging;
	}
	
	
}
