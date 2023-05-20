package com.devpro.shop16.service;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;



import com.devpro.shop16.entities.Categories;


@Service
public class CategoriesService extends BaseService<Categories>{

	@Override
	protected Class<Categories> clazz() {
		// TODO Auto-generated method stub
		return Categories.class;
	}
	


}
