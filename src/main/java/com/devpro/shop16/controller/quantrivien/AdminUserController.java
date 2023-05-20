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
import com.devpro.shop16.dto.AddProduct;
import com.devpro.shop16.dto.UserSearchModel;
import com.devpro.shop16.service.RoleService;
import com.devpro.shop16.service.UserService;

@Controller
public class AdminUserController extends BaseController{

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/admin/user/list","/admin/user" }, method = RequestMethod.GET)
	public String adminProductList(final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		
		UserSearchModel searchModel = new UserSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		searchModel.setPage(getCurrentPage(request));
		searchModel.roleId = super.getInteger(request, "roleId");
		
		model.addAttribute("userWithPaging", userService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		
		return "quantrivien/user";
	}

	

}
