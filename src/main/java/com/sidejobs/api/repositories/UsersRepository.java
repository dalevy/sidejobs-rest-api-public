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
	User findUserByEmailAndPassword(String email, String password);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE users_identity SET password_failures = :value WHERE id = :id", nativeQuery=true)
	void changeUserPasswordFailureCount(
			@Param("id")String id, 
			@Param("value")int value
			);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE users_identity SET status = :value WHERE id = :id", nativeQuery=true)
	void changeUserAccountStatus(
			@Param("id")String id, 
			@Param("value")String value
			);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE users_identity SET phone = :value WHERE id = :id", nativeQuery=true)
	void changeUserPhoneNumber(
			@Param("id")String id, 
			@Param("value")String value
			);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE users_identity SET email = :value WHERE id = :id", nativeQuery=true)
	void changeUserEmailAddress(
			@Param("id")String id, 
			@Param("value")String value
			);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE users_identity SET password = :value WHERE id = :id", nativeQuery=true)
	void changeUserPassword(
			@Param("id")String id, 
			@Param("value")String value
			);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE users_identity SET verified = 'Verified' WHERE id = :id", nativeQuery=true)
	void setUserAccountVerified(
			@Param("id") String id
			);
	
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
