package com.sidejobs.api.common;

import java.io.Serializable;

public class ResponseWrapper<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private T data;
	private ResponseStatus status;
	
	public ResponseWrapper() {}
	
	public ResponseWrapper(ResponseStatus status)
	{
		this.status = status;
	}
	
	public ResponseWrapper(ResponseStatus status, T data) {
		this(status);
		this.data = data;
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}
