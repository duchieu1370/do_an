package com.devpro.shop16.controller.quantrivien;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.devpro.shop16.controller.BaseController;
import com.devpro.shop16.dto.SubcribeSearchModel;
import com.devpro.shop16.service.SubcribeService;

@Controller
public class AdminSubcribeController extends BaseController{
	
	@Autowired
	private SubcribeService subcribeService;
	
	@RequestMapping(value = { "/admin/subcribe" }, method = RequestMethod.GET)
	public String adminSubcribe(final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		
		SubcribeSearchModel searchModel = new SubcribeSearchModel();
		searchModel.keyword = request.getParameter("keyword");	
		searchModel.setPage(getCurrentPage(request));
		
		model.addAttribute("subcribeWithPaging", subcribeService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		
		
		return "quantrivien/subcribe";
	}
	

}
