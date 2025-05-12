package com.twozo.foodDeliverySystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.twozo.foodDeliverySystem.model.dto.OrderListResponse;
import com.twozo.foodDeliverySystem.model.FoodItem;
import com.twozo.foodDeliverySystem.model.Order;
import com.twozo.foodDeliverySystem.model.OrderStatus;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * Implementation of OrderDao interface that handles database operations for orders
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    private final DataSource dataSource;

    /**
     * Constructor that initializes the connection pool if not already created
     */
    public OrderDaoImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Generates a new order ID for the given customer
     *
     * @param customerId Customer ID to generate order for
     * @return Generated order ID
     * @throws SQLException if database error occurs
     */


    @Transactional
    public int addOrder(final int customerId, Order order, final List<Integer> menuIds) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            final int orderId = generateOrderId(customerId, connection);
            order.setId(orderId);
            final int affectedRows = addOrderDetails(order, connection);
            if (affectedRows > 0) {
                final int finalResult = addOrderFoodItem(order, menuIds, connection);
                if (finalResult > 0) {
                    return orderId;
                }
            } else {
                throw new SQLException("Error placing order");
            }
            return 0;
        } catch (SQLException e) {
            throw new SQLException("Error placing order", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    private int generateOrderId(final int customerId, final Connection connection) throws SQLException {
        int orderId = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(GENERATE_ORDER_ID);
        preparedStatement.setInt(1, customerId);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            orderId = resultSet.getInt("id");
        }
        preparedStatement.close();
        return orderId;
    }

    /**
     * Adds order details to database
     *
     * @param order Order object containing details to add
     * @return Number of affected rows
     * @throws SQLException if database error occurs
     */
    private int addOrderDetails(final Order order, final Connection connection) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_ORDER_DETAILS);
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setString(2, order.getDateTime().toString());
            preparedStatement.setString(3, order.getStatus().name());
            preparedStatement.setBoolean(4, order.isAssigned());
            preparedStatement.setString(5, order.getDeliveryLocation());
            int affectedRow = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRow;
    }

    /**
     * Adds food items to an order
     *
     * @param order   Order to add items to
     * @param menuIds List of menu item IDs to add
     * @return Number of affected rows
     * @throws SQLException if database error occurs
     */
    private int addOrderFoodItem(final Order order, final List<Integer> menuIds,
                                 final Connection connection) throws SQLException {
        int affectedRows = 0;
        final int iteration = menuIds.size();
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_ORDER_FOOD_ITEM);
        for (int i = 0; i < iteration; i++) {
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setInt(2, menuIds.get(i));
            affectedRows = preparedStatement.executeUpdate();
        }
        preparedStatement.close();
        return affectedRows;
    }

    /**
     * Gets list of food items in an order
     *
     * @param orderId Order ID to get items for
     * @return List of food items in the order
     * @throws SQLException if database error occurs
     */
    @Override
    public Collection<FoodItem> getOrderedFoodItem(final int orderId) throws SQLException {
        Collection<FoodItem> orderedFoodItems = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDERED_FOOD_ITEMS);
            preparedStatement.setInt(1, orderId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int foodItemId = resultSet.getInt("food_item_id");
                final String foodName = resultSet.getString("name");
                final double foodPrice = resultSet.getDouble("food_item_price");
                final FoodItem foodItem = new FoodItem(foodItemId, foodName, foodPrice);
                orderedFoodItems.add(foodItem);
            }
            preparedStatement.close();
            return orderedFoodItems;
        } catch (SQLException e) {
            throw new SQLException("Error getting ordered food items", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Updates order status
     *
     * @param orderStatus New status to set
     * @param id          Order ID to update
     * @return Number of affected rows
     * @throws SQLException if database error occurs
     */
    @Override
    public int updateOrderStatus(final OrderStatus orderStatus, final int id) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS);
            preparedStatement.setString(1, orderStatus.name());
            preparedStatement.setInt(2, id);
            final int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRows;
        } catch (SQLException e) {
            throw new SQLException("Error updating order status", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Deletes an order
     *
     * @param id Order ID to delete
     * @return Number of affected rows
     * @throws SQLException if database error occurs
     */
    @Override
    public int deleteOrder(final int id) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ORDER);
            preparedStatement.setInt(1, id);
            final int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRows;
        } catch (SQLException e) {
            throw new SQLException("Error deleting order", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Gets list of available orders ready for pickup
     *
     * @return List of available orders
     * @throws SQLException if database error occurs
     */
    @Override
    public Collection<OrderListResponse> getAvailableOrders() throws SQLException {

        Collection<OrderListResponse> orders = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_AVAILABLE_ORDERS);
            final ResultSet resultSet = preparedStatement.executeQuery();
            int oId = 0;
            while (resultSet.next()) {
                final int orderId = resultSet.getInt("id");
                final int customerId = resultSet.getInt("customer_id");
                final String customerContact = resultSet.getString("customer_contact");
                final String orderDate = resultSet.getString("order_date");
                final LocalDateTime localDateTime = LocalDateTime.parse(orderDate);
                final int hotelId = resultSet.getInt("hotel_id");
                final String deliveryLocation = resultSet.getString("delivery_location");
                final int deliveryAgentId = resultSet.getInt("delivery_agent_id");
                if (oId != orderId) {
                    OrderListResponse order = new OrderListResponse(orderId, customerId, customerContact, hotelId, localDateTime, deliveryLocation, deliveryAgentId);
                    orders.add(order);
                    oId = orderId;
                }
            }
            preparedStatement.close();
            return orders;
        } catch (SQLException e) {
            throw new SQLException("Error getting available orders", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Gets list of orders allocated to delivery agents
     *
     * @return List of allocated orders
     * @throws SQLException if database error occurs
     */
    @Override
    public Collection<OrderListResponse> getAllocatedOrders() throws SQLException {
        Collection<OrderListResponse> orders = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALLOCATED_ORDERS);
            final ResultSet resultSet = preparedStatement.executeQuery();
            int oId = 0;
            while (resultSet.next()) {
                final int orderId = resultSet.getInt("id");
                final String orderDate = resultSet.getString("order_date");
                final LocalDateTime localDateTime = LocalDateTime.parse(orderDate);
                final int customerId = resultSet.getInt("customer_id");
                final String customerContact = resultSet.getString("customer_contact");
                final int hotelId = resultSet.getInt("hotel_id");
                final String deliveryLocation = resultSet.getString("delivery_location");
                final int deliveryAgentId = resultSet.getInt("delivery_agent_id");
                if (oId != orderId) {
                    final OrderListResponse order = new OrderListResponse(orderId, customerId, customerContact, hotelId, localDateTime, deliveryLocation, deliveryAgentId);
                    orders.add(order);
                    oId = orderId;
                }
            }
            preparedStatement.close();
            return orders;
        } catch (SQLException e) {
            throw new SQLException("Error getting allocated orders", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Gets list of completed orders
     *
     * @return List of completed orders
     * @throws SQLException if database error occurs
     */
    @Override
    public Collection<OrderListResponse> getCompletedOrders() throws SQLException {
        Collection<OrderListResponse> orders = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPLETED_ORDERS);
            final ResultSet resultSet = preparedStatement.executeQuery();
            int oId = 0;
            while (resultSet.next()) {
                final int orderId = resultSet.getInt("id");
                final String orderDate = resultSet.getString("order_date");
                final LocalDateTime localDateTime = LocalDateTime.parse(orderDate);
                final int customerId = resultSet.getInt("customer_id");
                final String customerContact = resultSet.getString("customer_contact");
                final int hotelId = resultSet.getInt("hotel_id");
                final String deliveryLocation = resultSet.getString("delivery_location");
                final int deliveryAgentId = resultSet.getInt("delivery_agent_id");
                if (oId != orderId) {
                    final OrderListResponse order = new OrderListResponse(orderId, customerId, customerContact, hotelId, localDateTime, deliveryLocation, deliveryAgentId);
                    orders.add(order);
                    oId = orderId;
                }
            }
            preparedStatement.close();
            return orders;
        } catch (SQLException e) {
            throw new SQLException("Error getting completed orders", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Gets count of available orders ready for pickup
     *
     * @return Available order count
     * @throws SQLException if database error occurs
     */
    @Override
    public int getAvailableOrdersCount() throws SQLException {
        int ordersCount = 0;
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_AVAILABLE_ORDER_COUNT);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ordersCount = resultSet.getInt(1);
            }
            preparedStatement.close();
            return ordersCount;
        } catch (SQLException e) {
            throw new SQLException("Error getting available order count", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Gets count of allocated orders
     *
     * @return Allocated order count
     * @throws SQLException if database error occurs
     */
    @Override
    public int getAllocatedOrdersCount() throws SQLException {
        int ordersCount = 0;
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALLOCATED_ORDER_COUNT);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ordersCount = resultSet.getInt(1);
            }
            preparedStatement.close();
            return ordersCount;
        } catch (SQLException e) {
            throw new SQLException("Error getting allocated order count", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Gets count of completed orders
     *
     * @return Completed order count
     * @throws SQLException if database error occurs
     */
    @Override
    public int getCompletedOrdersCount() throws SQLException {

        int ordersCount = 0;
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPLETED_ORDER_COUNT);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ordersCount = resultSet.getInt(1);
            }
            preparedStatement.close();
            return ordersCount;
        } catch (SQLException e) {
            throw new SQLException("Error getting completed order count", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Assigns an order to a delivery agent
     *
     * @param orderId         Order ID to assign
     * @param orderStatus     New status to set
     * @param deliveryAgentId ID of delivery agent to assign to
     * @return Number of affected rows
     * @throws SQLException if database error occurs
     */
    @Override
    public int acceptOrder(final int orderId, final OrderStatus orderStatus,
                           final int deliveryAgentId) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_ORDER_TO_AGENT);
            preparedStatement.setString(1, orderStatus.name());
            preparedStatement.setInt(2, deliveryAgentId);
            preparedStatement.setInt(3, orderId);
            final int affectedRow = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRow;
        } catch (SQLException e) {
            throw new SQLException("Error assigning order to agent", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

}