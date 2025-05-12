package com.twozo.foodDeliverySystem.model;

/**
 * Enum representing different states of an order in the food delivery system.
 */
public enum OrderStatus {

    /**
     * Order is being prepared by the hotel
     */
    UNDER_PREPARATION,

    /**
     * Order is prepared and ready to be picked up by a delivery partner
     */
    READY_FOR_PICKUP,

    /**
     * Order has been picked up and is being delivered to the customer
     */
    OUT_FOR_DELIVERY,

    /**
     * Order has been successfully delivered to the customer
     */
    DELIVERED

}