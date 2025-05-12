package com.twozo.foodDeliverySystem.service;

import java.util.List;

import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.FoodItem;
import com.twozo.foodDeliverySystem.model.Hotel;
import com.twozo.foodDeliverySystem.model.MenuItem;

/**
 * Service interface for handling hotel menu operations
 */
public interface MenuHandlerService {

    /**
     * Adds a new food item to the hotel's menu
     *
     * @param menuItem to be added in the menu
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access error occurs
     */
    int addFoodItem(final MenuItem menuItem) throws DataBaseAccessError;

    /**
     * Removes a food item from hotel's menu
     *
     * @param hotelId ID of the hotel
     * @param foodItemId ID of the foodItem
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access error occurs
     */
    public int removeFoodItem(final int hotelId, final int foodItemId) throws DataBaseAccessError;

    /**
     * Updates price of a food item in hotel's menu
     *
     * param menuItem with new price to update
     * @return Number of rows affected
     * @throws DataBaseAccessError if database access error occurs
     */
    public int UpdateFoodPrice(final MenuItem menuItem) throws DataBaseAccessError;

    /**
     * Gets the complete menu for a hotel
     *
     * @param id ID of the hotel
     * @return List of food items in hotel's menu
     * @throws DataBaseAccessError if database access error occurs
     */
    List<FoodItem> getMenu(final int id) throws DataBaseAccessError;

}
