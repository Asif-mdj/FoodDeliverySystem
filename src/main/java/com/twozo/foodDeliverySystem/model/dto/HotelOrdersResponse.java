package com.twozo.foodDeliverySystem.model.dto;

import com.twozo.foodDeliverySystem.model.FoodItem;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class HotelOrdersResponse {

    private int orderId;
    private int customerId;
    private Collection<FoodItem> foodItems;

    public HotelOrdersResponse() {
    }

    public HotelOrdersResponse(int orderId, int customerId, Collection<FoodItem> foodItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.foodItems = foodItems;
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
    public Collection<FoodItem> getFoodItems() {
        return foodItems;
    }
    public void setFoodItems(Collection<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

}
