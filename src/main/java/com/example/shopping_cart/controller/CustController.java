package com.example.shopping_cart.controller;

import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.example.shopping_cart.comman_response_dto.ResGenerator;
import com.example.shopping_cart.request_dto.CustDTO;
import com.example.shopping_cart.request_dto.CustDTOUpdate;
import com.example.shopping_cart.request_dto.CustLoginDTO;
import com.example.shopping_cart.security.JwtHelper;
import com.example.shopping_cart.service_impl.CustServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer API", description = "For CRUD operation of customers.")

@Validated
public class CustController {

    @Autowired
    CustServiceImpl customerServiceImpl;

    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody @Valid CustDTO custDTO) {

        customerServiceImpl.signup(custDTO);

        return ResGenerator.success("Signup successfully", null);
    }


    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody @Valid CustLoginDTO custLoginDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(custLoginDTO.getCustName(), custLoginDTO.getPswd()));
        String token = JwtHelper.generateToken(custLoginDTO.getCustName());

        return ResGenerator.success("Login successful", token);
    }


    @GetMapping("/get-list")
    public ResponseEntity<CommonResponse> getList() {

        return ResGenerator.success("List of customer", customerServiceImpl.getList());
    }


    @PutMapping("/update")
    public ResponseEntity<CommonResponse> update(@RequestBody @Valid CustDTOUpdate custDTOUpdate) {

        return ResGenerator.success("Customer updated successfully", customerServiceImpl.update(custDTOUpdate));
    }


    @DeleteMapping("/delete/{custId}")
    public ResponseEntity<CommonResponse> delete(@PathVariable(required = true)
                                                 @NotNull(message = "Customer ID must not be null")
                                                 @Positive(message = "Customer ID must be a positive number")
                                                 Integer custId) {

        return ResGenerator.success("Customer delete successfully", customerServiceImpl.delete(custId));
    }
}