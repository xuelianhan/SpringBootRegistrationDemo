package com.github.register.domain.payload.response;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
