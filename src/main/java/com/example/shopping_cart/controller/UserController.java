package com.example.shopping_cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api")
@Tag(name = "User API", description = "Signup, Login")
public class UserController {


//only for knowledge-how to spring internally work

    @GetMapping("/user")
    @Operation(summary = "Get user details")
    public String getCurrentUserDetails(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "Logged in as: " + user.getUsername();
        } else {
            return "No user is logged in.";
        }
    }
}