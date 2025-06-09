package com.example.shopping_cart.request_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDTO {

    private String toEmail;
    private String subject;
    private String body;
    private MultipartFile attmt;
}
