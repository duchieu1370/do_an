package com.devpro.shop16.controller.quantrivien;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AdminController {

		@RequestMapping(value = {"/admin","/admin/home"},
				method = RequestMethod.GET)
		public String home(final Model model, final HttpServletRequest request,final HttpServletResponse response )
		throws IOException{
			return "quantrivien/home";
		}

}
