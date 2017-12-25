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


}
