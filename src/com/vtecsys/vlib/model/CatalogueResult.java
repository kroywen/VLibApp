package com.vtecsys.vlib.model;

import java.util.ArrayList;
import java.util.List;

public class CatalogueResult {
	
	private Book book;
	private List<Volume> volumes;
	
	public CatalogueResult() {
		volumes = new ArrayList<Volume>();
	}
	
	public Book getBook() {
		return book;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public List<Volume> getVolumes() {
		return volumes;
	}
	
	public void setVolumes(List<Volume> volumes) {
		this.volumes = volumes;
	}
	
	public void addVolume(Volume volume) {
		if (volumes != null) {
			volumes.add(volume);
		}
	}

}
