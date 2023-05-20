package com.devpro.shop16.controller.khachhang;

import java.io.IOException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.devpro.shop16.controller.BaseController;
import com.devpro.shop16.dto.Cart;
import com.devpro.shop16.dto.CartItem;
import com.devpro.shop16.dto.ContactSearchModel;
import com.devpro.shop16.dto.ProductSearchModel;
import com.devpro.shop16.entities.Categories;
import com.devpro.shop16.entities.Product;
import com.devpro.shop16.entities.Saleorder;
import com.devpro.shop16.entities.SaleorderProducts;
import com.devpro.shop16.service.CategoriesService;
import com.devpro.shop16.service.ProductService;
import com.devpro.shop16.service.SaleorderService;

@Controller
public class CollectionController extends BaseController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoriesService categoriesService;
	
	@RequestMapping(value = { "/collection" }, method = RequestMethod.GET)
	public String cartView(final Model model, final HttpServletRequest request, final HttpServletResponse response
			) throws IOException {

		

		ProductSearchModel searchModel = new ProductSearchModel();
		searchModel.keyword = request.getParameter("keyword");

		model.addAttribute("productsWithPaging", productService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		return "khachhang/collection"; // -> đường dẫn tới View.
	}

	@RequestMapping(value = { "/collection/{categoryId}" }, method = RequestMethod.GET)
	public String cart(final Model model, final HttpServletRequest request, final HttpServletResponse response,
			@PathVariable("categoryId") int categoryId) throws IOException {

		Categories categories = categoriesService.getById(categoryId);
		model.addAttribute("categories", categoryId);

		return "khachhang/collection"; // -> đường dẫn tới View.
	}

}
//