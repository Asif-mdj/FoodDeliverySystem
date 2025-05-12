package com.twozo.foodDeliverySystem.model;

import org.springframework.stereotype.Component;

/**
 * Represents a food item in the food delivery system
 */
@Component
public class FoodItem {

    /**
     * Unique identifier for the food item
     */
    private int id;

    /**
     * Name of the food item
     */
    private String name;

    /**
     * Price of the food item
     */
    private double price;

    /**
     * Default constructor
     */
    public FoodItem() {
    }

    /**
     * Constructor with name and price
     *
     * @param name  Name of the food item
     * @param price Price of the food item
     */
    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Constructor with ID and name
     *
     * @param id   ID of the food item
     * @param name Name of the food item
     */
    public FoodItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructor with ID and price
     *
     * @param id    ID of the food item
     * @param price Price of the food item
     */
    public FoodItem(int id, double price) {
        this.id = id;
        this.price = price;
    }

    /**
     * Constructor with all fields
     *
     * @param id ID of the food item
     * @param foodName   Name of the food item
     * @param foodPrice  Price of the food item
     */
    public FoodItem(int id, String foodName, double foodPrice) {
        this.id = id;
        this.name = foodName;
        this.price = foodPrice;
    }

    /**
     * Gets the food item ID
     *
     * @return ID of the food item
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the food item ID
     *
     * @param id ID to set for the food item
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the food item name
     *
     * @return Name of the food item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the food item name
     *
     * @param name Name to set for the food item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the food item price
     *
     * @return Price of the food item
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the food item price
     *
     * @param price Price to set for the food item
     */
    public void setPrice(double price) {
        this.price = price;
    }

}
