package com.twozo.foodDeliverySystem.model;

public class MenuItem {

    private int hotelId;
    private int foodItemId;
    private double foodItemPrice;

    public MenuItem(int hotelId, int foodItemId, double foodItemPrice) {
        this.hotelId = hotelId;
        this.foodItemId = foodItemId;
        this.foodItemPrice = foodItemPrice;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public double getFoodItemPrice() {
        return foodItemPrice;
    }

    public void setFoodItemPrice(double foodItemPrice) {
        this.foodItemPrice = foodItemPrice;
    }
}
