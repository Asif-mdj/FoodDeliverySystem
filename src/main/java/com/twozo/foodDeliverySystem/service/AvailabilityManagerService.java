package com.twozo.foodDeliverySystem.service;

import java.util.Collection;

import com.twozo.foodDeliverySystem.model.dto.RetrieveUserResponse;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;

/**
 * Interface for managing delivery agent availability
 */
public interface AvailabilityManagerService {

    /**
     * Gets the count of available delivery agents
     *
     * @return Number of available agents
     * @throws DataBaseAccessError if database access fails
     */
    int getAvailableAgentsCount() throws DataBaseAccessError;

    /**
     * Gets a list of all available delivery agents
     *
     * @return List of available delivery agents
     * @throws DataBaseAccessError if database access fails
     */
    Collection<RetrieveUserResponse> getAvailableAgents() throws DataBaseAccessError;

    /**
     * Changes the availability status of a delivery agent
     *
     * @param id ID of the delivery agent.
     * @param status the availability status of the delivery agent either true or false.
     * @return Number of records updated
     * @throws DataBaseAccessError if database access fails
     */
    int changeStatus(final int id, final boolean status) throws DataBaseAccessError;

}