package com.vtecsys.vlib.model.result;

import java.util.ArrayList;
import java.util.List;

import com.vtecsys.vlib.model.Auth;

public class BrowseResult {
	
	private int loaded;
	private List<Auth> authes;
	
	public BrowseResult() {
		authes = new ArrayList<Auth>();
	}
	
	public int getLoaded() {
		return loaded;
	}
	
	public void setLoaded(int loaded) {
		this.loaded = loaded;
	}
	
	public List<Auth> getAuthes() {
		return authes;
	}
	
	public void setAuthes(List<Auth> authes) {
		this.authes = authes;
	}
	
	public void addAuth(Auth auth) {
		if (authes != null) {
			authes.add(auth);
		}
	}

}
