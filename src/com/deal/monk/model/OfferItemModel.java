package com.deal.monk.model;

public class OfferItemModel {
	private String restaurant_offer;
	private String start_time;
	private String offer_id;
	private String end_time;
	private String restaurent_area;
	private String success;
	private String restaurant_name;
	private String restaurant_image;
	private String restaurant_address1;
	private String restaurant_address2;
	private String restaurant_contact_details;
	
	public OfferItemModel (String restaurant_offer, String start_time,
			String offer_id, String end_time, String restaurent_area,
			String success, String restaurant_name, String restaurant_image,
			String restaurant_address1, String restaurant_address2,
			String restaurant_contact_details) {
		super();
		this.restaurant_offer = restaurant_offer;
		this.start_time = start_time;
		this.offer_id = offer_id;
		this.end_time = end_time;
		this.restaurent_area = restaurent_area;
		this.success = success;
		this.restaurant_name = restaurant_name;
		this.restaurant_image = restaurant_image;
		this.restaurant_address1 = restaurant_address1;
		this.restaurant_address2 = restaurant_address2;
		this.restaurant_contact_details = restaurant_contact_details;
	}

	public String getRestaurant_offer() {
		return restaurant_offer;
	}

	public void setRestaurant_offer(String restaurant_offer) {
		this.restaurant_offer = restaurant_offer;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(String offer_id) {
		this.offer_id = offer_id;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getRestaurent_area() {
		return restaurent_area;
	}

	public void setRestaurent_area(String restaurent_area) {
		this.restaurent_area = restaurent_area;
	}

	public String getRestaurant_name() {
		return restaurant_name;
	}

	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}

	public String getRestaurant_image() {
		return restaurant_image;
	}

	public void setRestaurant_image(String restaurant_image) {
		this.restaurant_image = restaurant_image;
	}

	public String getRestaurant_address1() {
		return restaurant_address1;
	}

	public void setRestaurant_address1(String restaurant_address1) {
		this.restaurant_address1 = restaurant_address1;
	}

	public String getRestaurant_address2() {
		return restaurant_address2;
	}

	public void setRestaurant_address2(String restaurant_address2) {
		this.restaurant_address2 = restaurant_address2;
	}

	public String getRestaurant_contact_details() {
		return restaurant_contact_details;
	}

	public void setRestaurant_contact_details(String restaurant_contact_details) {
		this.restaurant_contact_details = restaurant_contact_details;
	}
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

}
