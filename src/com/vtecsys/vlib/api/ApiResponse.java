package com.vtecsys.vlib.api;

import java.io.Serializable;

public class ApiResponse implements Serializable {
	
	private static final long serialVersionUID = 2298834586169426687L;
	
	public static final int STATUS_UNDEFINED = -1;
	public static final int STATUS_OK = 0;

	private int status;
	private String message;
	private Object data;
	
	public ApiResponse() {
		this(STATUS_UNDEFINED, null ,null);
	}
	
	public ApiResponse(int status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
}
