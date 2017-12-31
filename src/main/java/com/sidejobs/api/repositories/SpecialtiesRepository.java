package com.sidejobs.api.repositories;

import com.sidejobs.api.entities.Specialty;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface SpecialtiesRepository extends CrudRepository<Specialty, String> {
	
	Specialty findSpecialtyById(String id);
	
	Specialty findSpecialtyByName(String name);
	
	@Query(value = "SELECT * FROM specialties WHERE 1", nativeQuery=true)
	Collection<Specialty> listSpecialites();
	
	@Query(value = "SELECT s.id,s.name FROM specialties s, categories_specialties cs WHERE s.id IN (SELECT specialty_id FROM join_areas_specialties WHERE area_id = :area ) AND s.id = cs.specialty_id AND cs.category_id = :category ORDER BY s.name",nativeQuery=true)
	Collection<Specialty> listSpecialtiesByAreaAndCategory(@Param("area")String area, @Param("category") String category);
	
	@Query(value = "SELECT * FROM specialties s WHERE s.id IN (SELECT specialty_id FROM join_areas_specialties WHERE area_id = :id)", nativeQuery=true)
	Collection<Specialty> listSpecialtiesByArea(@Param("id")String id);

}
