package com.twozo.foodDeliverySystem.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.twozo.foodDeliverySystem.model.dto.OrderListResponse;
import com.twozo.foodDeliverySystem.model.FoodItem;
import com.twozo.foodDeliverySystem.model.Order;
import com.twozo.foodDeliverySystem.model.OrderStatus;

/**
 * Interface for order data access operations
 */
public interface OrderDao {

    // SQL queries for order operations
    String GENERATE_ORDER_ID = "INSERT INTO fds_order ( customer_id ) VALUES ( ? ) RETURNING id;";
    String ADD_ORDER_DETAILS = "INSERT INTO fds_order_detail ( order_id, order_date, order_status, is_assigned, delivery_location )  VALUES (?,?,?,?,?);";
    String ADD_ORDER_FOOD_ITEM = "INSERT INTO fds_ordered_food_item ( order_id, hotel_food_item_id) VALUES (?,?);";
    String GET_ORDERED_FOOD_ITEMS = "SELECT food_item_id, name, food_item_price FROM fds_ordered_food_item LEFT JOIN fds_hotel_food_item ON fds_ordered_food_item.hotel_food_item_id = fds_hotel_food_item.id JOIN fds_food_item ON fds_hotel_food_item.food_item_id = fds_food_item.id WHERE fds_ordered_food_item.order_id = ?;";
    String UPDATE_ORDER_STATUS = "UPDATE fds_order_detail SET order_status = ? WHERE order_id = ?;";
    String DELETE_ORDER = "DELETE FROM fds_order WHERE id = ?;";
    String GET_AVAILABLE_ORDERS = "SELECT fds_order.id, customer_id, (SELECT contact FROM fds_customer WHERE id = customer_id) AS customer_contact, hotel_id, order_date, delivery_location, delivery_agent_id FROM fds_order_detail JOIN fds_order ON fds_order_detail.order_id = fds_order.id JOIN fds_ordered_food_item ON fds_order.id = fds_ordered_food_item.order_id JOIN fds_hotel_food_item ON fds_ordered_food_item.hotel_food_item_id = fds_hotel_food_item.id WHERE order_status = 'READY_FOR_PICKUP' ORDER BY id ASC;";
    String GET_ALLOCATED_ORDERS = "SELECT fds_order.id, customer_id, (SELECT contact FROM fds_customer WHERE id = customer_id) AS customer_contact, hotel_id, order_date, delivery_location, delivery_agent_id FROM fds_order_detail JOIN fds_order ON fds_order_detail.order_id = fds_order.id JOIN fds_ordered_food_item ON fds_order.id = fds_ordered_food_item.order_id JOIN fds_hotel_food_item ON fds_ordered_food_item.hotel_food_item_id = fds_hotel_food_item.id WHERE order_status = 'OUT_FOR_DELIVERY' ORDER BY id ASC;";
    String GET_COMPLETED_ORDERS = "SELECT fds_order.id, customer_id, (SELECT contact FROM fds_customer WHERE id = customer_id) AS customer_contact, hotel_id, order_date, delivery_location, delivery_agent_id FROM fds_order_detail JOIN fds_order ON fds_order_detail.order_id = fds_order.id JOIN fds_ordered_food_item ON fds_order.id = fds_ordered_food_item.order_id JOIN fds_hotel_food_item ON fds_ordered_food_item.hotel_food_item_id = fds_hotel_food_item.id WHERE order_status = 'DELIVERED' ORDER BY id ASC;";
    String GET_AVAILABLE_ORDER_COUNT = "SELECT COUNT(order_id) FROM fds_order_detail WHERE order_status = 'READY_FOR_PICKUP';";
    String GET_ALLOCATED_ORDER_COUNT = "SELECT COUNT(order_id) FROM fds_order_detail WHERE order_status = 'OUT_FOR_DELIVERY';";
    String GET_COMPLETED_ORDER_COUNT = "SELECT COUNT(order_id) FROM fds_order_detail WHERE order_status = 'DELIVERED';";
    String ASSIGN_ORDER_TO_AGENT = "UPDATE fds_order_detail SET order_status = ?, is_assigned = true, delivery_agent_id = ? WHERE order_id = ?;";

    public int addOrder(final int customerId, Order order, final List<Integer> menuIds) throws SQLException;
    /**
     * Gets food items in an order
     *
     * @param orderId ID of order
     * @return List of food items in order
     * @throws SQLException if database access error occurs
     */
    Collection<FoodItem> getOrderedFoodItem(final int orderId) throws SQLException;

    /**
     * Updates status of an order
     *
     * @param orderStatus New order status
     * @param id          Order ID
     * @return Number of rows affected
     * @throws SQLException if database access error occurs
     */
    int updateOrderStatus(final OrderStatus orderStatus, final int id) throws SQLException;

    /**
     * Deletes an order
     *
     * @param id Order ID to delete
     * @return Number of rows affected
     * @throws SQLException if database access error occurs
     */
    int deleteOrder(int id) throws SQLException;

    /**
     * Gets list of available orders ready for pickup
     *
     * @return List of available orders
     * @throws SQLException if database access error occurs
     */
    Collection<OrderListResponse> getAvailableOrders() throws SQLException;

    /**
     * Gets list of orders allocated to delivery agents
     *
     * @return List of allocated orders
     * @throws SQLException if database access error occurs
     */
    Collection<OrderListResponse> getAllocatedOrders() throws SQLException;

    /**
     * Gets list of completed orders
     *
     * @return List of completed orders
     * @throws SQLException if database access error occurs
     */
    Collection<OrderListResponse> getCompletedOrders() throws SQLException;

    /**
     * Gets count of available orders
     *
     * @return Available order count
     * @throws SQLException if database access error occurs
     */
    int getAvailableOrdersCount() throws SQLException;

    /**
     * Gets count of allocated orders
     *
     * @return Allocated order count
     * @throws SQLException if database access error occurs
     */
    int getAllocatedOrdersCount() throws SQLException;

    /**
     * Gets count of completed orders
     *
     * @return Completed order count
     * @throws SQLException if database access error occurs
     */
    int getCompletedOrdersCount() throws SQLException;

    /**
     * Assigns an order to a delivery agent
     *
     * @param orderId         Order ID to assign
     * @param orderStatus     New order status
     * @param deliveryAgentId ID of delivery agent
     * @return Number of rows affected
     * @throws SQLException if database access error occurs
     */
    int acceptOrder(int orderId, OrderStatus orderStatus, int deliveryAgentId) throws SQLException;

}