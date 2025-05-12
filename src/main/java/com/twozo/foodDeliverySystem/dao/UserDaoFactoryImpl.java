package com.twozo.foodDeliverySystem.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Implementation of UserDaoFactory that provides UserDao instances based on user type
 */
@Repository
public class UserDaoFactoryImpl implements UserDaoFactory {

    /**
     * Map containing UserDao implementations for different user types
     */
    private final Map<String, UserDao> userDaoMap;

    /**
     * Constructor that initializes the UserDao implementations map
     *
     * @param customerDao      DAO for customer users
     * @param hotelDao         DAO for hotel users
     * @param deliveryAgentDao DAO for delivery agent users
     */
    public UserDaoFactoryImpl(@Qualifier("customerDao") UserDao customerDao,
                              @Qualifier("hotelDao") UserDao hotelDao,
                              @Qualifier("deliveryAgentDao") UserDao deliveryAgentDao) {

        userDaoMap = Map.of("customer", customerDao,
                "hotel", hotelDao, "deliveryAgent", deliveryAgentDao);
    }

    /**
     * Gets the UserDao implementation for a specific user type
     *
     * @param userType the type of user DAO to get
     * @return UserDao instance for the given type, or null if not found
     */
    @Override
    public UserDao getUserDao(final String userType) {
        return userDaoMap.get(userType);
    }

}
