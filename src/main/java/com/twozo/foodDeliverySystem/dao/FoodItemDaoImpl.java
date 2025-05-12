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
 * Implementation of the FoodItemDao interface that handles database operations
 * related to food items.
 */
@Repository
public class FoodItemDaoImpl implements FoodItemDao {

    private final DataSource dataSource;

    /**
     * Constructor to initialize the connection pool if not already created.
     */
    public FoodItemDaoImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Registers a new food item in the database.
     *
     * @param foodName Name of the food item to register
     * @return ID of the newly registered food item
     * @throws SQLException if database access error occurs
     */
    @Override
    public int registerFoodItem(final String foodName) throws SQLException {

        int foodItemId = 0;
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REGISTER_FOOD_ITEM);
            preparedStatement.setString(1, foodName);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                foodItemId = resultSet.getInt(1);
            }
            preparedStatement.close();
            return foodItemId;
        } catch (SQLException e) {
            throw new SQLException("Error registering food item in database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves food item details by food name.
     *
     * @param foodName Name of the food item to search
     * @return FoodItem object containing found food item details, null if not found
     * @throws SQLException if database access error occurs
     */
    @Override
    public FoodItem getFoodItemByName(final String foodName) throws SQLException {

        FoodItem foodItem = null;
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_FOOD_ITEM_ID_BY_NAME);
            preparedStatement.setString(1, foodName);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final int foodItemId = resultSet.getInt(1);
                foodItem = new FoodItem(foodItemId, foodName);
            }
            preparedStatement.close();
            return foodItem;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving food item from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Retrieves all registered food items from the database.
     *
     * @return List of all registered FoodItem objects
     * @throws SQLException if database access error occurs
     */
    @Override
    public List<FoodItem> getAllRegisteredFoodItems() throws SQLException {

        List<FoodItem> registeredFoodItems = new ArrayList<>();
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_REGISTERED_FOOD_ITEMS);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int foodItemId = resultSet.getInt(1);
                final String foodName = resultSet.getString(2);
                final FoodItem foodItem = new FoodItem(foodItemId, foodName);
                registeredFoodItems.add(foodItem);
            }
            preparedStatement.close();
            return registeredFoodItems;
        } catch (SQLException e) {
            throw new SQLException("Error retrieving food items from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    /**
     * Deletes a food item from the database.
     *
     * @param id ID of the food item to delete
     * @return Number of rows affected by the delete operation
     * @throws SQLException if database access error occurs
     */
    @Override
    public int deleteFoodItemFromDatabase(int id) throws SQLException {

        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FOOD_FROM_DATABASE);
            preparedStatement.setInt(1, id);
            final int affectedRow = preparedStatement.executeUpdate();
            preparedStatement.close();
            return affectedRow;
        } catch (SQLException e) {
            throw new SQLException("Error deleting food item from database: " + e.getMessage());
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

}