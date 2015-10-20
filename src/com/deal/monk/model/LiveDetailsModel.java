package com.deal.monk.model;

import java.util.ArrayList;

public class LiveDetailsModel {

	String id ,name ,location ,foodtype ,distance ,live_detail ,restaurentUrl, latitude, longitude;
	private ArrayList<String> cuisine;
	
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public ArrayList<String> getCuisine() {
        return cuisine;
    }

    public void setCuisine(ArrayList<String> cuisine) {
        this.cuisine = cuisine;
    }

	/**
	 * @return the restaurentUrl
	 */
	public String getRestaurentUrl() {
		return restaurentUrl;
	}

	/**
	 * @param restaurentUrl the restaurentUrl to set
	 */
	public void setRestaurentUrl(String restaurentUrl) {
		this.restaurentUrl = restaurentUrl;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the foodtype
	 */
	public String getFoodtype() {
		return foodtype;
	}

	/**
	 * @param foodtype the foodtype to set
	 */
	public void setFoodtype(String foodtype) {
		this.foodtype = foodtype;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(String distance) {
		this.distance = distance;
	}

	/**
	 * @return the live_detail
	 */
	public String getLive_detail() {
		return live_detail;
	}

	/**
	 * @param live_detail the live_detail to set
	 */
	public void setLive_detail(String live_detail) {
		this.live_detail = live_detail;
	}
	
	
}
