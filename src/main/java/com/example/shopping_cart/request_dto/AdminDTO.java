package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdminDTO {

    @NotBlank(message = "Admin name is required")
    private String adminName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 1, max = 20, message = "Password must be between 1 and 20 characters")
    private String pswd;
}