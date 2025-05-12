package com.twozo.foodDeliverySystem.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.twozo.foodDeliverySystem.dao.*;
import com.twozo.foodDeliverySystem.model.dto.OrderListResponse;
import com.twozo.foodDeliverySystem.model.dto.OrderPlacementRequest;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.*;
import org.springframework.stereotype.Service;

/**
 * Service class that manages order-related operations
 */
@Service
public class OrderManagementService implements OrderService {

    private final OrderDao orderDao;
    private final MenuDao menuDao;
    private final UserDao customerDaoImpl;

    /**
     * Constructor to initialize service with required DAOs
     *
     * @param orderDao DAO for order operations
     * @param menuDao  DAO for menu operations
     */
    public OrderManagementService(final OrderDao orderDao, final MenuDao menuDao,
                                  final UserDao customerDaoImpl) {
        this.orderDao = orderDao;
        this.menuDao = menuDao;
        this.customerDaoImpl = customerDaoImpl;
    }

    /**
     * Places a new order in the system
     *
     * @param orderPlacementRequest to be placed. Contains customer ID, hotel ID, ordered food items and order status.
     * @return ID of created order
     * @throws DataBaseAccessError if database access fails
     */
    public int placeOrder(final OrderPlacementRequest orderPlacementRequest) throws DataBaseAccessError {
        try {
            final String customerContact = orderPlacementRequest.getCustomerContact();
            final int customerId = customerDaoImpl.getUserByContact(customerContact).getId();
            final OrderStatus orderStatus = OrderStatus.UNDER_PREPARATION;
            final LocalDateTime orderDate = orderPlacementRequest.getDateTime();
            final String deliveryLocation = orderPlacementRequest.getDeliveryLocation();
            final List<FoodItem> orderFoodItems = orderPlacementRequest.getOrderedFoodItems();
            final boolean isAssigned = false;
            Order order = new Order(0, orderStatus,orderDate,deliveryLocation, isAssigned);
            final int hotelId = orderPlacementRequest.getHotelId();
            final List<Integer> menuIds = menuDao.getMenuIds(hotelId, orderFoodItems);
            final int orderId = orderDao.addOrder(customerId,order,menuIds);
            if (orderId == 0) {
                return 0;
            }
            return orderId;
        } catch (SQLException e) {
            throw new DataBaseAccessError("Failed Access!!");
        }

    }

    /**
     * Gets a list of orders available for pickup
     *
     * @return List of available orders
     * @throws DataBaseAccessError if database access fails
     */
    public Collection<OrderListResponse> getAvailableOrders() throws DataBaseAccessError {
        try {
            return orderDao.getAvailableOrders();
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }
    }

    /**
     * Gets a list of orders allocated to delivery agents
     *
     * @return List of allocated orders
     * @throws DataBaseAccessError if database access fails
     */
    public Collection<OrderListResponse> getAllocatedOrders() throws DataBaseAccessError {
        try {
            return orderDao.getAllocatedOrders();
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }
    }

    /**
     * Gets a list of completed orders
     *
     * @return List of completed orders
     * @throws DataBaseAccessError if database access fails
     */
    public Collection<OrderListResponse> getCompletedOrders() throws DataBaseAccessError {
        try {
            return orderDao.getCompletedOrders();
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }
    }

    /**
     * Gets count of orders available for pickup
     *
     * @return Number of available orders
     * @throws DataBaseAccessError if database access fails
     */
    public int getAvailableOrdersCount() throws DataBaseAccessError {
        try {
            return orderDao.getAvailableOrdersCount();
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }
    }

    /**
     * Gets count of orders allocated to delivery agents
     *
     * @return Number of allocated orders
     * @throws DataBaseAccessError if database access fails
     */
    public int getAllocatedOrdersCount() throws DataBaseAccessError {
        try {
            return orderDao.getAllocatedOrdersCount();
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }
    }

    /**
     * Gets count of completed orders
     *
     * @return Number of completed orders
     * @throws DataBaseAccessError if database access fails
     */
    public int getCompletedOrderCount() throws DataBaseAccessError {
        try {
            return orderDao.getCompletedOrdersCount();
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }
    }

    @Override
    public Collection<FoodItem> getOrderedFoodItem(final int orderId) throws DataBaseAccessError {
        try {
            return orderDao.getOrderedFoodItem(orderId);
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }
    }

    /**
     * Assigns order to a delivery agent
     *
     * @param orderId ID of the order
     * @param deliveryAgentId ID of the delivery agent
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    public int acceptOrder(final int orderId, final int deliveryAgentId)
            throws DataBaseAccessError {
        try {
            final OrderStatus orderStatus = OrderStatus.OUT_FOR_DELIVERY;
            return orderDao.acceptOrder(orderId, orderStatus, deliveryAgentId);
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }

    }

    /**
     * Marks an order as completed by the delivery agent
     *
     * @param orderId ID of the order
     * @param orderStatus order status either Ready for pickup or Delivered
     * @throws DataBaseAccessError if database access fails
     */
    public int updateOrderStatus(final int orderId, final OrderStatus orderStatus)
            throws DataBaseAccessError {
        try {
            return orderDao.updateOrderStatus(orderStatus, orderId);
        } catch (SQLException s) {
            throw new DataBaseAccessError("Failed Access!!");
        }
    }

}