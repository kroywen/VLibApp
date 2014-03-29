package com.vtecsys.vlib.model;

public class Loan {
	
	private String itemNumber;
	private String loanDate;
	private String dueDate;
	private String title;
	private String author;
	private String pubication;
	private String location;
	private String collection;
	private String callNumber;
	private String suffix;
	private String volume;
	private boolean isOverdue;
	private boolean canRenew;
	private int renewed;
	
	public Loan() {}
	
	public String getItemNumber() {
		return itemNumber;
	}
	
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	public String getLoanDate() {
		return loanDate;
	}
	
	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}
	
	public String getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
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
		return pubication;
	}
	
	public void setPublication(String publication) {
		this.pubication = publication;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getCollection() {
		return collection;
	}
	
	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public String getCallNumber() {
		return callNumber;
	}
	
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public String getVolume() {
		return volume;
	}
	
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	public boolean isOverdue() {
		return isOverdue;
	}
	
	public void setIsOverdue(boolean isOverdue) {
		this.isOverdue = isOverdue;
	}
	
	public boolean canRenew() {
		return canRenew;
	}
	
	public void setCanRenew(boolean canRenew) {
		this.canRenew = canRenew;
	}
	
	public int getRenewed() {
		return renewed;
	}
	
	public void setRenewed(int renewed) {
		this.renewed = renewed;
	}

}
