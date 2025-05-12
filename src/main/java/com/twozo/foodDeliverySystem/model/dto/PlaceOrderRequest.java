package com.twozo.foodDeliverySystem.model.dto;

import com.twozo.foodDeliverySystem.model.FoodItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PlaceOrderRequest {

    private String customerContact;
    private int hotelId;
    private LocalDateTime dateTime;
    private String deliveryLocation;
    private List<FoodItem> orderedFoodItems;

    public PlaceOrderRequest() {
    }

    public PlaceOrderRequest(String customerContact, int hotelId,
                             LocalDateTime dateTime, String deliveryLocation,
                             List<FoodItem> orderedFoodItems) {
        this.customerContact = customerContact;
        this.hotelId = hotelId;
        this.dateTime = dateTime;
        this.deliveryLocation = deliveryLocation;
        this.orderedFoodItems = orderedFoodItems;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public List<FoodItem> getOrderedFoodItems() {
        return orderedFoodItems;
    }

    public void setOrderedFoodItems(List<FoodItem> orderedFoodItems) {
        this.orderedFoodItems = orderedFoodItems;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
