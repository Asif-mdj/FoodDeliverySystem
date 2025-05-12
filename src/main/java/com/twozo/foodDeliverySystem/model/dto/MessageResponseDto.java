package com.twozo.foodDeliverySystem.model.dto;

import org.springframework.stereotype.Component;

@Component
public class MessageResponseDto {

    private String message;

    public MessageResponseDto() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
