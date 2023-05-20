package com.devpro.shop16.controller.quantrivien;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {
	//định nghĩa action phải sử dụng "@RequestMapping"
		/*
		 * model : sử dụng để đẩy dữ liệu từ controller -> view
		 * request :thông tin người dùng đẩy lên controller
		 * response
		 * */
		@RequestMapping(value = {"/admin","/admin/home"},
				method = RequestMethod.GET)
		public String home(final Model model, final HttpServletRequest request,final HttpServletResponse response )
		throws IOException{
			return "quantrivien/home";
		}

}
