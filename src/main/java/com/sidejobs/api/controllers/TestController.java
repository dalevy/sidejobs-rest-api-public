package com.sidejobs.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sidejobs.api.repositories.CategoriesRepository;

@RestController
@RequestMapping("/rene")
public class TestController {
	
	private CategoriesRepository categoriesRepostiory;
	
	
	@Autowired
	public TestController(CategoriesRepository categoriesRepository)
	{
		this.categoriesRepostiory = categoriesRepository;
	}
}
