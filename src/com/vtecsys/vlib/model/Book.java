package com.vtecsys.vlib.model;

import java.io.Serializable;

import android.text.TextUtils;

public class Book implements Serializable {
	
	private static final long serialVersionUID = 1713185284501831671L;
	
	private String rid;
	private String title;
	private String author;
	private String publication;
	private String callNumber;
	private String bookCover;
	private String isbn;
	private String edition;
	
	public Book() {}
	
	public Book(String rid, String title, String author, String publication,
			String callNumber, String bookCover, String isbn, String edition) 
	{
		this.rid = rid;
		this.title = title;
		this.author = author;
		this.publication = publication;
		this.callNumber = callNumber;
		this.bookCover = bookCover;
		this.isbn = isbn;
		this.edition = edition;
	}

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
	
	public boolean hasAuthor() {
		return !TextUtils.isEmpty(author);
	}
	
	public String getPublication() {
		return publication;
	}
	
	public void setPublication(String publication) {
		this.publication = publication;
	}
	
	public boolean hasPublication() {
		return !TextUtils.isEmpty(publication);
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
	
	public boolean  hasCallNumber() {
		return !TextUtils.isEmpty(callNumber);
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
	
	public boolean hasISBN() {
		return !TextUtils.isEmpty(isbn);
	}
	
	public String getEdition() {
		return edition;
	}
	
	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	public boolean hasEdition() {
		return !TextUtils.isEmpty(edition);
	}

}
