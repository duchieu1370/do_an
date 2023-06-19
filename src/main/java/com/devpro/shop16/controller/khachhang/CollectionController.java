package com.devpro.shop16.controller.khachhang;

import com.devpro.shop16.controller.BaseController;
import com.devpro.shop16.dto.ProductSearchModel;
import com.devpro.shop16.entities.Categories;
import com.devpro.shop16.service.CategoriesService;
import com.devpro.shop16.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
		return "khachhang/collection";
	}

	@RequestMapping(value = { "/collection/{categoryId}" }, method = RequestMethod.GET)
	public String cart(final Model model, final HttpServletRequest request, final HttpServletResponse response,
			@PathVariable("categoryId") int categoryId) throws IOException {

		Categories categories = categoriesService.getById(categoryId);
		model.addAttribute("categories", categoryId);

		return "khachhang/collection";
	}

}
//