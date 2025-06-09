package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.request_dto.EmailDTO;
import com.example.shopping_cart.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachment(String toEmail, String subject,
                                        String body, MultipartFile attachment) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();

//        mailSender = वह चीज़ जो ईमेल भेजती है (जैसे डाकिया)।
//        createMimeMessage() = एक नया ईमेल पेज (message) बनाता है जिस पर हम To, Subject, Body, और Attachment लिख सकते हैं।

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        अगर आप सीधे MimeMessage का इस्तेमाल करें तो आपको manually header set करने पड़ते हैं — यह मुश्किल और जटिल हो सकता है।

//        helper.setFrom("your-email@gmail.com"); // already set in Property
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);

        if (attachment != null && !attachment.isEmpty()) {
            helper.addAttachment(attachment.getOriginalFilename(), new ByteArrayResource(attachment.getBytes()));

//            attachment.getOriginalFilename() – अटैचमेंट का नाम लेता है।
//            attachment.getBytes() – अटैचमेंट के कंटेंट को बाइट्स में कनवर्ट करता है।
//            ByteArrayResource – बाइट्स को एक resource में बदलता है जिसे ईमेल के साथ अटैच किया जा सके।
        }

        mailSender.send(message);
        System.out.println("Email with attachment sent...");
    }


    public void sendEmailWithAttachment2(EmailDTO emailDTO) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(emailDTO.getToEmail());
        helper.setSubject(emailDTO.getSubject());
        helper.setText(emailDTO.getBody());

        if (emailDTO.getAttmt()!= null && !emailDTO.getAttmt().isEmpty()) {
            helper.addAttachment(emailDTO.getAttmt().getOriginalFilename(),
                    new ByteArrayResource(emailDTO.getAttmt().getBytes()));
        }

        mailSender.send(message);
        System.out.println("Email with attachment sent...");
    }

}
