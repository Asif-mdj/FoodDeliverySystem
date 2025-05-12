package com.twozo.foodDeliverySystem.service;

import com.twozo.foodDeliverySystem.model.OrderStatus;
import com.twozo.foodDeliverySystem.model.dto.OrderListResponse;
import com.twozo.foodDeliverySystem.model.dto.OrderPlacementRequest;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.FoodItem;

import java.util.Collection;

/**
 * Service interface for managing food delivery orders
 */
public interface OrderService {

    /**
     * Places a new food order
     *
     * @param orderPlacementRequest with details od order to be placed.
     * Contains customer ID, hotel ID, ordered food items and order status.
     * @return ID of the placed order
     * @throws DataBaseAccessError if the database operation fails
     */
    int placeOrder(final OrderPlacementRequest orderPlacementRequest) throws DataBaseAccessError;

    /**
     * Gets a list of orders available for pickup
     *
     * @return List of available orders
     * @throws DataBaseAccessError if the database operation fails
     */
    Collection<OrderListResponse> getAvailableOrders() throws DataBaseAccessError;

    /**
     * Gets a list of orders allocated to delivery agents
     *
     * @return List of allocated orders
     * @throws DataBaseAccessError if the database operation fails
     */
    Collection<OrderListResponse> getAllocatedOrders() throws DataBaseAccessError;

    /**
     * Gets a list of completed orders
     *
     * @return List of completed orders
     * @throws DataBaseAccessError if the database operation fails
     */
    Collection<OrderListResponse> getCompletedOrders() throws DataBaseAccessError;

    /**
     * Gets count of orders available for pickup
     *
     * @return Number of available orders
     * @throws DataBaseAccessError if the database operation fails
     */
    int getAvailableOrdersCount() throws DataBaseAccessError;

    /**
     * Gets count of orders allocated to delivery agents
     *
     * @return Number of allocated orders
     * @throws DataBaseAccessError if the database operation fails
     */
    int getAllocatedOrdersCount() throws DataBaseAccessError;

    /**
     * Gets count of completed orders
     *
     * @return Number of completed orders
     * @throws DataBaseAccessError if the database operation fails
     */
    int getCompletedOrderCount() throws DataBaseAccessError;


    Collection<FoodItem> getOrderedFoodItem(final int orderId) throws DataBaseAccessError;
    /**
     * Assigns an order to a delivery agent
     *
     * @param orderId ID of the order
     * @param deliveryAgentId ID of the delivery agent
     * @return Number of rows affected
     * @throws DataBaseAccessError if the database operation fails
     */
    int acceptOrder(final int orderId, final int deliveryAgentId) throws DataBaseAccessError;

    /**
     * Marks an order as completed by the delivery agent
     *
     * @param orderId ID of the order
     * @param orderStatus order status either Ready for pickup or Delivered
     * @return Number of rows affected
     * @throws DataBaseAccessError if the database operation fails
     */
    int updateOrderStatus(final int orderId, final OrderStatus orderStatus) throws DataBaseAccessError;

}
