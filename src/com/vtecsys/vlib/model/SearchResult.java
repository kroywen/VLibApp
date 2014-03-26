package com.vtecsys.vlib.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
	
	private int hits;
	private int loaded;
	private List<Book> books;
	
	public SearchResult() {
		books = new ArrayList<Book>();
	}
	
	public int getHits() {
		return hits;
	}
	
	public void setHits(int hits) {
		this.hits = hits;
	}
	
	public int getLoaded() {
		return loaded;
	}
	
	public void setLoaded(int loaded) {
		this.loaded = loaded;
	}
	
	public List<Book> getBooks() {
		return books;
	}
	
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	public void addBook(Book book) {
		if (books != null) {
			books.add(book);
		}
	}

}
