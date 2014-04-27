package com.vtecsys.vlib.model;

import java.io.Serializable;

import android.text.TextUtils;

public class Notices implements Serializable {
	
	private static final long serialVersionUID = -2898566294218208549L;
	
	private String predue;
	private String due;
	private String overdue;
	private String collection;
	
	public Notices() {}
	
	public String getPredue() {
		return predue;
	}
	
	public void setPredue(String predue) {
		this.predue = predue;
	}
	
	public boolean hasPredue() {
		return !TextUtils.isEmpty(predue);
	}
	
	public String getDue() {
		return due;
	}
	
	public void setDue(String due) {
		this.due = due;
	}
	
	public boolean hasDue() {
		return !TextUtils.isEmpty(due);
	}
	
	public String getOverdue() {
		return overdue;
	}
	
	public void setOverdue(String overdue) {
		this.overdue = overdue;
	}
	
	public boolean hasOverdue() {
		return !TextUtils.isEmpty(overdue);
	}
	
	public String getCollection() {
		return collection;
	}
	
	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public boolean hasCollection() {
		return !TextUtils.isEmpty(collection);
	}
	
	public boolean hasNotices() {
		return hasPredue() || hasDue() || hasOverdue() || hasCollection();
	}

}
