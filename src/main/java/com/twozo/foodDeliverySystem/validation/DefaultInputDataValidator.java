package com.twozo.foodDeliverySystem.validation;

import com.twozo.foodDeliverySystem.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DefaultInputDataValidator implements InputDataValidator {

    private static final String USER_TYPE_CUSTOMER = "customer";
    private static final String USER_TYPE_HOTEL = "hotel";
    private static final String USER_TYPE_DELIVERY_AGENT = "deliveryAgent";

    @Override
    public boolean validateId(final int userId) {
        final Pattern pattern = Pattern.compile("^[0-9]+$");
        final Matcher matcher = pattern.matcher(String.valueOf(userId));
        return matcher.find();
    }

    @Override
    public boolean validateName(final String name) {
        final Pattern pattern = Pattern.compile("^[a-zA-Z]{3,20}");
        final Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    @Override
    public boolean validateContact(final String contact) {
        final Pattern pattern = Pattern.compile("^([6-9][0-9]{9})$");
        final Matcher matcher = pattern.matcher(contact);
        return matcher.find();
    }

    @Override
    public boolean validateLocation(final String location) {
        final Pattern pattern = Pattern.compile("^[A-Za-z0-9\\s.'-]+,\\s?[A-Za-z\\s]+$");
        final Matcher matcher = pattern.matcher(location);
        return matcher.find();
    }

    @Override
    public boolean validatePassword(final String password) {
        final Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        final Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    @Override
    public boolean validateDateTime(final LocalDateTime dateTime) {
        final Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$");
        final Matcher matcher = pattern.matcher(String.valueOf(dateTime));
        return matcher.find();
    }

    @Override
    public boolean validatePrice(final double price) {
        final Pattern pattern = Pattern.compile("^\\d+\\.\\d{2}$");
        final Matcher matcher = pattern.matcher(String.valueOf(price));
        return !matcher.find();
    }

    @Override
    public boolean validateAvailability(final boolean availability) {
        final Pattern pattern = Pattern.compile("^(true|false)$");
        final Matcher matcher = pattern.matcher(String.valueOf(availability));
        return matcher.find();
    }

    @Override
    public boolean validateUserType(final String userType) {
        if(userType.equals(USER_TYPE_CUSTOMER)) {
            return true;
        } else if(userType.equals(USER_TYPE_HOTEL)) {
            return true;
        } else
            return userType.equals(USER_TYPE_DELIVERY_AGENT);
    }

    @Override
    public boolean validateOrderStatus(final OrderStatus orderStatus) {
        final String status = orderStatus.toString();
        if(status.equals("READY_FOR_PICKUP")) {
            return true;
        } else if(status.equals("OUT_FOR_DELIVERY")) {
            return true;
        } else
            return status.equals("DELIVERED");
    }

}
