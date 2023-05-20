package com.devpro.shop16.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.devpro.shop16.entities.Categories;
import com.devpro.shop16.entities.User;
import com.devpro.shop16.service.CategoriesService;

public abstract class BaseController {

	@Autowired
	private CategoriesService categoriesService;

	@ModelAttribute("isLogined")
	public boolean isLogined() {
		boolean isLogined = false;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			isLogined = true;
		}
		return isLogined;
	}
	
	@ModelAttribute("userLogined")
	public User getUserLogined() {
		Object userLogined = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userLogined != null && userLogined instanceof UserDetails)
			return (User) userLogined;
		
		return null;
	}
	
	@ModelAttribute("categories")
	public List<Categories> getAllCategories() {
		return categoriesService.findAll();
	}
	
	@ModelAttribute("menus")
	public String getCategories() {
		return this.buildMenu();
	}
	
	private String buildMenu() {
		StringBuilder menu = new StringBuilder();
		//danh sách menu cha
//		List<Categories> categories = categoriesService.getAllParents();
		
//		for (Categories c : categories) {
//			menu.append("<li> <a href=\"/category/" + c.getSeo() + "\">" + c.getName() + "</a>");
			
//			if (c.getChilds() != null && !c.getChilds().isEmpty()) {
//				recursiveMenu(menu, c.getChilds());
//			}
			
//			menu.append("</li>");
//		}
		
		return menu.toString();
		
	}
	
	
	public int getCurrentPage(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("page"));
		} catch (Exception e) {
			return 1;
		}
	}
	
	
	public Integer getInteger(HttpServletRequest request, String paramName) {
		try {
			return Integer.parseInt(request.getParameter(paramName));
		} catch (Exception e) {
			return null;
		}
	}
	

}
