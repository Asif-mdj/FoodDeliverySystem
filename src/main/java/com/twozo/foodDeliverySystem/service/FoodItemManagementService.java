package com.twozo.foodDeliverySystem.service;

import com.twozo.foodDeliverySystem.dao.FoodItemDao;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.FoodItem;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Service implementation for managing food item operations
 * Implements FoodItemService interface
 */
@Service
public class FoodItemManagementService implements FoodItemService {

    /**
     * Data Access Object for food item operations
     */
    private final FoodItemDao foodItemDao;

    /**
     * Error message for database access failures
     */
    public static final String MESSAGE = "Failed, Unable to reach database";

    /**
     * Constructor for FoodItemManagementService
     *
     * @param foodItemDao Data access object for food items
     */
    public FoodItemManagementService(final FoodItemDao foodItemDao) {
        this.foodItemDao = foodItemDao;
    }

    /**
     * Get list of all registered food items
     *
     * @return List of FoodItem objects
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public List<FoodItem> getRegisteredFoodItems() throws DataBaseAccessError {
        try {
            return foodItemDao.getAllRegisteredFoodItems();
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Get food item by name
     *
     * @param foodName Name of the food item to retrieve
     * @return FoodItem object
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public FoodItem getFoodItemByName(final String foodName) throws DataBaseAccessError {
        try {
            return foodItemDao.getFoodItemByName(foodName);
        } catch (SQLException e) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Register a new food item
     *
     * @param foodName Name of the food item
     * @return Generated food item ID
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public int registerNewFoodItem(final String foodName) throws DataBaseAccessError {
        try {
            return foodItemDao.registerFoodItem(foodName);
        } catch (SQLException e) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Delete a food item from a database
     *
     * @param id ID of the food item to delete
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public int deleteFoodItemFromDatabase(final int id) throws DataBaseAccessError {
        try {
            return foodItemDao.deleteFoodItemFromDatabase(id);
        } catch (SQLException e) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

}