package com.twozo.foodDeliverySystem.model.dto;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderListResponse {

    private int orderId;
    private int customerId;
    private String customerContact;
    private int hotelId;
    private LocalDateTime dateTime;
    private String deliveryLocation;
    private int deliveryAgentId;

    public OrderListResponse() {
    }

    public OrderListResponse(int orderId, int customerId, String customerContact, int hotelId, LocalDateTime dateTime, String deliveryLocation, int deliveryAgentId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerContact = customerContact;
        this.hotelId = hotelId;
        this.dateTime = dateTime;
        this.deliveryLocation = deliveryLocation;
        this.deliveryAgentId = deliveryAgentId;
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public int getHotelId() {
        return hotelId;
    }
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public String getDeliveryLocation() {
        return deliveryLocation;
    }
    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }
    public int getDeliveryAgentId() {
        return deliveryAgentId;
    }
    public void setDeliveryAgentId(int deliveryAgentId) {
        this.deliveryAgentId = deliveryAgentId;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

}
