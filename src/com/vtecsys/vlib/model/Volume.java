package com.vtecsys.vlib.model;

public class Volume {
	
	private String volume;
	private String item;
	private String status;
	private String location;
	private boolean canReserve;
	
	public Volume() {}
	
	public String getVolume() {
		return volume;
	}
	
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public boolean canReserve() {
		return canReserve;
	}
	
	public void setCanReserve(boolean canReserve) {
		this.canReserve = canReserve;
	}

}
