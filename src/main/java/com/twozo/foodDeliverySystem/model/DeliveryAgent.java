package com.twozo.foodDeliverySystem.model;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Represents a delivery agent in the food delivery system.
 * Implements the User interface and adds delivery agent specific attributes.
 */
@Component
@Qualifier("deliveryAgent")
public class DeliveryAgent extends User {

    public DeliveryAgent() {
        super();
    }

    public DeliveryAgent(int id, String name, String location, String contact, String password) {
        super(id,name,location,contact, password);
    }

}