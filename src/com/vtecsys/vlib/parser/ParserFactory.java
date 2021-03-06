package com.vtecsys.vlib.parser;

import android.text.TextUtils;

import com.vtecsys.vlib.api.ApiData;

public class ParserFactory {
	
	public static ApiParser getParser(String command) {
		if (TextUtils.isEmpty(command)) {
			return null;
		} else if (ApiData.COMMAND_SITELIST.equalsIgnoreCase(command)) {
			return new SiteListParser();
		} else if (ApiData.COMMAND_SITESELECT.equalsIgnoreCase(command)) {
			return new SiteSelectParser();
		} else if (ApiData.COMMAND_SITENAME.equalsIgnoreCase(command)) {
			return new SiteNameParser();
		} else if (ApiData.COMMAND_SEARCH.equalsIgnoreCase(command)) {
			return new SearchParser();
		} else if (ApiData.COMMAND_CATALOGUE.equalsIgnoreCase(command)) {
			return new CatalogueParser();
		} else if (ApiData.COMMAND_BROWSE.equalsIgnoreCase(command)) {
			return new BrowseParser();
		} else if (ApiData.COMMAND_ABOUT.equalsIgnoreCase(command)) {
			return new AboutParser();
		} else if (ApiData.COMMAND_PATR_ACCOUNT.equalsIgnoreCase(command)) {
			return new PatronAccountParser();
		} else if (ApiData.COMMAND_CHANGE_PASSWORD.equalsIgnoreCase(command)) { 
			return new ChangePasswordParser();
		} else if (ApiData.COMMAND_CHECK_ALERTS.equalsIgnoreCase(command)) {
			return new AlertParser();
		} else if (ApiData.COMMAND_RESERVATION.equalsIgnoreCase(command)) {
			return new SimpleParser();
		} else if (ApiData.COMMAND_CANCEL_RESERVATION.equalsIgnoreCase(command)) {
			return new SimpleParser();
		} else if (ApiData.COMMAND_RENEW_LOAN.equalsIgnoreCase(command)) {
			return new SimpleParser();
		} else if (ApiData.COMMAND_BIB_DETAIL.equalsIgnoreCase(command)) {
			return new BibDetailParser();
		} else {
			return null;
		}
	}

}
