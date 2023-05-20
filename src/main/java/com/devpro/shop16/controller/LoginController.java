package com.devpro.shop16.controller;

import com.devpro.shop16.entities.Role;
import com.devpro.shop16.entities.User;
import com.devpro.shop16.service.RoleService;
import com.devpro.shop16.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		
		model.addAttribute("user", new User());
		
		return "login";
	}
	
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public String post_spring_form(final Model model, 
								   final HttpServletRequest request, 
								   final HttpServletResponse response,
								   final @ModelAttribute("user") User user) throws IllegalStateException, IOException{
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4)));
		
		Role role = (new RoleService()).loadRoleByRolename("GUEST");
		user.addRoles(role);
		model.addAttribute("user",new User());
		userService.saveOrUpdate(user);
		
		// trả về đường dẫn tới view
		return "redirect:/login";
	}
	

}
