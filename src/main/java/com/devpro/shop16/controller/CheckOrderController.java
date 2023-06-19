package com.devpro.shop16.controller;

import com.devpro.shop16.dto.OrderSearchModel;
import com.devpro.shop16.service.SaleorderProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CheckOrderController extends  BaseController{

	private final SaleorderProductsService saleorderProductsService;

	public CheckOrderController(SaleorderProductsService saleorderProductsService) {
		this.saleorderProductsService = saleorderProductsService;
	}

	@RequestMapping(value = { "/checkOrder" }, method = RequestMethod.GET)
	public String checkOrder(final Model model, final HttpServletRequest request) {
		OrderSearchModel searchModel = new OrderSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		searchModel.setPage(getCurrentPage(request));

		model.addAttribute("orderWithPaging", saleorderProductsService.search(searchModel));
		model.addAttribute("searchModel", searchModel);

		return "khachhang/checkOrder";
	}

}
