package com.twozo.foodDeliverySystem.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.twozo.foodDeliverySystem.dao.DeliveryAgentDao;
import com.twozo.foodDeliverySystem.model.dto.RetrieveUserResponse;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.DeliveryAgent;
import org.springframework.stereotype.Service;

/**
 * Service class that manages delivery agent availability in the food delivery system.
 * Implements AvailabilityManagerService interface to handle agent status and counts.
 */
@Service
public class AgentAvailabilityManagerService implements AvailabilityManagerService {

    private final DeliveryAgentDao deliveryAgentDao;
    public static final String MESSAGE = "Failed, Unable to reach database";

    /**
     * Constructs an AgentAvailabilityManagerService with the required DAO
     *
     * @param deliveryAgentDao DAO for delivery agent operations
     */
    public AgentAvailabilityManagerService(final DeliveryAgentDao deliveryAgentDao) {
        this.deliveryAgentDao = deliveryAgentDao;
    }

    /**
     * Gets the count of currently available delivery agents
     *
     * @return The number of available delivery agents
     * @throws DataBaseAccessError If there is an error accessing the database
     */
    @Override
    public int getAvailableAgentsCount() throws DataBaseAccessError {
        try {
            return deliveryAgentDao.getAvailableAgentCount();
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Retrieves a list of all currently available delivery agents
     *
     * @return List of User objects representing available delivery agents
     * @throws DataBaseAccessError If there is an error accessing the database
     */
    @Override
    public Collection<RetrieveUserResponse> getAvailableAgents() throws DataBaseAccessError {
        try {
            Collection<DeliveryAgent> deliveryAgents = deliveryAgentDao.getAvailableAgents();
            Collection<RetrieveUserResponse> agents = new ArrayList<>();
            for (DeliveryAgent deliveryAgent : deliveryAgents) {
                final RetrieveUserResponse retrieveUserResponse = new RetrieveUserResponse();
                retrieveUserResponse.setId(deliveryAgent.getId());
                retrieveUserResponse.setName(deliveryAgent.getName());
                retrieveUserResponse.setLocation(deliveryAgent.getLocation());
                retrieveUserResponse.setContact(deliveryAgent.getContact());
                agents.add(retrieveUserResponse);
            }
            return agents;
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Updates the availability status of a delivery agent
     *
     * @param id ID of the delivery agent.
     * @param status the availability status of the delivery agent either true or false.
     * @return Number of rows affected by the update
     * @throws DataBaseAccessError If there is an error accessing the database
     */
    @Override
    public int changeStatus(final int id, final boolean status) throws DataBaseAccessError {
        try {
            return deliveryAgentDao.changeStatus(id, status);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

}