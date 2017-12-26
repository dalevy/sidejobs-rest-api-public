package com.sidejobs.api.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="verifications")
public class Verification implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private String token;
	
	@Column(name="user_id")
	private String userId;
	private String reason;
	
	@Column(name="sent_date")
	private Timestamp sentDate;
	
	private String status;
	
	public Verification() {}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Timestamp getSentDate() {
		return sentDate;
	}

	public void setSentDate(Timestamp sentDate) {
		this.sentDate = sentDate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isTokenValid(Timestamp now)
	{
		boolean isValid = false;
		
		if(!status.equalsIgnoreCase("Open"))
			return isValid;
		
		long milliseconds1 = sentDate.getTime();
		long milliseconds2 = now.getTime();

		long diff = milliseconds2 - milliseconds1;
		long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		//isValid = (diffHours > 24) ? true : false;
		
		isValid = (diffMinutes > 1) ? true : false;
		System.out.println("The time for the token has not passed");
		
		Date date = new Date(milliseconds1);
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(date);
		
		System.out.println("Sent: "+formattedDate);
		
		Date date2 = new Date(milliseconds2);
		formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(date2);
		System.out.println("Now: "+formattedDate);
		
		
		return isValid;
	}

	

}
