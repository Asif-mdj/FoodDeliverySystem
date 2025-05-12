package com.twozo.foodDeliverySystem.model;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Represents a Hotel entity in the food delivery system.
 * Implements User interface to provide common user functionality.
 */
@Component
@Qualifier("hotel")
public class Hotel extends User {

    public Hotel() {
        super();
    }

    public Hotel(int id, String name, String location, String contact, String password) {
        super(id,name,location,contact, password);
    }

}