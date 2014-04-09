package com.vtecsys.vlib.model;

import android.text.TextUtils;

public class Book {
	
	private String rid;
	private String title;
	private String author;
	private String publication;
	private String callNumber;
	private String bookCover;
	private String isbn;
	private String edition;
	
	public Book() {}
	
	public String getRID() {
		return rid;
	}
	
	public void setRID(String rid) {
		this.rid = rid;
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
	
	public String getPublisher() {
		if (TextUtils.isEmpty(publication)) {
			return null;
		}
		String[] items = publication.split("[:,]");
		return (items.length > 1) ? items[1].trim() : items[0];
	}
	
	public String getCallNumber() {
		return callNumber;
	}
	
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}
	
	public String getBookCover() {
		return bookCover;
	}
	
	public void setBookCover(String bookCover) {
		this.bookCover = bookCover;
	}
	
	public String getISBN() {
		return isbn;
	}
	
	public void setISBN(String isbn) {
		this.isbn = isbn;
	}
	
	public String getEdition() {
		return edition;
	}
	
	public void setEdition(String edition) {
		this.edition = edition;
	}

}
