package com.devpro.shop16.controller.quantrivien;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devpro.shop16.controller.BaseController;
import com.devpro.shop16.dto.AddProduct;

import com.devpro.shop16.dto.ProductSearchModel;
import com.devpro.shop16.entities.Categories;
import com.devpro.shop16.entities.Product;
import com.devpro.shop16.repository.ContactRepository;
import com.devpro.shop16.repository.ProductRepository;
import com.devpro.shop16.service.CategoriesService;
import com.devpro.shop16.service.ProductService;

@Controller
public class AddProductController extends BaseController{

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoriesService categoriesService;
	
	@Autowired
	private ProductRepository productRepository;
		
	@RequestMapping(value = { "/admin/product/list","/admin/product" }, method = RequestMethod.GET)
	public String adminProductList(final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		
		ProductSearchModel searchModel = new ProductSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		searchModel.setPage(getCurrentPage(request));
		searchModel.categoryId = super.getInteger(request, "categoryId");
		
		model.addAttribute("productsWithPaging", productService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		
		return "quantrivien/product";
	}

	@RequestMapping(value = { "/admin/product/add-product" }, method = RequestMethod.GET)
	public String admin_add(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		// tạo 1 đối tượng
		Product add = new Product();

		// đẩy đối tượng xuống tầng view từ Controller
		//đẩy danh sách categories xuống
//		model.addAttribute("categories", categoriesService.findAll());

		// thêm sản phẩm mới
		model.addAttribute("add", add);

		// trả đường dẫn về view
		return "quantrivien/add-product";
	}
	
	@RequestMapping(value = { "/admin/product/add-product/{productId}" }, method = RequestMethod.GET)
	public String adminProductEdit(final Model model, final HttpServletRequest request,
			final HttpServletResponse response, @PathVariable("productId") int productId) throws IOException {
		// đẩy danh sách categories xuống
//		model.addAttribute("categories", categoriesService.findAll());

		// lấy sản phẩm trong db
		Product product = productService.getById(productId);

		// edit sản phẩm
		model.addAttribute("add", product);

		//trả về tầng view
		return "quantrivien/add-product";
	}
	
	@GetMapping( "/delete/{id}" )
	public String adminDelete(@PathVariable("id") Integer id) {

		productRepository.deleteById(id);
		// lấy sản phẩm trong db
//		Product product = productService.getById(productId);

		// delete sản phẩm
//		productService.remove(product);
//		notify.addFlashAttribute("TB", product.getTitle() + " has been removed to database!");

		return "redirect:/admin/product";
	}


	@RequestMapping(value = { "/admin/product/add-product" }, method = RequestMethod.POST)
	public String admin_addpost_spring_form(final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final @ModelAttribute("add") Product product,
			@RequestParam("productAvatar") MultipartFile productAvatar, // hứng file đẩy lên
			@RequestParam("productPictures") MultipartFile[] productPictures) throws Exception { // hứng file đẩy lên)
																									// throws
																									// IllegalStateException,
																									// IOException{
//		model.addAttribute("TB", "Cảm ơn , bạn đã thêm " + product.getTenSP() + " thành công!");
		
		        // cần kiểm tra xem id của product
				// = null || = 0 thì thêm mới
				// ngược lại là chỉnh sửa
				if (product.getId() == null || product.getId() <= 0) { // thêm mới
					productService.add(product, productAvatar, productPictures);
				} else { // chỉnh sửa
					productService.update(product, productAvatar, productPictures);
				}

		// lưu ảnh lên server
//		attachment.transferTo(new File("C:/upload/" + attachment.getOriginalFilename()));
		// lưu đường dẫn ảnh vào add
//		add.setAttachment("C:/upload/" + attachment.getOriginalFilename());

		// sau khi xử lí xong thì đưa add về trạng mới
//		model.addAttribute("add", new Product());

		// trả về đường dẫn tới view
		return "redirect:/admin/product";

	}

	/**
	 * Sử dụng ajax
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param contactModel
	 * @return
	 */
	@RequestMapping(value = { "/ajax/add-product" }, method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> ajax_contact(final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final @RequestBody AddProduct add) {
		Map<String, Object> jsonResult = new HashMap<String, Object>();
		jsonResult.put("code", 200);
		jsonResult.put("message", "Cảm ơn, bạn đã thêm " + add.getTenSP() + " thành công!");
		return ResponseEntity.ok(jsonResult);
	}
	
	@RequestMapping(value = { "/admin/product/add-category" }, method = RequestMethod.GET)
	public String admin_add_category(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		// tạo 1 đối tượng
		Categories addCate = new Categories();

		// thêm sản phẩm mới
		model.addAttribute("addCate", addCate);

		// trả đường dẫn về view
		return "quantrivien/add-category";
	}
	


	@RequestMapping(value = { "/admin/product/add-category" }, method = RequestMethod.POST)
	public String admin_add_spring_form(final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final @ModelAttribute("addCate") Categories categories
			) throws Exception { 
		categoriesService.saveOrUpdate(categories);
		// trả về đường dẫn tới view
		return "redirect:/admin/product";

	}

}
