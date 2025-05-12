package com.twozo.foodDeliverySystem.model;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Represents a customer in the food delivery system.
 * This class implements the User interface and manages customer-specific information
 * such as user ID, name, location, contact details, and password.
 */
@Component
@Qualifier("customer")
public class Customer extends User {

    public Customer() {
        super();
    }

    public Customer(int id, String name, String location, String contact, String password) {
        super(id,name,location,contact, password);
    }


}
