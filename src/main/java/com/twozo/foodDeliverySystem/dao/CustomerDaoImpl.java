package com.twozo.foodDeliverySystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.twozo.foodDeliverySystem.model.Customer;
import com.twozo.foodDeliverySystem.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * CustomerDaoImpl is an implementation of the UserDao interface.
 * It provides methods to perform CRUD operations on the `fds_customer` table
 * in the database, such as adding, retrieving, updating, and deleting customer records.
 */
@Repository
@Qualifier("customerDao")
public class CustomerDaoImpl implements UserDao {

    private final DataSource dataSource;

    // SQL queries for customer-related operations
    private static final String GENERATE_USER_ID = "INSERT INTO fds_user (role) VALUES ('customer') RETURNING id;";
    private static final String USER_CUSTOMER_MAPPING = "INSERT INTO fds_user_customer (user_id, customer_id) VALUES (?, ?);";
    private static final String ADD_CUSTOMER = "INSERT INTO fds_customer ( name, contact, location, password) VALUES (?,?,?,?) RETURNING id;";
    private static final String GET_CUSTOMER = "SELECT id, name, location, contact FROM fds_customer WHERE contact = ?;";
    private static final String GET_CUSTOMER_BY_ID = "SELECT id, name, location, contact FROM fds_customer WHERE id = ?;";
    private static final String UPDATE_PROFILE = "UPDATE fds_customer SET name = ?, location = ? WHERE contact = ?;";
    private static final String DELETE_USER = "DELETE FROM fds_customer WHERE id = ?;";
    private static final String UPDATE_PASSWORD = "UPDATE fds_customer SET password = ? WHERE contact = ?;";
    private static final String GET_ALL_CUSTOMERS = "SELECT id, name, location, contact FROM fds_customer ORDER BY id;";
    private static final String GET_USER_COUNT = "SELECT COUNT(name) FROM fds_customer;";

    public CustomerDaoImpl (final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a new customer to the database.
     *
     * @param user The User object containing customer details
     * @return The number of rows affected by the operation
     * @throws SQLException If there is an error accessing the database
     */
    @Transactional
    public int addUser(final User user) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try{
            final int userId = generateUserId(connection);
            int customerId = 0;
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_CUSTOMER);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getContact());
            preparedStatement.setString(3, user.getLocation());
            preparedStatement.setString(4, user.getPassword());
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                customerId = rs.getInt("id");
            }
            preparedStatement.close();
            final int finalResult = mapCustomerToUser(connection, userId, customerId);
            if (finalResult == 0) {
                return 0;
            }
            return customerId;
        } catch (SQLException e) {
            throw new SQLException("Error adding customer to database: " + e.getMessage());
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

    private int mapCustomerToUser(final Connection connection,
                                  final  int userId, final  int customerId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(USER_CUSTOMER_MAPPING);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, customerId);
        int affectedRow = preparedStatement.executeUpdate();
        preparedStatement.close();
        return affectedRow;
    }


    /**
     * Retrieves a customer by their contact information.
     *
     * @param contactNumber The contact number of the customer
     * @return A Customer object representing the customer, or null if not found
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public User getUserByContact(final String contactNumber) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try{
            Customer customer = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER);
            preparedStatement.setString(1, contactNumber);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String password = "********";
                customer = new Customer(id, name, contact, location, password);
            }
            preparedStatement.close();
            return customer;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving customer from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }


    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer
     * @return A Customer object representing the customer, or null if not found
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public User getUserById(final int id) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            Customer customer = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER_BY_ID);
            preparedStatement.setInt(1, id);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                customer = new Customer(id, name, contact, location, password);
            }
            preparedStatement.close();
            return customer;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving customer from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Updates the profile of a customer.
     *
     * @param user The User object containing updated customer details
     * @return The number of rows affected by the operation
     * @throws SQLException If there is an error accessing the database
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
            throw new SQLException("Error updating customer profile: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id The ID of the customer to be deleted
     * @return The number of rows affected by the operation
     * @throws SQLException If there is an error accessing the database
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
            throw new SQLException("Error deleting customer from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Updates the password of a customer.
     *
     * @param contact     The contact detail of the customer
     * @param newPassword The new password to be set
     * @return The number of rows affected by the operation
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public int updatePassword(final String contact, final  String newPassword) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, contact);
            final int affectedRow = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRow;
        } catch (SQLException e) {
            throw new SQLException("Error updating customer password: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves a list of all customers.
     *
     * @return A list of Customer objects representing all customers
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public Collection<Customer> getAll() throws SQLException {
        final Collection<Customer> list = new ArrayList<>();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            Customer customer = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CUSTOMERS);
            final ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                customer = new Customer(id, name, contact, location, password);
                list.add(customer);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving customers from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves the total number of customers.
     *
     * @return The total number of customers
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public int getUserCount() throws SQLException {

        int userCount = 0;
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_COUNT);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userCount = rs.getInt(1);
            }
            preparedStatement.close();
            return userCount;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving customer count from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

}
