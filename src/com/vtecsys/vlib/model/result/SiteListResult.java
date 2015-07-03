package com.vtecsys.vlib.model.result;

import java.util.ArrayList;
import java.util.List;

import com.vtecsys.vlib.model.Site;

public class SiteListResult {
	
	private int count;
	private List<Site> sites;
	
	public SiteListResult() {
		sites = new ArrayList<Site>();
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public List<Site> getSites() {
		return sites;
	}
	
	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	
	public void addSite(Site site) {
		if (sites != null) {
			sites.add(site);
		}
	}

}
