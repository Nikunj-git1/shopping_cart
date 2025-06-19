package com.example.shopping_cart.service;

import com.example.shopping_cart.request_dto.EmailDTO;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service

public interface EmailService {

    void sendEmailWithAttachment(String toEmail, String subject, String body, MultipartFile attachment) throws MessagingException, IOException;

    void sendEmailWithAttachment2(EmailDTO emailDTO) throws MessagingException, IOException;

}