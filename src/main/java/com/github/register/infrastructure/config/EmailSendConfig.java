package com.github.register.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author sniper
 * @date 18 Sep 2023
 *
 */
@Configuration
public class EmailSendConfig {

    @Autowired
    private EmailProviderConfig emailProviderConfig;

    @Bean
    public JavaMailSender mailSender()
    {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailProviderConfig.getHost());
        javaMailSender.setPort(emailProviderConfig.getPort());

        javaMailSender.setUsername(emailProviderConfig.getUsername());
        javaMailSender.setPassword(emailProviderConfig.getPassword());

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return javaMailSender;
    }
}
