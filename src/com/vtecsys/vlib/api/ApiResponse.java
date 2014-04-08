package com.vtecsys.vlib.api;

import java.io.Serializable;

public class ApiResponse implements Serializable {
	
	private static final long serialVersionUID = 2298834586169426687L;
	
	public static final int STATUS_OK = 0;

	private int status;
	private String message;
	private String requestName;
	private Object data;
	
	public ApiResponse() {}
	
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
	
	public String getRequestName() {
		return requestName;
	}
	
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
}
