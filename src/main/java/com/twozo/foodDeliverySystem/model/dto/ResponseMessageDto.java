package com.twozo.foodDeliverySystem.model.dto;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessageDto {

    private String message;

    public ResponseMessageDto() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
