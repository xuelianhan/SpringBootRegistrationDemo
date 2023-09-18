package com.github.register.application.listener;

import com.github.register.application.EmailService;
import com.github.register.domain.event.UserRegistrationEvent;
import com.github.register.domain.mail.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
@Component
public class UserRegistrationListener {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationListener.class);

    @Autowired
    private EmailService emailService;

    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMPLETION)
    public void handleRegistrationEvent(UserRegistrationEvent event) {
        Email email = new Email();
        email.setSubject("Welcome!");
        email.setBody("Hello World!");
        List<String> receivers = new ArrayList<>();
        receivers.add(event.getEmail());
        email.setRecipients(receivers);
        //emailService.sendSimpleMessage(email);
        log.info("Welcome email send successfully!");
    }
}
