package com.example.shopping_cart.controller;

import com.example.shopping_cart.request_dto.EmailDTO;
import com.example.shopping_cart.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-attachment")
    public String sendEmailWithAttachment(@RequestParam String toEmail,
                                          @RequestParam String subject,
                                          @RequestParam String body,
                                          @RequestParam MultipartFile attmt) throws MessagingException, IOException {

        emailService.sendEmailWithAttachment(toEmail, subject, body, attmt);

        return "Email sent with attachment!";

    }

//    Form-data convert to DTO
    @PostMapping("/send-attachment2")
    public String sendEmailWithAttachment2(@ModelAttribute EmailDTO emailDTO) throws MessagingException, IOException {

        emailService.sendEmailWithAttachment2(emailDTO);

        return "Email sent with attachment!";

    }
}
