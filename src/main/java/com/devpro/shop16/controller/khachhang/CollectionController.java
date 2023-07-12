package com.devpro.shop16.controller.khachhang;

import com.devpro.shop16.controller.BaseController;
import com.devpro.shop16.dto.ProductSearchModel;
import com.devpro.shop16.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class CollectionController extends BaseController {

	private final ProductService productService;

	public CollectionController(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping(value = { "/collection" }, method = RequestMethod.GET)
	public String cartView(final Model model, final HttpServletRequest request) {
		ProductSearchModel searchModel = new ProductSearchModel();
		searchModel.keyword = request.getParameter("keyword");

		model.addAttribute("productsWithPaging", productService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		return "khachhang/collection";
	}

	@RequestMapping(value = { "/collection/{categoryId}" }, method = RequestMethod.GET)
	public String cart(final Model model, @PathVariable("categoryId") int categoryId) throws IOException {

		model.addAttribute("categories", categoryId);

		return "khachhang/collection";
	}

}
