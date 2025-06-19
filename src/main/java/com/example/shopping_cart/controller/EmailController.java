package com.example.shopping_cart.controller;

import com.example.shopping_cart.request_dto.EmailDTO;
import com.example.shopping_cart.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/email")

@Validated
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-attachment")
    public String sendEmailWithAttachment(@RequestParam (required = true)
                                          @NotBlank(message = "Recipient email must not be blank")
                                          @Email(message = "Invalid email format")
                                          String toEmail,

                                          @RequestParam (required = true)
                                          @NotBlank(message = "Subject must not be blank")
                                          String subject,

                                          @RequestParam (required = true)
                                          @NotBlank(message = "Body must not be blank")
                                          String body,

                                          @RequestParam (required = false)
                                          MultipartFile attmt) throws MessagingException, IOException {

        emailService.sendEmailWithAttachment(toEmail, subject, body, attmt);

        return "Email sent with attachment!";

    }


    //    Form-data convert to DTO
    @PostMapping("/send-attachment2")
    public String sendEmailWithAttachment2(@ModelAttribute @Valid EmailDTO emailDTO) throws MessagingException, IOException {

        emailService.sendEmailWithAttachment2(emailDTO);

        return "Email sent with attachment!";

    }
}