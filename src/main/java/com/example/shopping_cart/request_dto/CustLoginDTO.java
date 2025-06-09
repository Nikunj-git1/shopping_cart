package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustLoginDTO {

    @NotBlank(message = "Customer name is required")
    private String custName;

    @NotBlank(message = "Password is required")
    private String pswd;

}
