package com.github.drinking_buddies.webservices.facebook;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;

import com.github.drinking_buddies.webservices.rest.RestRequest;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;
import com.google.gson.Gson;

public class Facebook {

	
	public Facebook(String token){
		this.token=token;
	}
	
	private String token;
	private static final String HOST="graph.facebook.com";
	private static final String FRIENDS_PATH="/me/friends";
	
	public List<Friend> getFriends() throws RestException{
		URI url;
		try {
			url = new URIBuilder()
					.setScheme("https")
			        .setHost(HOST)
			        .setPath(FRIENDS_PATH)
			        .setParameter("access_token", token)
			        .build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("token is not valid",e);
		}
		System.out.println(url.toString());
		String jsonFriends=RestRequest.makeRequest(url.toString());
		Gson gson = new Gson();
		FriendsPage friendsPage = gson.fromJson(jsonFriends, FriendsPage.class);
		List<Friend> friends = friendsPage.getData();
		while(friendsPage.getPaging().getNext()!=null){
			jsonFriends=RestRequest.makeRequest(friendsPage.getPaging().getNext());
			friendsPage = gson.fromJson(jsonFriends, FriendsPage.class);
			friends.addAll(friendsPage.getData());
		}
		return friends;
	}
	
	public Person getUser(String id) throws RestException{
		URI url;
		try {
			url = new URIBuilder()
				.setScheme("https")
				.setHost(HOST)
				.setPath("/"+id)
				.setParameter("access_token", token)
				.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Id or token is not valid",e);
		}
		System.out.println(url.toString());
		String jsonUser=RestRequest.makeRequest(url.toString());
		Gson gson = new Gson();
		Person person = gson.fromJson(jsonUser, Person.class);
		return person;
	}
	
}
