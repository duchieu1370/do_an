package com.devpro.shop16.controller.quantrivien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class AdminController {
		@RequestMapping(value = {"/admin","/admin/home"},
				method = RequestMethod.GET)
		public String home() throws IOException{
			return "quantrivien/home";
		}

}
