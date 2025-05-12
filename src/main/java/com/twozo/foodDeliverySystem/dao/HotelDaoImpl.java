package com.twozo.foodDeliverySystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.twozo.foodDeliverySystem.model.dto.HotelOrderResponse;
import com.twozo.foodDeliverySystem.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * Implementation of HotelDao interface that handles database operations for hotels
 */
@Repository
@Qualifier("hotelDao")
public class HotelDaoImpl implements UserDao, HotelDao {

    // SQL queries as constants
    private static final String GENERATE_USER_ID = "INSERT INTO fds_user (role) VALUES ('hotel') RETURNING id;";
    private static final String USER_HOTEL_MAPPING = "INSERT INTO fds_user_hotel (user_id, hotel_id) VALUES (?, ?);";
    private static final String ADD_HOTEL = "INSERT INTO fds_hotel (name, contact, location, password) VALUES (?,?,?,?) RETURNING id;";
    private static final String GET_HOTEL = "SELECT id, name, location, contact FROM fds_hotel WHERE contact=?;";
    private static final String GET_HOTEL_BY_ID = "SELECT name, location, contact FROM fds_hotel WHERE id=?;";
    private static final String UPDATE_PROFILE = "UPDATE fds_hotel SET name = ?, location=? WHERE contact = ?;";
    private static final String DELETE_USER = "DELETE FROM fds_hotel WHERE id=?;";
    private static final String UPDATE_PASSWORD = "UPDATE fds_hotel SET password = ? WHERE contact = ?;";
    private static final String GET_ALL_HOTELS = "SELECT id, name, location, contact FROM fds_hotel ORDER BY id;";
    private static final String GET_USER_COUNT = "SELECT COUNT (name) FROM fds_hotel;";

    private final DataSource dataSource;

    /**
     * Constructor initializes the connection pool if not already created
     */
    public HotelDaoImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a new hotel user to the database
     *
     * @param user The user object containing hotel details
     * @return The generated hotel ID
     */
    @Transactional
    public int addUser(final User user) throws SQLException {

        int hotelId = 0;
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            final int userId = generateUserId(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_HOTEL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getContact());
            preparedStatement.setString(3, user.getLocation());
            preparedStatement.setString(4, user.getPassword());
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                hotelId = rs.getInt(1);
            }
            preparedStatement.close();
            final int finalResult = mapHotelToUser(connection, userId, hotelId);
            if (finalResult == 0) {
                return 0;
            }
            return hotelId;
        } catch (SQLException e) {
            throw new SQLException("Error adding hotel to database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    private int generateUserId(final Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GENERATE_USER_ID);
        final ResultSet rs = preparedStatement.executeQuery();
        int userId = 0;
        if (rs.next()) {
            userId = rs.getInt(1);
        }
        preparedStatement.close();
        return userId;
    }

    private int mapHotelToUser(final Connection connection,
                               final int userId,final int hotelId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(USER_HOTEL_MAPPING);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, hotelId);
        final int affectedRow = preparedStatement.executeUpdate();
        preparedStatement.close();
        return affectedRow;
    }

    /**
     * Retrieves hotel details by contact number
     *
     * @param contactNumber The hotel's contact number
     * @return Hotel user object
     */
    @Override
    public User getUserByContact(final String contactNumber) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            Hotel hotel = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_HOTEL);
            preparedStatement.setString(1, contactNumber);
            final ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                hotel = new Hotel (id, name, contact, location, password);
            }
            preparedStatement.close();
            return hotel;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving hotel from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves hotel details by ID
     *
     * @param id The hotel ID
     * @return Hotel user object
     */
    @Override
    public User getUserById(final int id) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            Hotel hotel = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_HOTEL_BY_ID);
            preparedStatement.setInt(1, id);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                hotel = new Hotel (id, name, contact, location, password);
            }
            preparedStatement.close();
            return hotel;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving hotel from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Updates hotel profile information
     *
     * @param user The user object with updated details
     * @return Number of rows affected
     */
    @Override
    public int updateProfile(final User user) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROFILE);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLocation());
            preparedStatement.setString(3, user.getContact());
            final int affectedRow = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRow;
        } catch (SQLException e) {
            throw new SQLException("Error updating hotel profile: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Deletes a hotel user from database
     *
     * @param id The hotel ID to delete
     * @return Number of rows affected
     */
    @Override
    public int deleteUser(final int id) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER);
            preparedStatement.setInt(1, id);
            final int affectedRow = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRow;
        } catch (SQLException e) {
            throw new SQLException("Error deleting hotel from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Updates hotel user password
     *
     * @param contact     contact detail of the hotel
     * @param newPassword New password to set
     * @return Number of rows affected
     */
    @Override
    public int updatePassword(final String contact,final String newPassword) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, contact);
            final int affectedRow = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRow;
        } catch (SQLException e) {
            throw new SQLException("Error updating hotel password: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves list of all hotels
     *
     * @return List of hotel objects
     */
    @Override
    public Collection<Hotel> getAll() throws SQLException {

        Collection<Hotel> list = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_HOTELS);
            final ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                Hotel hotel = new Hotel (id, name, contact, location, password);
                list.add(hotel);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving hotels from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Gets total count of hotel users
     *
     * @return Total number of hotels
     */
    @Override
    public int getUserCount() throws SQLException {

        int userCount = 0;
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_COUNT);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userCount = rs.getInt(1);
            }
            preparedStatement.close();
            return userCount;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving user count from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Gets all orders for a specific hotel
     *
     * @param hotelId The hotel ID
     * @return List of orders with food items
     */
    @Override
    public Collection<HotelOrderResponse> getHotelOrders(final int hotelId) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_HOTEL_ORDERS);
            preparedStatement.setInt(1, hotelId);
            ResultSet rs = preparedStatement.executeQuery();
            int oId = 0;
            int cId = 0;
            Collection<HotelOrderResponse> orderList = new ArrayList<>();
            Collection<FoodItem> foodItemList = new ArrayList<>();
            while (rs.next()) {
                final int orderId = rs.getInt("order_id");
                final int customerId = rs.getInt("customer_id");
                final String foodItemName = rs.getString("name");
                final double foodItemPrice = rs.getDouble("food_item_price");
                final FoodItem foodItem = new FoodItem(foodItemName, foodItemPrice);
                foodItemList.add(foodItem);
                if (oId ==0 && cId == 0) {
                    oId = orderId;
                    cId = customerId;
                }
                if (oId != orderId) {
                    oId = orderId;
                    cId = customerId;
                    final HotelOrderResponse order = new HotelOrderResponse(orderId, customerId, foodItemList);
                    orderList.add(order);
                    foodItemList = new ArrayList<>();
                }

            }
            preparedStatement.close();
            return orderList;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving hotel orders from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Updates order status to ready for pickup
     *
     * @param orderId The order ID to update
     * @return Number of rows affected
     */
    @Override
    public int updateOrderStatusReadyForPickup(final int orderId) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_STATUS_READY_FOR_PICKUP);
            preparedStatement.setInt(1, orderId);
            final int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRows;
        } catch (SQLException e) {
            throw new SQLException("Error updating order status ready for pickup: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

}