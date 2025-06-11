package com.example.shopping_cart.controller;

import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.example.shopping_cart.comman_response_dto.ResGenerator;
import com.example.shopping_cart.request_dto.AdminDTO;
import com.example.shopping_cart.security.JwtHelper;
import com.example.shopping_cart.service_impl.AdminServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
@Tag(name = "Admin API", description = "Signup and Login.")


public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    AdminServiceImpl adminServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@Valid @RequestBody AdminDTO adminDTO) {

        adminServiceImpl.signup(adminDTO);

        return ResGenerator.success("Signup successfully", null);
    }


    @PostMapping("/login")
    public ResponseEntity<CommonResponse> adminLogin(@Valid @RequestBody AdminDTO adminDTO) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                adminDTO.getAdminName(), adminDTO.getPswd()));
        String token = JwtHelper.generateToken(adminDTO.getAdminName());

        return ResGenerator.success("Login successful", token);
    }
}