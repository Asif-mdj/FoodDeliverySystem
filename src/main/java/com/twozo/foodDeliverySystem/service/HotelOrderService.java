package com.twozo.foodDeliverySystem.service;

import com.twozo.foodDeliverySystem.model.dto.HotelOrderResponse;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;

import java.util.Collection;

/**
 * Service interface for managing hotel orders
 */
public interface HotelOrderService {

    /**
     * Updates the status of an order
     *
     * @param id order ID to update
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access error occurs
     */
    int updateOrderStatus(final int id) throws DataBaseAccessError;

    /**
     * Gets all orders for a specific hotel
     *
     * @param id hotel id to get orders
     * @return List of orders for the hotel
     * @throws DataBaseAccessError if database access error occurs
     */
    Collection<HotelOrderResponse> getHotelOrders(final int id) throws DataBaseAccessError;
}