package com.twozo.foodDeliverySystem.service;

import java.util.Collection;

import com.twozo.foodDeliverySystem.model.dto.RetrieveUserResponse;
import com.twozo.foodDeliverySystem.model.dto.UserRegistrationDto;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;

/**
 * Service interface for managing user operations in the food delivery system
 */
public interface UserServices {

    /**
     * Registers a new user of a specified type in the system
     *
     * @param userType Type of user (customer, hotel, delivery agent, etc.)
     * @param user     User object containing registration details
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    int registerUser(final String userType, final UserRegistrationDto user) throws DataBaseAccessError;

    /**
     * Retrieves user by their contact information
     *
     * @param userType Type of user to search for
     * @param contact contact information of the user
     * @return User object if found
     * @throws DataBaseAccessError if database access fails
     */
    RetrieveUserResponse getUserByContact(final String userType, final String contact) throws DataBaseAccessError;

    /**
     * Retrieves user by their ID
     *
     * @param userType Type of user to search for
     * @param id ID of the user to search for.
     * @return User object if found
     * @throws DataBaseAccessError if database access fails
     */
    RetrieveUserResponse getUserById(final String userType, final int id) throws DataBaseAccessError;

    /**
     * Gets a list of all users of a specified type
     *
     * @param userType Type of users to retrieve
     * @return List of all users of a specified type
     * @throws DataBaseAccessError if database access fails
     */
    Collection<RetrieveUserResponse> getAllUser(final String userType) throws DataBaseAccessError;

    /**
     * Gets count of users of a specified type
     *
     * @param userType Type of users to count
     * @return Count of users
     * @throws DataBaseAccessError if database access fails
     */
    int getUserCount(final String userType) throws DataBaseAccessError;

    /**
     * Updates password for a specified user
     *
     * @param userType    Type of user
     * @param user with a new password and user id to update.
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    int updatePassword(final String userType, final UserRegistrationDto user) throws DataBaseAccessError;

    /**
     * Updates profile information for a specified user
     *
     * @param userType Type of user
     * @param user     User object with updated information
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    int updateProfile(final String userType, final UserRegistrationDto user) throws DataBaseAccessError;

    /**
     * Removes specified user from a system
     *
     * @param userType Type of user to remove
     * @param id ID of the user to remove
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    int remove(final String userType, final int id) throws DataBaseAccessError;

}
