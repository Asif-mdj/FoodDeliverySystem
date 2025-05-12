package com.twozo.foodDeliverySystem.dao;

import java.sql.SQLException;
import java.util.Collection;

import com.twozo.foodDeliverySystem.model.User;

/**
 * Interface for user data access operations
 */
public interface UserDao {

    /**
     * Adds a new user to the system
     *
     * @param user the user to add
     * @return number of rows affected
     * @throws SQLException if database access error occurs
     */
    int addUser(final User user) throws SQLException;

    /**
     * Gets a user by their contact number
     *
     * @param contactNumber the contact number to search for
     * @return the matching user or null if not found
     * @throws SQLException if database access error occurs
     */
    User getUserByContact(final String contactNumber) throws SQLException;

    /**
     * Gets a user by their ID
     *
     * @param Id the user ID to search for
     * @return the matching user or null if not found
     * @throws SQLException if database access error occurs
     */
    User getUserById(final int Id) throws SQLException;

    /**
     * Updates an existing user's profile information
     *
     * @param user the user with updated information
     * @return number of rows affected
     * @throws SQLException if database access error occurs
     */
    int updateProfile(final User user) throws SQLException;

    /**
     * Deletes a user from the system
     *
     * @param id the ID of user to delete
     * @return number of rows affected
     * @throws SQLException if database access error occurs
     */
    int deleteUser(final int id) throws SQLException;

    /**
     * Updates a user's password
     *
     * @param contact contact of the user
     * @param newPassword the new password to set
     * @return number of rows affected
     * @throws SQLException if database access error occurs
     */
    int updatePassword(final String contact, final String newPassword) throws SQLException;

    /**
     * Gets all users from the system
     *
     * @return list of all users
     * @throws SQLException if database access error occurs
     */
    <T> Collection<T> getAll() throws SQLException;

    /**
     * Gets total count of users in the system
     *
     * @return total number of users
     * @throws SQLException if database access error occurs
     */
    int getUserCount() throws SQLException;

}