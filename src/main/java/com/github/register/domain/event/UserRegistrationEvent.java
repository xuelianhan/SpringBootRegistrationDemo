package com.github.register.domain.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
public class UserRegistrationEvent extends ApplicationEvent {

    private String username;

    private String email;


    public UserRegistrationEvent(Object source, String username, String email) {
        super(source);
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }


}
