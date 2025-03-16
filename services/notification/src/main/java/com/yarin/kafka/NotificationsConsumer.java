package com.yarin.kafka;

import com.yarin.common_dtos.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableKafka
public class NotificationsConsumer {
    //private final EmailService emailService;

    @KafkaListener(topics = "customer-events", groupId = "notification-group")
    public void handleUserRegisteredEvent(ConsumerRecord<String, UserRegisteredEvent> record){
        UserRegisteredEvent event = null;
        try {
            event = record.value();
            System.out.println("Received message: " + record.value());

            log.info("Received UserRegisteredEvent: {}", event);

            sendWelcomeEmail(event.email(), event.fullName());
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    public void sendWelcomeEmail(String email, String fullName){
        log.info("Sending welcome email to: {} ({})", fullName, email);
        // TODO: send the email using the email service
    }
}