package com.twozo.foodDeliverySystem.dao;

import com.twozo.foodDeliverySystem.model.DeliveryAgent;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;


/**
 * DeliveryAgentDao is an interface that extends the base Dao interface.
 * It defines SQL queries and methods for performing CRUD operations and
 * managing delivery-agent-related data in the `fds_delivery_agent` table.
 */
public interface DeliveryAgentDao {   

     /**
     * Retrieves the count of available delivery agents.
     *
     * @return The number of available delivery agents
     * @throws SQLException If there is an error accessing the database
     */
    int getAvailableAgentCount() throws SQLException;

    /**
     * Retrieves a list of all available delivery agents.
     *
     * @return A list of User objects representing available delivery agents
     * @throws SQLException If there is an error accessing the database
     */
    Collection<DeliveryAgent> getAvailableAgents() throws SQLException;

     /**
     * Marks a delivery agent as available.
     *
     * @param deliveryAgentId The ID of the delivery agent to be marked as available
     * @return An integer indicating the result of the operation
     * @throws SQLException If there is an error accessing the database
     */
    int changeStatus(final int deliveryAgentId, final boolean status) throws SQLException;

}
