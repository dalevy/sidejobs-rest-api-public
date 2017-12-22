package com.sidejobs.api.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sidejobs.api.entities.Category;


public interface CategoriesRepository extends CrudRepository<Category, String> {

	Category findCategoryById(String id);
	
	Category findCategoryByName(String name);
	
	@Query(value = "SELECT * FROM categories WHERE status='Active'",nativeQuery=true) 
	Collection<Category> listActiveCategories();
	
	@Query(value = "SELECT * FROM categories WHERE 1",nativeQuery=true) 
	Collection<Category> listCategories();
}
