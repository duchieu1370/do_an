package com.devpro.shop16.controller;

import com.devpro.shop16.entities.Categories;
import com.devpro.shop16.entities.User;
import com.devpro.shop16.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        if (userLogined != null && userLogined instanceof UserDetails)
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
