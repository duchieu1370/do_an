package com.shopHMsic.controller.api;

import com.shopHMsic.entities.*;
import com.shopHMsic.repository.*;
import com.shopHMsic.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private final RoleService roleService;
    private final AreaRepository areaRepository;

    public AdminApiController(RoleService roleService, AreaRepository areaRepository) {
        this.roleService = roleService;
        this.areaRepository = areaRepository;
    }

    // --- ROLES ---
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") int id) {
        Role role = roleService.getById(id);
        if (role != null) {
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/roles")
    public ResponseEntity<Map<String, Object>> saveRole(@RequestBody Role role) {
        Map<String, Object> result = new HashMap<>();
        if (role.getId() == null || role.getId() <= 0) {
            role.setCreatedDate(new java.util.Date());
            roleService.saveOrUpdate(role);
            result.put("message", "Thêm mới nhóm quyền thành công!");
        } else {
            role.setUpdatedDate(new java.util.Date());
            roleService.saveOrUpdate(role);
            result.put("message", "Cập nhật nhóm quyền thành công!");
        }
        result.put("code", 200);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Map<String, Object>> deleteRole(@PathVariable("id") int id) {
        roleService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Đã xóa nhóm quyền thành công!");
        return ResponseEntity.ok(result);
    }

    // --- AREAS ---
    @GetMapping("/provinces")
    public ResponseEntity<List<Area>> getProvinces() {
        return ResponseEntity.ok(areaRepository.findByParentId(0));
    }

    @GetMapping("/wards")
    public ResponseEntity<List<Area>> getWards(@RequestParam("provinceId") Integer provinceId) {
        return ResponseEntity.ok(areaRepository.findByParentId(provinceId));
    }
}
