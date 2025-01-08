package com.yarin.email;

import com.yarin.notification.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public void sendEmail(Notification notification, String emailSubject, String emailContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setTo(notification.getCustomer().email());
            messageHelper.setSubject(emailSubject);
            messageHelper.setText(emailContent, true);

            // Send the email
            mailSender.send(mimeMessage);

            logger.info("Email sent to {} with subject: {}", notification.getCustomer().email(), emailSubject);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", notification.getCustomer().email(), e.getMessage());
        }
    }
}


