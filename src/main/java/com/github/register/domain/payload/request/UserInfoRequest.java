package com.github.register.domain.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
public class UserInfoRequest {

    @NotBlank(message = "The username field cannot be blank")
    @Size(max = 20)
    private String username;

    @NotBlank(message = "The email field cannot be blank")
    @Size(max = 50)
    @Email(message = "The email format is illegal")
    private String email;

    private Integer deleted = 0;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
