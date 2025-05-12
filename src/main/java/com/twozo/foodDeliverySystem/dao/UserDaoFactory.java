package com.twozo.foodDeliverySystem.dao;

/**
 * Factory interface for creating UserDao instances
 */
public interface UserDaoFactory {

    /**
     * Creates and returns a UserDao instance for the specified user type
     *
     * @param userType the type of user DAO to create
     * @return UserDao instance for the given type
     * @throws IllegalArgumentException if userType is invalid
     */
    UserDao getUserDao(final String userType);

}