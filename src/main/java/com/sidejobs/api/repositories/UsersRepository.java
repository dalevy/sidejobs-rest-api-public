package com.sidejobs.api.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sidejobs.api.entities.User;

public interface UsersRepository extends CrudRepository<User,String> {

	User findUserById(String id);
	Optional<User> findUserByEmail(String email);
	User findUserByPhone(String phone);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE users_identity SET verified = 'Verified' WHERE id = :id", nativeQuery=true)
	void setUserAccountVerified(@Param("id") String id);
	
	@Procedure(name="register_worker_user")
	void registerStudentUser(
			@Param("p_id") String p_id,
			@Param("p_first_name") String p_first_name,
			@Param("p_last_name") String p_last_name,
			@Param("p_email") String p_email,
			@Param("p_password") String p_password,
			@Param("p_phone") String p_phone
		);
}
