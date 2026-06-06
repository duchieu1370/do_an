package com.shopHMsic.controller.api;

import com.shopHMsic.dto.BaseResponse;
import com.shopHMsic.dto.ContactSearchModel;
import com.shopHMsic.dto.GetResponseDTO;
import com.shopHMsic.entities.Contact;
import com.shopHMsic.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminContactApiController {

    private final ContactService contactService;

    public AdminContactApiController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contacts/search-list")
    public ResponseEntity<?> getContacts(
            @RequestBody ContactSearchModel searchModel) {
        if (searchModel.getSize() == null) {
            searchModel.setSize(20);
        }
        if (searchModel.getPage() == null) {
            searchModel.setPage(1);
        }
        Pageable pageable = PageRequest.of(searchModel.getPage() - 1, searchModel.getSize());
        Page<Contact> responses = contactService.getListContact(searchModel, pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(responses.getTotalElements()))
                .body(BaseResponse.<List<?>>builder()
                        .data(responses.getContent())
                        .total(responses.getTotalElements())
                        .build());
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable("id") int id) {
        Contact contact = contactService.getById(id);
        if (contact != null) {
            return ResponseEntity.ok(contact);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/contacts/{id}/status")
    public ResponseEntity<?> updateContactStatus(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> body) throws Exception {
        Boolean status = (Boolean) body.get("status");
        if (status == null) {
            GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(400)
                    .message("Trạng thái không được để trống!")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        contactService.updateContactStatus(id, status);
        GetResponseDTO responseDTO = GetResponseDTO.builder()
                    .code(200)
                    .message("Cập nhật trạng thái liên hệ thành công!")
                    .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<Map<String, Object>> deleteContact(@PathVariable("id") int id) throws Exception {
        contactService.deleteContact(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa thư liên hệ thành công!");
        return ResponseEntity.ok(result);
    }
}
