package com.devpro.shop16.controller.quantrivien;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devpro.shop16.controller.BaseController;
import com.devpro.shop16.dto.AddProduct;
import com.devpro.shop16.dto.Cart;
import com.devpro.shop16.dto.CartItem;

import com.devpro.shop16.dto.ContactSearchModel;
import com.devpro.shop16.dto.ProductSearchModel;
import com.devpro.shop16.entities.Contact;
import com.devpro.shop16.repository.ContactRepository;
import com.devpro.shop16.service.CategoriesService;
import com.devpro.shop16.service.ContactService;
import com.devpro.shop16.service.ProductService;

import antlr.StringUtils;

@Controller
public class AdminContactController extends BaseController{
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@RequestMapping(value = { "/admin/contact" }, method = RequestMethod.GET)
	public String adminContact(final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		
		ContactSearchModel searchModel = new ContactSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		searchModel.setPage(getCurrentPage(request));
		
		model.addAttribute("contactWithPaging", contactService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		
		
		return "quantrivien/contact";
	}
	
	
//	@GetMapping( "/delete/{contactId}" )
//	public String adminContactDelete(@PathVariable("contactId") Integer id) {
//
//		contactRepository.deleteById(id);
//
//		return "redirect:/admin/contact";
//	}

	
//	@RequestMapping(value = { "/admin/product/delete/{contactId}" }, method = RequestMethod.GET)
//	public String adminProductDelete(final Model model, final HttpServletRequest request, RedirectAttributes notify,
//			final HttpServletResponse response, @PathVariable("contactId") int contactId) throws IOException {
//
//
//		// lấy sản phẩm trong db
//		Contact contact = contactService.getById(contactId);
//
//		// delete sản phẩm
//		contactService.remove(contact);
//		notify.addFlashAttribute("TB", contact.getName() + " has been removed to database!");
//
//		return "redirect:/admin/contact";
//	}

}
