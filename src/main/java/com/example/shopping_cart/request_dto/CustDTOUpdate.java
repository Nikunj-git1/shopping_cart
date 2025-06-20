package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustDTOUpdate {

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a positive number")
    private Integer custId;

    @NotBlank(message = "Customer name is required")
    private String custName;

    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "\\d{12}", message = "Aadhaar number must contain exactly 12 digits")
    private String aadhaarNo;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 1, max = 20, message = "Password must be between 1 and 20 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pswd;
}