package com.twozo.foodDeliverySystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.twozo.foodDeliverySystem.model.DeliveryAgent;
import com.twozo.foodDeliverySystem.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * DeliveryAgentDaoImpl is an implementation of the DeliveryAgentDao and UserDao interfaces.
 * It provides methods to perform CRUD operations and manage delivery-agent-related data
 * in the `fds_delivery_agent` table.
 */
@Repository
@Qualifier("deliveryAgentDao")
public class DeliveryAgentDaoImpl implements UserDao, DeliveryAgentDao {

    private final DataSource dataSource;

    private static final String GENERATE_USER_ID = "INSERT INTO fds_user (role) VALUES ('delivery_agent') RETURNING id;";
    private static final String USER_AGENT_MAPPING = "INSERT INTO fds_user_delivery_agent (user_id, agent_id) VALUES (?, ?);";
    private static final String ADD_AGENT = "INSERT INTO fds_delivery_agent ( name, contact, password,  location) VALUES (?,?,?,?) RETURNING id;";
    private static final String GET_AGENT = "SELECT id, name, location, contact FROM fds_delivery_agent WHERE contact = ?;";
    private static final String GET_AGENT_BY_ID = "SELECT id, name, location, contact FROM fds_delivery_agent WHERE id = ?;";
    private static final String UPDATE_PROFILE = "UPDATE fds_delivery_agent SET name = ?, location =? WHERE contact = ?;";
    private static final String DELETE_USER = "DELETE FROM fds_delivery_agent WHERE id = ?;";
    private static final String UPDATE_PASSWORD = "UPDATE fds_delivery_agent SET password = ? WHERE contact = ?;";
    private static final String GET_ALL_AGENTS = "SELECT id, name, location, contact, availability FROM fds_delivery_agent ORDER BY id";
    private static final String GET_USER_COUNT = "SELECT COUNT(name) FROM fds_delivery_agent;";
    private static final String GET_AVAILABLE_AGENTS_COUNT = "SELECT COUNT(name) FROM fds_delivery_agent WHERE availability = true;";
    private static final String GET_AVAILABLE_AGENTS = "SELECT id, name, location, contact, availability FROM fds_delivery_agent WHERE availability = true;";
    private static final String CHANGE_STATUS = "UPDATE fds_delivery_agent SET availability = ? WHERE id = ?;";

    public DeliveryAgentDaoImpl (final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a new delivery agent to the database.
     *
     * @param user The User object containing delivery agent details
     * @return The number of rows affected by the operation
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    @Transactional
    public int addUser(final User user) throws SQLException {

        int deliveryAgentId = 0;
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            final int userId = generateUserId(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_AGENT);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getContact());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getLocation());
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                deliveryAgentId = resultSet.getInt(1);
            }
            preparedStatement.close();
            final int finalResult = mapDeliveryAgentToUser(connection, userId, deliveryAgentId);
            if (finalResult == 0) {
                return 0;
            }
            return deliveryAgentId;
        } catch (SQLException e) {
            throw new SQLException("Error adding delivery agent to database: " + e.getMessage());
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

    private int mapDeliveryAgentToUser(final Connection connection,
                                       final int userId, final  int deliveryAgentId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(USER_AGENT_MAPPING);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, deliveryAgentId);
        final int affectedRow = preparedStatement.executeUpdate();
        preparedStatement.close();
        return affectedRow;
    }

    /**
     * Retrieves a delivery agent by their contact information.
     *
     * @param contactNumber The contact number of the delivery agent
     * @return A DeliveryAgent object representing the delivery agent, or null if not found
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public User getUserByContact(final String contactNumber) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            DeliveryAgent deliveryAgent = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_AGENT);
            preparedStatement.setString(1, contactNumber);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                deliveryAgent = new DeliveryAgent(id, name, contact, location, password);
            }
            preparedStatement.close();
            return deliveryAgent;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving delivery agent from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }


    }

    /**
     * Retrieves a delivery agent by their ID.
     *
     * @param id The ID of the delivery agent
     * @return A DeliveryAgent object representing the delivery agent, or null if not found
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public User getUserById(final int id) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            DeliveryAgent deliveryAgent = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_AGENT_BY_ID);
            preparedStatement.setInt(1, id);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                deliveryAgent = new DeliveryAgent(id, name, contact, location, password);
            }
            preparedStatement.close();
            return deliveryAgent;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving delivery agent from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }
    
    /**
     * Updates the profile of a delivery agent.
     *
     * @param user The User object containing updated delivery agent details
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
            throw new SQLException("Error updating delivery agent profile: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Deletes a delivery agent by their ID.
     *
     * @param id The ID of the delivery agent to be deleted
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
            throw new SQLException("Error deleting delivery agent from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Updates the password of a delivery agent.
     *
     * @param contact     The contact of the delivery agent
     * @param newPassword The new password to be set
     * @return The number of rows affected by the operation
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public int updatePassword(final String contact, final String newPassword) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, contact);
            final int affectedRow = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRow;
        } catch (SQLException e) {
            throw new SQLException("Error updating delivery agent password: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves a list of all delivery agents.
     *
     * @return A list of DeliveryAgent objects representing all delivery agents
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public Collection<DeliveryAgent> getAll() throws SQLException {
        Collection<DeliveryAgent> list = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            DeliveryAgent deliveryAgent = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_AGENTS);
            final ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                deliveryAgent = new DeliveryAgent(id, name, contact, location, password);
                list.add(deliveryAgent);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving delivery agents from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves the total count of delivery agents.
     *
     * @return The total number of delivery agents
     * @throws SQLException If there is an error accessing the database
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
     * Retrieves the count of available delivery agents.
     *
     * @return The number of available delivery agents
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public int getAvailableAgentCount() throws SQLException {

        int availableUserCount = 0;
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_AVAILABLE_AGENTS_COUNT);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                availableUserCount = rs.getInt(1);
            }
            preparedStatement.close();
            return availableUserCount;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving available user count from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves a list of all available delivery agents.
     *
     * @return A list of User objects representing available delivery agents
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public Collection<DeliveryAgent> getAvailableAgents() throws SQLException {

        Collection<DeliveryAgent> availableList = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            DeliveryAgent deliveryAgent = null;
            PreparedStatement preparedStatement = connection.prepareStatement(GET_AVAILABLE_AGENTS);
            final ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                final String location = rs.getString("location");
                final String contact = rs.getString("contact");
                final String password = "********";
                deliveryAgent = new DeliveryAgent(id, name, contact, location, password);
                availableList.add(deliveryAgent);
            }
            preparedStatement.close();
            return availableList;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving available delivery agents from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Marks a delivery agent as available.
     *
     * @param deliveryAgentId The ID of the delivery agent to be marked as available
     * @return The number of rows affected by the operation
     * @throws SQLException If there is an error accessing the database
     */
    @Override
    public int changeStatus(int deliveryAgentId, boolean status) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_STATUS);
            preparedStatement.setBoolean(1, status);
            preparedStatement.setInt(2, deliveryAgentId);
            final int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRows;
        } catch (SQLException e) {
            throw new SQLException("Error changing delivery agent availability: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        
    }

}
