package com.sidejobs.api.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sidejobs.api.entities.Area;

public interface AreasRepository extends CrudRepository<Area,String> {

	Area findAreaById(String id);
	
	Area findAreaByName(String name);
	
	@Query(value = "SELECT * FROM areas WHERE 1",nativeQuery=true) 
	Collection<Area> listAreas();
	
	@Query(value = "SELECT * FROM areas a WHERE a.id IN (SELECT area_id FROM join_categories_areas WHERE category_id = :id AND status = 'Active')", nativeQuery=true)
	Collection<Area> listAreasByActiveCategory(@Param("id") String id);
}
