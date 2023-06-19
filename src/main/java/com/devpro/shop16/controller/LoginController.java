package com.devpro.shop16.controller;

import com.devpro.shop16.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController extends BaseController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login(final Model model, final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {

        return "login";
    }


    @PostMapping  (value = "/logout")
    public String logout() {
        return "redirect:/home";
    }

}
