package com.sidejobs.api.repositories;

import com.sidejobs.api.entities.Specialty;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface SpecialtiesRepository extends CrudRepository<Specialty, String> {
	
	Specialty findSpecialtyById(String id);
	
	Specialty findSpecialtyByName(String name);
	
	@Query(value = "SELECT * FROM specialties WHERE 1", nativeQuery=true)
	Collection<Specialty> listSpecialites();

}
