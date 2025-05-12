package com.twozo.foodDeliverySystem.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.twozo.foodDeliverySystem.model.FoodItem;

/**
 * Interface for managing hotel menu operations
 */
public interface MenuDao {

    /**
     * SQL query to add a food item to hotel menu
     */
    String ADD_FOOD_ITEM_TO_MENU = "INSERT INTO fds_hotel_food_item (hotel_id, food_item_id, food_item_price) VALUES (?,?,?);";

    /**
     * SQL query to get menu items for a hotel
     */
    String GET_MENU = "SELECT food_item_id, name, food_item_price FROM fds_hotel_food_item JOIN fds_food_item ON fds_hotel_food_item.food_item_id = fds_food_item.id WHERE fds_hotel_food_item.hotel_id = ? ORDER BY food_item_id ASC;";

    /**
     * SQL query to update price of a food item
     */
    String UPDATE_FOOD_PRICE = "UPDATE fds_hotel_food_item SET food_item_price = ? WHERE hotel_id = ? AND food_item_id = ?;";

    /**
     * SQL query to delete a food item from menu
     */
    String DELETE_FOOD_FROM_MENU = "DELETE FROM fds_hotel_food_item WHERE hotel_id=? AND food_item_id = ?;";

    /**
     * SQL query to get menu item IDs
     */
    String GET_MENU_IDS = "SELECT id FROM fds_hotel_food_item WHERE hotel_id = ? AND food_item_id = ?;";


    /**
     * Adds a new food item to hotel menu
     *
     * @param hotelId  ID of the hotel
     * @param foodItem Food item to be added
     * @return Number of rows affected
     * @throws SQLException if database access error occurs
     */
    int addFoodItemToHotelMenu(final int hotelId,final FoodItem foodItem) throws SQLException;

    /**
     * Gets a menu for a specific hotel
     *
     * @param hotelId ID of the hotel
     * @return List of food items in the menu
     * @throws SQLException if database access error occurs
     */
    List<FoodItem> getMenu(final int hotelId) throws SQLException;

    /**
     * Updates price of a food item in hotel menu
     *
     * @param hotelId  ID of the hotel
     * @param foodItem Food item with updated price
     * @return Number of rows affected
     * @throws SQLException if database access error occurs
     */
    int updateFoodPrice(final int hotelId,final FoodItem foodItem) throws SQLException;

    /**
     * Deletes a food item from hotel menu
     *
     * @param hotelId    ID of the hotel
     * @param foodItemId ID of the food item to delete
     * @return Number of rows affected
     * @throws SQLException if database access error occurs
     */
    int deleteFoodItemFromMenu(final int hotelId, final int foodItemId) throws SQLException;

    /**
     * Gets menu IDs for a list of food items
     *
     * @param hotelId   ID of the hotel
     * @param foodItems List of food items
     * @return List of menu IDs
     * @throws SQLException if database access error occurs
     */
    List<Integer> getMenuIds(final int hotelId, final List<FoodItem> foodItems) throws SQLException;

}
