package com.github.register.domain.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
public class LoginRequest {
    @NotBlank(message = "The username field cannot be blank")
    private String username;

    @NotBlank(message = "The password field cannot be blank")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
