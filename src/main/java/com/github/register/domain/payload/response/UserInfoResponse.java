package com.github.register.domain.payload.response;

import java.util.List;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
public class UserInfoResponse {

    private Integer id;
    private String username;
    private String email;
    private List<String> roles;
    private UserInfoResponse() {}

    public static class Builder {
        private Integer id;
        private String username;
        private String email;
        private List<String> roles;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }
    public Builder username(String username) {
        this.username = username;
        return this;
    }

    public Builder email(String email) {
        this.email = email;
        return this;
    }

    public Builder roles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public UserInfoResponse build() {
        UserInfoResponse response = new UserInfoResponse();
        response.id = this.id;
        response.username = this.username;
        response.email = this.email;
        response.roles = this.roles;
        return response;
    }
}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
