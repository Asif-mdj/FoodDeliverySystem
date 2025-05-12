package com.twozo.foodDeliverySystem.dao;

import com.twozo.foodDeliverySystem.model.dto.HotelOrderResponse;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Interface for hotel data access operations
 */
public interface HotelDao {

    /**
     * SQL query to get all orders for a specific hotel including food item details
     */
    String GET_HOTEL_ORDERS = "SELECT order_id, customer_id, name, food_item_price " +
            "FROM fds_order " +
            "JOIN fds_ordered_food_item ON fds_order.id = fds_ordered_food_item.order_id " +
            "JOIN fds_hotel_food_item ON fds_ordered_food_item.hotel_food_item_id = fds_hotel_food_item.id " +
            "JOIN fds_food_item ON fds_hotel_food_item.food_item_id = fds_food_item.id " +
            "WHERE hotel_id = ? " +
            "ORDER BY order_id;";

    /**
     * SQL query to update order status to READY_FOR_PICKUP
     */
    String UPDATE_ORDER_STATUS_READY_FOR_PICKUP = "UPDATE fds_order_detail SET order_status = 'READY_FOR_PICKUP' WHERE order_id = ?;";

    /**
     * Gets all orders for a specific hotel
     *
     * @param hotelId ID of the hotel
     * @return List of orders for the hotel
     * @throws SQLException if database access error occurs
     */
    Collection<HotelOrderResponse> getHotelOrders(final int hotelId) throws SQLException;

    /**
     * Updates order status to READY_FOR_PICKUP
     *
     * @param orderId ID of the order to update
     * @return Number of rows updated
     * @throws SQLException if database access error occurs
     */
    int updateOrderStatusReadyForPickup(final int orderId) throws SQLException;

}