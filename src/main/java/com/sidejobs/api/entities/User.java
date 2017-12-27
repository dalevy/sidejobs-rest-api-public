package com.sidejobs.api.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

@Entity
@Table(name="users_identity")
@NamedStoredProcedureQueries({
	   @NamedStoredProcedureQuery(name = "register_worker_user", 
	                              procedureName = "register_worker_user",
	                              parameters = {
	                                 @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_id", type = String.class),
	                                 @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_first_name", type = String.class),
	                                 @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_last_name", type = String.class),
	                                 @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_email", type = String.class),
	                                 @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_password", type = String.class),
	                                 @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_phone", type = String.class),
	                              })

})
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;

	private String first_name;
	private String last_name;
	private String email;
	private String password;
	private int password_failures;
	private String phone;
	private String role;
	private String status;
	private String verified;
	
	public User() {
		
	}
	


	public String getId() {
		return id;
	}

	public String getRole() {
		return role;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone;
	}

	public String getPassword() {
		return password;
	}

	public int getPassword_failures() {
		return password_failures;
	}

	public String getStatus() {
		return status;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public void setPassword_failures(int password_failures) {
		this.password_failures = password_failures;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
