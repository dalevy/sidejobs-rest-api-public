package com.sidejobs.api.controllers;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sidejobs.api.common.ResponseStatus;
import com.sidejobs.api.common.ResponseWrapper;
import com.sidejobs.api.entities.Area;
import com.sidejobs.api.entities.Category;
import com.sidejobs.api.entities.Specialty;
import com.sidejobs.api.repositories.AreasRepository;
import com.sidejobs.api.repositories.CategoriesRepository;
import com.sidejobs.api.repositories.SpecialtiesRepository;


@RestController
@RequestMapping("/jobs")
public class JobsController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final CategoriesRepository categoriesRepository;
	private final AreasRepository areasRepository;
	private final SpecialtiesRepository specialtiesRepository;
	
	@Autowired
	public JobsController(CategoriesRepository categoriesRepository, AreasRepository areasRepository, SpecialtiesRepository specialtiesRepository)
	{
		this.categoriesRepository = categoriesRepository;
		this.areasRepository = areasRepository;
		this.specialtiesRepository = specialtiesRepository;
	}
	
	@RequestMapping(value="/specialties/area/category/list", method=RequestMethod.GET)
	public ResponseWrapper<Collection<Specialty>> getSpecialtiesByAreaAndCategory(@RequestParam("area") String area, @RequestParam("category") String category){
		
		ResponseWrapper<Collection<Specialty>> result = new ResponseWrapper<Collection<Specialty>>(ResponseStatus.Failure);
		Collection<Specialty> specialties = this.specialtiesRepository.listSpecialtiesByAreaAndCategory(area, category);
		
		if(specialties == null)
			return result;
		
		result =  new ResponseWrapper<Collection<Specialty>>(ResponseStatus.Success,specialties);
	
		return result;
	}
	
	@RequestMapping(value="/specialties/area/{id}/list", method=RequestMethod.GET)
	public ResponseWrapper<Collection<Specialty>> getSpecialtiesByArea(@PathVariable("id") String id)
	{
		ResponseWrapper<Collection<Specialty>> result = new ResponseWrapper<Collection<Specialty>>(ResponseStatus.Failure);
		Collection<Specialty> specialties = this.specialtiesRepository.listSpecialtiesByArea(id);
		
		if(specialties == null)
			return result;
		
		result =  new ResponseWrapper<Collection<Specialty>>(ResponseStatus.Success,specialties);
	
		return result;
	}
	
	
	/***
	 * List specialty by id
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/specialties/search/{id}",method=RequestMethod.GET)
	public ResponseWrapper<Specialty> getSpecialtyById(@PathVariable String id){
	
		ResponseWrapper<Specialty> result = new ResponseWrapper<Specialty>(ResponseStatus.Failure);
		Specialty categories = this.specialtiesRepository.findSpecialtyById(id);
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Specialty>(ResponseStatus.Success,categories);
	
		return result;
		
	}
	
	
	/***
	 * List all specialites
	 * @return
	 */
	@RequestMapping(value="/specialties/list",method=RequestMethod.GET)
	public ResponseWrapper<Collection<Specialty>> getSpecialties(){
	
		ResponseWrapper<Collection<Specialty>> result = new ResponseWrapper<Collection<Specialty>>(ResponseStatus.Failure);
		Collection<Specialty> categories = this.specialtiesRepository.listSpecialites();
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Collection<Specialty>>(ResponseStatus.Success,categories);
	
		return result;
		
	}
	
	
	/***
	 * List area by id
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/areas/search/{id}",method=RequestMethod.GET)
	public ResponseWrapper<Area> getAreaById(@PathVariable String id){
	
		ResponseWrapper<Area> result = new ResponseWrapper<Area>(ResponseStatus.Failure);
		Area categories = this.areasRepository.findAreaById(id);
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Area>(ResponseStatus.Success,categories);
	
		return result;
		
	}
	
	
	/***
	 * List all areas
	 * @return
	 */
	@RequestMapping(value="/areas/list",method=RequestMethod.GET)
	public ResponseWrapper<Collection<Area>> getActiveAreas(){
	
		ResponseWrapper<Collection<Area>> result = new ResponseWrapper<Collection<Area>>(ResponseStatus.Failure);
		Collection<Area> categories = this.areasRepository.listAreas();
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Collection<Area>>(ResponseStatus.Success,categories);
	
		return result;
		
	}
	
	@RequestMapping(value="/areas/category/{id}/list")
	public ResponseWrapper<Collection<Area>> getAreasByActiveCategory(@PathVariable("id") String id)
	{
		ResponseWrapper<Collection<Area>> result = new ResponseWrapper<Collection<Area>>(ResponseStatus.Failure);
		Collection<Area> categories = this.areasRepository.listAreasByActiveCategory(id);
		
		if(categories == null)
			return result;
		
		result =  new ResponseWrapper<Collection<Area>>(ResponseStatus.Success,categories);
	
		return result;
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
