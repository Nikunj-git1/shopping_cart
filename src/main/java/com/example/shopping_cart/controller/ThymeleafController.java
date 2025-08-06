package com.example.shopping_cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/thymlf")
public class ThymeleafController {

    @GetMapping("/welcome")
    public String welcome() {

        return "welcome";
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

//    @GetMapping("/admin/cat/get-list")
//    public String getListCat(){
//        return "getListCat";
//    }
}