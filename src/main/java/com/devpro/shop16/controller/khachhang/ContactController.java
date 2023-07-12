package com.devpro.shop16.controller.khachhang;

import com.devpro.shop16.entities.Contact;
import com.devpro.shop16.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ContactController{
	
	private final ContactService contactService;

	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

	@RequestMapping(value = { "/contact" }, method = RequestMethod.GET)
	public String contact(final Model model) throws IOException {

		Contact contact = new Contact();
		model.addAttribute("contact", contact);
		return "khachhang/contact";
	}


	@RequestMapping(value = { "/contact" }, method = RequestMethod.POST)
	public String post_spring_form(final Model model,
								   final @ModelAttribute("contact") Contact contact) throws IllegalStateException{
		model.addAttribute("TB", "Cảm ơn " + contact.getName() + ", chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất!");
		// sau khi xử lí xong đưa contact về trạng thái mới
		model.addAttribute("contact", "");
		contactService.saveOrUpdate(contact);

		return "khachhang/contact";
	
	}

	/**
	 * Sử dụng ajax
	 */
	@RequestMapping(value = { "/ajax/contact"}, method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> ajax_contact(final Model model, final @RequestBody Contact contact) {
		Map<String, Object> jsonResultCt = new HashMap<String, Object>();
		model.addAttribute("contact", "");

		List<Contact> contacts = contactService.checkEmailContact(contact);
		if(contacts.size() == 0) {
			contactService.saveOrUpdate(contact);
			jsonResultCt.put("code", 200);
			jsonResultCt.put("messages", "Cảm ơn, " + contact.getEmail() + " đã đăng kí thành công!");
		} else {
			jsonResultCt.put("err", "Bạn chưa nhập email / Trùng email");
		}
		return ResponseEntity.ok(jsonResultCt);
	}
}
