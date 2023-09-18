package com.github.register.application;

import com.github.register.domain.mail.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    public Boolean sendSimpleMessage(Email email)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email.getRecipients().stream().collect(Collectors.joining(",")));
        mailMessage.setSubject(email.getSubject());
        mailMessage.setText(email.getBody());

        Boolean isSent = false;
        try
        {
            emailSender.send(mailMessage);
            isSent = true;
        }
        catch (Exception e) {
            log.error("Sending e-mail error: {}", e.getMessage());
        }
        return isSent;
    }

    public Boolean sendMessageWithCC(Email email)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email.getRecipients().stream().collect(Collectors.joining(",")));
        mailMessage.setCc(email.getCcList().stream().collect(Collectors.joining(",")));
        mailMessage.setSubject(email.getSubject());
        mailMessage.setText(email.getBody());

        Boolean isSent = false;
        try
        {
            emailSender.send(mailMessage);
            isSent = true;
        }
        catch (Exception e) {
            log.error("Sending e-mail error: {}", e.getMessage());
        }
        return isSent;
    }


    public Boolean sendMessageWithAttachment(Email email) throws IOException, MessagingException
    {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        messageHelper.setTo(email.getRecipients().stream().collect(Collectors.joining(",")));
        messageHelper.setSubject(email.getSubject());
        messageHelper.setText(email.getBody());

        Resource resource = new ClassPathResource(email.getAttachmentPath());
        messageHelper.addAttachment("attachment", resource.getFile());

        Boolean isSent = false;
        try
        {
            emailSender.send(message);
            isSent = true;
        }
        catch (Exception e) {
            log.error("Sending e-mail with attachment error: {}", e.getMessage());
        }
        return isSent;
    }

}
