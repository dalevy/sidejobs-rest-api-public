package com.sidejobs.api.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sidejobs.api.common.ResponseStatus;
import com.sidejobs.api.common.ResponseWrapper;
import com.sidejobs.api.entities.Category;
import com.sidejobs.api.repositories.CategoriesRepository;


@RestController
@RequestMapping("/jobs")
public class JobsController {

	private final CategoriesRepository categoriesRepository;
	
	@Autowired
	public JobsController(CategoriesRepository categoriesRepository)
	{
		this.categoriesRepository = categoriesRepository;
	}
	
	/***
	 * List all active categories
	 * @return
	 */
	@RequestMapping(value="/categories/list/active",method=RequestMethod.GET)
	public ResponseWrapper<Collection<Category>> getActiveCategories(){
	
		ResponseWrapper<Collection<Category>> result = new ResponseWrapper<Collection<Category>>(ResponseStatus.Failure);
		Collection<Category> categories = this.categoriesRepository.listActiveCategories();
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Collection<Category>>(ResponseStatus.Success,categories);
	
		return result;
		
	}
	
	/***
	 * List all categories regardless of status
	 * @return
	 */
	@RequestMapping(value="/categories/list",method=RequestMethod.GET)
	public ResponseWrapper<Collection<Category>> getCategories(){
	
		ResponseWrapper<Collection<Category>> result = new ResponseWrapper<Collection<Category>>(ResponseStatus.Failure);
		Collection<Category> categories = this.categoriesRepository.listCategories();
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Collection<Category>>(ResponseStatus.Success,categories);
	
		return result;
		
	}
	
	/***
	 * List categories by id
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/categories/search/{id}",method=RequestMethod.GET)
	public ResponseWrapper<Category> getCategoryById(@PathVariable String id){
	
		ResponseWrapper<Category> result = new ResponseWrapper<Category>(ResponseStatus.Failure);
		Category categories = this.categoriesRepository.findCategoryById(id);
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Category>(ResponseStatus.Success,categories);
	
		return result;
		
	}
	
	/***
	 * List categories by name
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/categories/search/name/{name}",method=RequestMethod.GET)
	public ResponseWrapper<Category> getCategoryByName(@PathVariable String name){
	
		ResponseWrapper<Category> result = new ResponseWrapper<Category>(ResponseStatus.Failure);
		Category categories = this.categoriesRepository.findCategoryByName(name);
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Category>(ResponseStatus.Success,categories);
	
		return result;
		
	}
	

}
