package com.github.register.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailProviderConfig {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private Boolean auth;

}
