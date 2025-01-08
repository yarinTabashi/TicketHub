package com.yarin.kafka;

import com.yarin.common_dtos.Customer;
import com.yarin.common_dtos.Order;
import com.yarin.email.EmailService;
import com.yarin.notification.Notification;
import com.yarin.notification.NotificationRepository;
import com.yarin.notification.dtos.DeliveryStatus;
import com.yarin.notification.dtos.NotificationReason;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableKafka
public class NotificationsConsumer {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "order-topic", groupId = "notifications-consumer-group")
    public void listenOrderConfirmation(Customer customer, Order order) throws MessagingException {
        log.info("Processing ORDER_CONFIRMATION message from 'order-topic'");

        // Save notification in the database
        Notification notification = saveNotification(customer, order, NotificationReason.ORDER_CONFIRMATION);

        // Send the order confirmation email
        emailService.sendEmail(notification, "ORDER CONFIRMATION", "Thank for your order!");
    }

    @KafkaListener(topics = "upcoming-reminder-topic", groupId = "notifications-consumer-group")
    public void listenUpcomingReminder(Customer customer, Order order) throws MessagingException {
        log.info("Processing REMINDER message from 'upcoming-reminder-topic'");

        // Save notification in the database
        Notification notification = saveNotification(customer, order, NotificationReason.REMINDER);

        // Send the order confirmation email
        emailService.sendEmail(notification, "UPCOMING REMINDER", "Thank for your order!");
    }

    /**
     * Listen for welcome messages after registration from Kafka topic
     */
    @KafkaListener(topics = "welcome-topic", groupId = "notifications-consumer-group")
    public void listenWelcomeAfterRegister(Customer customer) throws MessagingException {
        log.info("Processing WELCOME_AFTER_REGISTER message from 'welcome-topic'");

        // Save notification in the database
        Notification notification = saveNotification(customer, null, NotificationReason.WELCOME_AFTER_REGISTER);

        // Send the welcome email
        emailService.sendEmail(notification, "Welcome!", "Thank you for registering.");
    }

    /**
     * Helper method to save notifications in the database.
     */
    private Notification saveNotification(Customer customer, Order optionalOrder, NotificationReason reason) {
        // Create the notification
        Notification notification = Notification.builder()
                .notificationReason(reason)
                .deliveryStatus(DeliveryStatus.IN_PROGRESS)
                .sentAt(LocalDateTime.now())
                .customer(customer)
                .order(optionalOrder)
                .build();

        // Save the notification to the repository
        notificationRepository.save(notification);

        // Return the saved notification
        return notification;
    }
}
