package com.nisum.test.user.errors;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class DataFormatException extends RuntimeException {

	private HttpStatus status;
    private String message;
    
    public DataFormatException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
