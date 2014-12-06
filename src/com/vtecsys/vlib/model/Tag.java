package com.vtecsys.vlib.model;

import android.text.TextUtils;

public class Tag {
	
	private int seq;
	private int tag;
	private String caption;
	private String contents;
	
	public Tag() {}
	
	public int getSeq() {
		return seq;
	}
	
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public int getTag() {
		return tag;
	}
	
	public void setTag(int tag) {
		this.tag = tag;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public boolean hasCaption() {
		return !TextUtils.isEmpty(caption);
	}
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public boolean hasContents() {
		return !TextUtils.isEmpty(contents);
	}

}
