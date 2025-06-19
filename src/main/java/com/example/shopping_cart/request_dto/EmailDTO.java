package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDTO {

    @NotBlank(message = "Recipient email must not be blank")
    @Email(message = "Invalid email format")
    private String toEmail;

    @NotBlank(message = "Subject must not be blank")
    private String subject;

    @NotBlank(message = "Body must not be blank")
    private String body;

    private MultipartFile attmt;
}
