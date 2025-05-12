package com.twozo.foodDeliverySystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.twozo.foodDeliverySystem.model.FoodItem;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Implementation of the MenuDao interface that handles database operations related to restaurant menus.
 * Uses a connection pool to manage database connections efficiently.
 */
@Repository
public class MenuDaoImpl implements MenuDao {

    private final DataSource dataSource;

    /**
     * Constructor that initializes the connection pool if not already created
     */
    public MenuDaoImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a food item to a hotel's menu
     *
     * @param hotelId  ID of the hotel
     * @param foodItem FoodItem object containing item details
     * @return Number of rows affected in a database
     * @throws SQLException if the database operation fails
     */
    @Override
    public int addFoodItemToHotelMenu(final int hotelId, final FoodItem foodItem) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_FOOD_ITEM_TO_MENU);
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, foodItem.getId());
            preparedStatement.setDouble(3, foodItem.getPrice());
            final int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRows;
        } catch (SQLException e) {
            throw new SQLException("Error adding food item to menu: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves the complete menu for a specified hotel
     *
     * @param hotelId ID of the hotel
     * @return List of FoodItems in the hotel's menu
     * @throws SQLException if the database operation fails
     */
    @Override
    public List<FoodItem> getMenu(final int hotelId) throws SQLException {

        List<FoodItem> menuFoodItems = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MENU);
            preparedStatement.setInt(1, hotelId);
            final ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                final int foodItemId = rs.getInt("food_item_id");
                final String foodName = rs.getString("name");
                final double foodPrice = rs.getDouble("food_item_price");
                final FoodItem foodItem = new FoodItem(foodItemId, foodName, foodPrice);
                menuFoodItems.add(foodItem);
            }
            preparedStatement.close();
            return menuFoodItems;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving menu from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Updates the price of a food item in a hotel's menu
     *
     * @param hotelId  ID of the hotel
     * @param foodItem FoodItem object containing updated price
     * @return Number of rows affected in a database
     * @throws SQLException if the database operation fails
     */
    @Override
    public int updateFoodPrice(int hotelId, FoodItem foodItem) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FOOD_PRICE);
            preparedStatement.setDouble(1, foodItem.getPrice());
            preparedStatement.setInt(2, hotelId);
            preparedStatement.setInt(3, foodItem.getId());
            final int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRows;
        } catch (SQLException e) {
            throw new SQLException("Error updating food price: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Removes a food item from a hotel's menu
     *
     * @param hotelId    ID of the hotel
     * @param foodItemId ID of the food item to remove
     * @return Number of rows affected in a database
     * @throws SQLException if the database operation fails
     */
    @Override
    public int deleteFoodItemFromMenu(final int hotelId, final int foodItemId) throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FOOD_FROM_MENU);
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, foodItemId);
            final int affectedRows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRows;
        } catch (SQLException e) {
            throw new SQLException("Error deleting food item from menu: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Gets menu IDs for a list of food items from a specific hotel
     *
     * @param hotelId   ID of the hotel
     * @param foodItems List of FoodItems to get menu IDs for
     * @return List of menu IDs
     * @throws SQLException if the database operation fails
     */
    @Override
    public List<Integer> getMenuIds(int hotelId, List<FoodItem> foodItems) throws SQLException {

        List<Integer> menuIds = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            for (FoodItem foodItem : foodItems) {
                PreparedStatement preparedStatement = connection.prepareStatement(GET_MENU_IDS);
                preparedStatement.setInt(1, hotelId);
                preparedStatement.setInt(2, foodItem.getId());
                final ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    final int menuId = rs.getInt(1);
                    menuIds.add(menuId);
                }
                preparedStatement.close();
            }
            return menuIds;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving menu IDs from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }


}