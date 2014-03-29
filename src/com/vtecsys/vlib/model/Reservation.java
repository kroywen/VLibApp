package com.vtecsys.vlib.model;

public class Reservation {
	
	private String reserveDate;
	private String isReady;
	private String itemReady;
	private String isBooking;
	private String type;
	private String rid;
	private String issue;
	private String volume;
	private String title;
	private String author;
	private String publication;
	private String callNumber;
	private boolean canCancel;
	
	public Reservation() {}

	public String getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}

	public String getIsReady() {
		return isReady;
	}

	public void setIsReady(String isReady) {
		this.isReady = isReady;
	}

	public String getItemReady() {
		return itemReady;
	}

	public void setItemReady(String itemReady) {
		this.itemReady = itemReady;
	}

	public String getIsBooking() {
		return isBooking;
	}

	public void setIsBooking(String isBooking) {
		this.isBooking = isBooking;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRID() {
		return rid;
	}

	public void setRID(String rid) {
		this.rid = rid;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public boolean canCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

}
