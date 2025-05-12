package com.twozo.foodDeliverySystem.dao;

import java.sql.SQLException;
import java.util.List;

import com.twozo.foodDeliverySystem.model.FoodItem;

/**
 * Data Access Object interface for food item-related database operations
 */
public interface FoodItemDao {

    /**
     * SQL query to register a new food item
     */
    String REGISTER_FOOD_ITEM = "INSERT INTO fds_food_item (name) VALUES (?) RETURNING id;";

    /**
     * SQL query to get food item ID by name
     */
    String GET_FOOD_ITEM_ID_BY_NAME = "SELECT id FROM fds_food_item WHERE name = ?;";

    /**
     * SQL query to delete a food item from database
     */
    String DELETE_FOOD_FROM_DATABASE = "DELETE FROM fds_food_item WHERE id = ?;";

    /**
     * SQL query to get all registered food items
     */
    String GET_ALL_REGISTERED_FOOD_ITEMS = "SELECT * FROM fds_food_item ORDER BY id ASC;";

    /**
     * Register a new food item in the database
     *
     * @param foodName Name of the food item to register
     * @return Generated food item ID
     * @throws SQLException if database error occurs
     */
    int registerFoodItem(final String foodName) throws SQLException;

    /**
     * Get food item details by name
     *
     * @param foodName Name of the food item to retrieve
     * @return FoodItem object containing item details
     * @throws SQLException if database error occurs
     */
    FoodItem getFoodItemByName(final String foodName) throws SQLException;

    /**
     * Get list of all registered food items
     *
     * @return List of all FoodItem objects
     * @throws SQLException if database error occurs
     */
    List<FoodItem> getAllRegisteredFoodItems() throws SQLException;

    /**
     * Delete a food item from database
     *
     * @param id ID of the food item to delete
     * @return Number of rows affected
     * @throws SQLException if database error occurs
     */
    int deleteFoodItemFromDatabase(final int id) throws SQLException;

}
