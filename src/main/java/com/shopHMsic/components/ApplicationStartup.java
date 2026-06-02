package com.shopHMsic.components;

import com.shopHMsic.entities.Role;
import com.shopHMsic.entities.User;
import com.shopHMsic.entities.UserRole;
import com.shopHMsic.enums.RoleName;
import com.shopHMsic.repository.RoleRepository;
import com.shopHMsic.repository.UserRepository;
import com.shopHMsic.repository.UserRoleRepository;
import com.shopHMsic.service.RoleService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleService roleService;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ApplicationStartup(RoleService roleService, UserRoleRepository userRoleRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        System.out.println("=== STARTING DATA SEEDING IN APPLICATION STARTUP ===");
        try {
            seedRoles();
            seedDefaultUsers();
            System.out.println("=== DATA SEEDING COMPLETED SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("Error during data seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seedRoles() {
        for (RoleName roleName : RoleName.values()) {
            Role existingRole = roleService.loadRoleByRoleName(roleName.name());
            if (existingRole == null) {
                System.out.println("Seeding role: " + roleName.name());
                Role role = new Role();
                role.setName(roleName.name());
                role.setStatus(true);
                role.setCreatedDate(new Date());

                if (roleName == RoleName.ADMIN) {
                    role.setDescription("Quản trị viên hệ thống");
                } else if (roleName == RoleName.STAFF) {
                    role.setDescription("Nhân viên quản lý sản phẩm");
                } else if (roleName == RoleName.GUEST) {
                    role.setDescription("Khách hàng / Người dùng");
                }

                roleService.saveOrUpdate(role);
            }
        }
    }

    private void seedDefaultUsers() {
        // Create admin user if not exists
        List<User> existingAdmin = userRepository.findByUserNameRegister("ADMIN123");
        if (existingAdmin == null || existingAdmin.isEmpty()) {
            System.out.println("Seeding default admin user...");
            User admin = new User();
            admin.setUsername("ADMIN123");
            admin.setPassword(BCrypt.hashpw("123456aB@", BCrypt.gensalt(4)));
            admin.setEmail("admin@shop.com");
            admin.setAddress("Hà Nội, Việt Nam");
            admin.setPhone("0123456789");
            admin.setStatus(true);
            admin.setCreatedDate(new Date());
            User userNew = userRepository.save(admin);

            Role adminRole = roleRepository.findByName(RoleName.ADMIN.name());
            if (adminRole != null) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userNew.getId());
                userRole.setRoleId(adminRole.getId());
                userRole.setCreatedDate(new Date());
                userRoleRepository.save(userRole);
            }
        }

        // Create staff user if not exists
        List<User> existingStaff = userRepository.findByUserNameRegister("STAFF123");
        if (existingStaff == null || existingStaff.isEmpty()) {
            System.out.println("Seeding default staff user...");
            User staff = new User();
            staff.setUsername("STAFF123");
            staff.setPassword(BCrypt.hashpw("123456aB@", BCrypt.gensalt(4)));
            staff.setEmail("staff@shop.com");
            staff.setAddress("Đà Nẵng, Việt Nam");
            staff.setPhone("0987654321");
            staff.setStatus(true);
            staff.setCreatedDate(new Date());
            User userNew = userRepository.save(staff);

            Role staffRole = roleRepository.findByName(RoleName.STAFF.name());
            if (staffRole != null) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userNew.getId());
                userRole.setRoleId(staffRole.getId());
                userRole.setCreatedDate(new Date());
                userRoleRepository.save(userRole);
            }
        }
    }
}
