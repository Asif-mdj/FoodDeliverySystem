package com.twozo.foodDeliverySystem.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.twozo.foodDeliverySystem.dao.*;
import com.twozo.foodDeliverySystem.model.dto.HotelOrderResponse;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.FoodItem;
import com.twozo.foodDeliverySystem.model.MenuItem;
import org.springframework.stereotype.Service;

/**
 * Service class for handling hotel-related operations including menu management and order processing
 */
@Service
public class HotelService implements MenuHandlerService, HotelOrderService {

    private final MenuDao menuDao;
    private final HotelDao hotelDao;
    public static final String MESSAGE = "Failed, Unable to reach database";

    /**
     * Constructor for HotelService
     *
     * @param menuDao  DAO for menu operations
     * @param hotelDao DAO for hotel operations
     */
    public HotelService(final MenuDao menuDao, final HotelDao hotelDao) {
        this.menuDao = menuDao;
        this.hotelDao = hotelDao;
    }

    /**
     * Adds a new food item to the hotel's menu
     *
     * @param menuItem with hotel id, food item id and food item price
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public int addFoodItem(final MenuItem menuItem) throws DataBaseAccessError {
        try {
            final int hotelId = menuItem.getHotelId();
            final int foodItemId = menuItem.getFoodItemId();
            final double foodItemPrice = menuItem.getFoodItemPrice();
            final FoodItem foodItem = new FoodItem(foodItemId, foodItemPrice);
            return menuDao.addFoodItemToHotelMenu(hotelId, foodItem);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Removes a food item from the hotel's menu
     *
     * @param hotelId ID of the hotel
     * @param foodItemId ID of the foodItem
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public int removeFoodItem(final int hotelId, final int foodItemId) throws DataBaseAccessError {
        try {
            return menuDao.deleteFoodItemFromMenu(hotelId, foodItemId);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Updates price of a food item in the hotel's menu
     * param menuItem to update the price
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public int UpdateFoodPrice(MenuItem menuItem) throws DataBaseAccessError {
        try {
            final int hotelId = menuItem.getHotelId();
            final int foodItemId = menuItem.getFoodItemId();
            final double newFoodItemPrice = menuItem.getFoodItemPrice();
            final FoodItem foodItem = new FoodItem (foodItemId, newFoodItemPrice);
            return menuDao.updateFoodPrice(hotelId, foodItem);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Gets a complete menu of a hotel
     *
     * @param id of the hotel to get the menu
     * @return List of food items in the hotel's menu
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public List<FoodItem> getMenu(int id) throws DataBaseAccessError {
        try {
            return menuDao.getMenu(id);
        } catch (SQLException s) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Updates the status of an order to ready for pickup
     *
     * @param id order ID to update status
     * @return Number of rows affected in a database
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public int updateOrderStatus(final int id) throws DataBaseAccessError {
        try {
            return hotelDao.updateOrderStatusReadyForPickup(id);
        } catch (SQLException e) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

    /**
     * Gets all orders for a hotel
     *
     * @param id ID of the hotel
     * @return List of orders for the hotel
     * @throws DataBaseAccessError if database access fails
     */
    @Override
    public Collection<HotelOrderResponse> getHotelOrders(final int id) throws DataBaseAccessError {
        try {
            return hotelDao.getHotelOrders(id);
        } catch (SQLException e) {
            throw new DataBaseAccessError(MESSAGE);
        }
    }

}