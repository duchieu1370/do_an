package com.devpro.shop16.controller;

import com.devpro.shop16.entities.Role;
import com.devpro.shop16.entities.User;
import com.devpro.shop16.service.RoleService;
import com.devpro.shop16.service.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RegisterController extends BaseController{

    final UserService userService;
    private final RoleService roleService;

    public RegisterController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }



    @RequestMapping(value = { "/register" }, method = RequestMethod.GET)
    public String register(final Model model) throws IOException {
        model.addAttribute("user", new User());

        return "register";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String post_spring_form( final @ModelAttribute("user") User user) throws IllegalStateException{
        Map<String, Object> jsonResultCt = new HashMap<>();
        List<User> usersMail = userService.checkEmailRegister(user);
        List<User> usersName = userService.checkUserNameRegister(user);
        if(usersMail.size() == 0){
            if(usersName.size() == 0){
                user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4)));
                Role role = roleService.loadRoleByRoleName("GUEST");
                user.addRoles(role);
                userService.saveOrUpdate(user);
            }
        } else {
            jsonResultCt.put("message","Tài khoản hoặc email của bạn đã được đăng ký!");
        }

        return "register";
    }


}
