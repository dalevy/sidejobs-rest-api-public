package com.sidejobs.api.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sidejobs.api.entities.Verification;

public interface VerificationsRepository extends CrudRepository<Verification, String> {

	Verification findVerificationByToken(String token);
	
	@Modifying
	@Transactional
	@Query(value="INSERT INTO verifications (token,user_id,reason) VALUES(:token,:user_id,:reason)", nativeQuery = true)
	void insertVerificationToken(
			@Param("token") String token,
			@Param("user_id") String user_id,
			@Param("reason") String reason
			);
	 
}
