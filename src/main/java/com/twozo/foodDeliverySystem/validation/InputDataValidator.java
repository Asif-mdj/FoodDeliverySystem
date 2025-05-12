package com.twozo.foodDeliverySystem.validation;

import com.twozo.foodDeliverySystem.model.OrderStatus;

import java.time.LocalDateTime;

public interface InputDataValidator {

    boolean validateId(final int id);

    boolean validateName(final String name);

    boolean validateContact(final String contact);

    boolean validateLocation(final String location);

    boolean validatePassword(final String password);

    boolean validateDateTime(final LocalDateTime localDateTime);

    boolean validatePrice(final double price);

    boolean validateAvailability(final boolean status);

    boolean validateUserType(final String userType);

    boolean validateOrderStatus(final OrderStatus orderStatus);

}
