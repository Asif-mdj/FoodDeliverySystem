package com.twozo.foodDeliverySystem.service;

import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.FoodItem;

import java.util.List;

/**
 * Service interface for managing food items in the system
 */
public interface FoodItemService {

    /**
     * Retrieves all registered food items from the database
     *
     * @return List of all registered FoodItem objects
     * @throws DataBaseAccessError if there is an error accessing the database
     */
    List<FoodItem> getRegisteredFoodItems() throws DataBaseAccessError;

    /**
     * Finds a food item by its name
     *
     * @param foodName Name of the food item to search for
     * @return FoodItem object if found
     * @throws DataBaseAccessError if there is an error accessing the database
     */
    FoodItem getFoodItemByName(final String foodName) throws DataBaseAccessError;

    /**
     * Registers a new food item in the system
     *
     * @param foodName of the new food item to register
     * @return Number of rows affected in database (positive number indicates success)
     * @throws DataBaseAccessError if there is an error accessing the database
     */
    int registerNewFoodItem(final String foodName) throws DataBaseAccessError;

    /**
     * Deletes a food item from the database
     *
     * @param id ID of the food item to delete
     * @return Number of rows affected in database (positive number indicates success)
     * @throws DataBaseAccessError if there is an error accessing the database
     */
    int deleteFoodItemFromDatabase(final int id) throws DataBaseAccessError;

}
