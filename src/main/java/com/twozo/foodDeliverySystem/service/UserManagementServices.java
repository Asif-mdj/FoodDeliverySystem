package com.twozo.foodDeliverySystem.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.twozo.foodDeliverySystem.dao.UserDao;
import com.twozo.foodDeliverySystem.dao.UserDaoFactory;
import com.twozo.foodDeliverySystem.model.dto.RetrieveUserResponse;
import com.twozo.foodDeliverySystem.model.dto.UserRegistrationDto;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.User;
import org.springframework.stereotype.Service;

/**
 * Service class that implements UserServices interface to manage user-related operations
 */
@Service
public class UserManagementServices implements UserServices {

    public static final String MESSAGE = "Failed, Unable to reach database";

    private final UserDaoFactory userDaoFactory;

    /**
     * Constructor to initialize UserManagementServices with UserDaoFactory
     *
     * @param userDaoFactory Factory for creating UserDao instances
     */
    public UserManagementServices(final UserDaoFactory userDaoFactory) {
        this.userDaoFactory = userDaoFactory;
    }

    /**
     * Registers a new user in the system
     *
     * @param userType Type of user to register
     * @param user     User object containing user details
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access fails
     */
    public int registerUser(final String userType,
                            final UserRegistrationDto user) throws DataBaseAccessError {
        try {
            final UserDao userDao = findUserType(userType);
            User newUser = new User();
            newUser.setName(user.getName());
            newUser.setContact(user.getContact());
            newUser.setLocation(user.getLocation());
            newUser.setPassword(user.getPassword());
            return userDao.addUser(newUser);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Gets user details by contact number
     *
     * @param userType Type of user to search
     * @param contact contact detail to fetch
     * @return Matching User object or null if not found
     * @throws DataBaseAccessError if database access fails
     */
    public RetrieveUserResponse getUserByContact(final String userType,
                                                 final String contact) throws DataBaseAccessError {
        try {
            final UserDao userDao = findUserType(userType);
            final User user = userDao.getUserByContact(contact);
            RetrieveUserResponse existingUser = new RetrieveUserResponse();
            existingUser.setId(user.getId());
            existingUser.setName(user.getName());
            existingUser.setContact(user.getContact());
            existingUser.setLocation(user.getLocation());
            return existingUser;
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Gets user details by ID
     *
     * @param userType Type of user to search
     * @param id ID of user to search for
     * @return Matching User object or null if not found
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public RetrieveUserResponse getUserById(final String userType,
                                            final int id ) throws DataBaseAccessError {
        try {
            final UserDao userDao = findUserType(userType);
            final User user = userDao.getUserById(id);
            RetrieveUserResponse existingUser = new RetrieveUserResponse();
            existingUser.setId(user.getId());
            existingUser.setName(user.getName());
            existingUser.setContact(user.getContact());
            existingUser.setLocation(user.getLocation());
            return existingUser;

        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Gets a list of all users of a specified type
     *
     * @param userType Type of users to retrieve
     * @return List of all users of a specified type
     * @throws DataBaseAccessError if database access fails
     */
    public Collection<RetrieveUserResponse> getAllUser(final String userType) throws DataBaseAccessError {
        try {
            final UserDao userDao = findUserType(userType);
            Collection<User> users = userDao.getAll();
            Collection<RetrieveUserResponse> existingUsers = new ArrayList<>();
            for (User user : users) {
                RetrieveUserResponse existingUser = new RetrieveUserResponse();
                existingUser.setId(user.getId());
                existingUser.setName(user.getName());
                existingUser.setContact(user.getContact());
                existingUser.setLocation(user.getLocation());
                existingUsers.add(existingUser);
            }
            return existingUsers;
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Gets the total count of users of a specified type
     *
     * @param userType Type of users to count
     * @return Total number of users
     * @throws DataBaseAccessError if database access fails
     */
    public int getUserCount(final String userType) throws DataBaseAccessError {
        try {
            final UserDao userDao = findUserType(userType);
            return userDao.getUserCount();
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Updates password for a specified user
     *
     * @param userType    Type of user
     * @param user with a new password to update the password for the user with the specified id.
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access fails
     */
    public int updatePassword(final String userType, final UserRegistrationDto user) throws DataBaseAccessError {
        try {
            final UserDao userDao = findUserType(userType);
            final String contact = user.getContact();
            final String newPassword = user.getPassword();
            return userDao.updatePassword(contact, newPassword);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Updates user profile information
     *
     * @param userType Type of user
     * @param user     User object with updated information
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access fails
     */
    public int updateProfile(final String userType, final UserRegistrationDto user) throws DataBaseAccessError {
        try {
            final UserDao userDao = findUserType(userType);
            User existingUser = new User();
            existingUser.setId(0);
            existingUser.setName(user.getName());
            existingUser.setContact(user.getContact());
            existingUser.setLocation(user.getLocation());
            return userDao.updateProfile(existingUser);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Removes a user from the system
     *
     * @param userType Type of user to remove
     * @param id ID of the user to remove
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access fails
     */
    public int remove(final String userType, final int id) throws DataBaseAccessError {
        try {
            final UserDao userDao = findUserType(userType);
            return userDao.deleteUser(id);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Validates a user type and returns the appropriate UserDao instance
     *
     * @param userType Type of user to validate
     * @return UserDao instance for the specified type
     * @throws IllegalArgumentException if a user type is invalid
     */
    private UserDao findUserType(String userType) {
        final UserDao userDao = userDaoFactory.getUserDao(userType);
        if (userDao == null) {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
        return userDao;
    }


}
