package com.twozo.foodDeliverySystem.model;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an order in the food delivery system
 */
@Component
public class Order {

    private int id;
    private OrderStatus status;
    private LocalDateTime dateTime;
    private String deliveryLocation;
    private boolean isAssigned;

    /**
     * Default constructor
     */
    public Order() {
    }

    public Order(int id, OrderStatus status, LocalDateTime dateTime,
                 String deliveryLocation, boolean isAssigned) {
        this.id = id;
        this.status = status;
        this.dateTime = dateTime;
        this.deliveryLocation = deliveryLocation;
        this.isAssigned = isAssigned;
    }

    /**
     * Gets the order ID
     *
     * @return ID of the order
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the order ID
     *
     * @param id ID to set for the order
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the order status
     *
     * @return Status of the order
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the order status
     *
     * @param status Status to set for the order
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Checks if delivery agent is assigned
     *
     * @return true if delivery agent is assigned, false otherwise
     */
    public boolean isAssigned() {
        return isAssigned;
    }

    /**
     * Sets whether delivery agent is assigned
     *
     * @param isAssigned Assignment status to set
     */
    public void setAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
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

}