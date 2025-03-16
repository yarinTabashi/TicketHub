package com.yarin.kafka;

import com.yarin.common_dtos.CustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableKafka
public class NotificationsConsumer {
    // private EmailService emailService;

    @KafkaListener(topics = "customer-events", groupId = "notification-group")
    public void handleCustomerEvents(CustomerEvent event){
        switch (event.type()){
            case REGISTERED:
                handleCustomerRegisteredEvent(event);
                break;
            case UPDATED:
                handleCustomerUpdatedEvent(event);
                break;
            case DELETED:
                handleCustomerDeletedEvent(event);
                break;
            default:
                log.error("Cannot determine the event type: {}", event.type());
        }
    }

    public void handleCustomerRegisteredEvent(CustomerEvent event){
        String emailContent = String.format("Hello %s, Welcome to our platform!", event.fullName());
        sendEmail(event.email(), "Customer Registered", emailContent);
    }

    public void handleCustomerUpdatedEvent(CustomerEvent event){
        String emailContent = String.format("Hello %s, Your details have been successfully updated.", event.fullName());
        sendEmail(event.email(), "Customer Details Updated", emailContent);
    }

    public void handleCustomerDeletedEvent(CustomerEvent event){
        String emailContent = String.format("Hello %s, We're sorry to see you go. Your account has been deleted.", event.fullName());
        sendEmail(event.email(), "Customer Account Deleted", emailContent);
    }

    public void sendEmail(String email, String subject, String content){
        log.info("To: {} \n Subject: {} \n Content: {} ", email, subject, content);
        // TODO: send the email using the email service
    }
}