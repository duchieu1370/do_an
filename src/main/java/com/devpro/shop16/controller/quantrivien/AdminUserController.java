package com.devpro.shop16.controller.quantrivien;


import com.devpro.shop16.controller.BaseController;
import com.devpro.shop16.dto.UserSearchModel;
import com.devpro.shop16.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminUserController extends BaseController{

	private final UserService userService;

	public AdminUserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = { "/admin/user/list","/admin/user" }, method = RequestMethod.GET)
	public String adminProductList(final Model model, final HttpServletRequest request) {
		
		UserSearchModel searchModel = new UserSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		searchModel.setPage(getCurrentPage(request));
		searchModel.roleId = super.getInteger(request, "roleId");
		
		model.addAttribute("userWithPaging", userService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		
		return "quantrivien/user";
	}

	

}
