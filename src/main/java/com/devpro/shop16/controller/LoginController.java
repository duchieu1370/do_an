package com.devpro.shop16.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class LoginController extends BaseController {

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() throws IOException {

        return "login";
    }


    @PostMapping  (value = "/logout")
    public String logout() {
        return "redirect:/home";
    }

}
