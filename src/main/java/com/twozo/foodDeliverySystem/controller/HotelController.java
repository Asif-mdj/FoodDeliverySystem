package com.twozo.foodDeliverySystem.controller;

import com.twozo.foodDeliverySystem.model.dto.HotelOrderResponse;
import com.twozo.foodDeliverySystem.model.dto.MessageResponseDto;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.FoodItem;
import com.twozo.foodDeliverySystem.model.MenuItem;
import com.twozo.foodDeliverySystem.service.*;
import com.twozo.foodDeliverySystem.validation.InputDataValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Controller for managing hotel-related operations in the food delivery system.
 * Handles endpoints for menu management, food item operations, and order processing.
 */
@RestController
@RequestMapping("/hotel")
public class HotelController {

    private final MenuHandlerService menuHandler;
    private final HotelOrderService hotelOrderService;
    private final FoodItemService foodItemManagementService;
    private final InputDataValidator defaultInputDataValidator;
    private MessageResponseDto messageResponseDto;

    /**
     * Constructor for HotelController
     *
     * @param menuHandler               Service for handling hotel menu operations
     * @param hotelOrderService         Service for managing hotel orders
     * @param foodItemManagementService Service for food item management
     */
    public HotelController(final MenuHandlerService menuHandler,
                           final HotelOrderService hotelOrderService,
                           final FoodItemService foodItemManagementService,
                           final InputDataValidator defaultInputDataValidator,
                           MessageResponseDto messageResponseDto) {
        this.menuHandler = menuHandler;
        this.hotelOrderService = hotelOrderService;
        this.foodItemManagementService = foodItemManagementService;
        this.defaultInputDataValidator = defaultInputDataValidator;
        this.messageResponseDto = messageResponseDto;
    }

    /**
     * Get menu items for a specific hotel
     *
     * @param id hotel id to get the menu
     * @return ResponseEntity containing a list of food items or appropriate status code
     */
    @PostMapping("/hotelMenu")
    public ResponseEntity<?> getHotelMenu (@RequestParam("id") final int id) {
        try {
            if (!defaultInputDataValidator.validateId(id)) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
            final Collection<FoodItem> menuFoodItems = menuHandler.getMenu(id);
            if (menuFoodItems.isEmpty()) {
                final String errorMessage = "No menu found for hotel with id: " + id;
                messageResponseDto.setMessage(errorMessage);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageResponseDto);
            } else {
                return ResponseEntity.ok(menuFoodItems);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a list of all registered food items
     *
     * @return ResponseEntity containing a list of registered food items or appropriate status code
     */
    @GetMapping("/registeredFoodItems")
    public ResponseEntity<?> getRegisteredFoodItems() {
        try {
            final Collection<FoodItem> registeredFoodItems = foodItemManagementService.getRegisteredFoodItems();
            if (registeredFoodItems.isEmpty()) {
                final String errorMessage = "No registered food items found";
                messageResponseDto.setMessage(errorMessage);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageResponseDto);
            } else {
                return ResponseEntity.ok(registeredFoodItems);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get orders for a specific hotel
     *
     * @param id ID of the hotel
     * @return ResponseEntity containing a list of orders or appropriate status code
     */
    @PostMapping("/hotelOrders")
    public ResponseEntity<?> getHotelOrders(@RequestParam("id") final int id) {
        try {
            if (!defaultInputDataValidator.validateId(id)) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
            final Collection<HotelOrderResponse> hotelOrders = hotelOrderService.getHotelOrders(id);
            if (hotelOrders.isEmpty()) {
                final String errorMessage = "No orders found for hotel with id: " + id;
                messageResponseDto.setMessage(errorMessage);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageResponseDto);
            } else {
                return ResponseEntity.ok(hotelOrders);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Add a food item to a hotel menu
     *
     * @param menuItem menu item of the hotel
     * @return ResponseEntity with a success/failure message
     */
    @PostMapping("/foodItemAddition")
    public ResponseEntity<MessageResponseDto> addFoodItem(@RequestBody final MenuItem menuItem) {
        try {
            if (!defaultInputDataValidator.validateId(menuItem.getHotelId())) {
                final String message = "Invalid Hotel ID";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            } else if (!defaultInputDataValidator.validateId(menuItem.getFoodItemId())) {
                final String message = "Invalid Food Item ID";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            } else if (!defaultInputDataValidator.validatePrice(menuItem.getFoodItemPrice())) {
                final String message = "Invalid Food Item Price";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            }
            final int result = menuHandler.addFoodItem(menuItem);
            final String successMessage = "Food item added successfully";
            final String failureMessage = "Failed to add food item";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Register a new food item
     *
     * @param foodName Name of the food item to be added in the hotel menu
     * @return ResponseEntity with a success/failure message
     */
    @PostMapping("/newFoodItemRegistration")
    public ResponseEntity<MessageResponseDto> registerNewFoodItem(@RequestParam("foodName") final String foodName) {

        try {
            if (!defaultInputDataValidator.validateName(foodName)) {
                final String message = "Invalid Food Name";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            }
            final int result = foodItemManagementService.registerNewFoodItem(foodName);
            final String successMessage = "Food item added successfully";
            final String failureMessage = "Failed to add food item";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update price of a food item
     * param menuItem with the price to update
     * @return ResponseEntity with a success/failure message
     */
    @PutMapping("/foodItemPrice")
    public ResponseEntity<MessageResponseDto> updateFoodItemPrice(@RequestBody MenuItem menuItem) {
        try {

            if (!defaultInputDataValidator.validateId(menuItem.getHotelId())) {
                final String message = "Invalid Hotel ID";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            } else if (!defaultInputDataValidator.validateId(menuItem.getFoodItemId())) {
                final String message = "Invalid Food Item ID";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            } else if (!defaultInputDataValidator.validatePrice(menuItem.getFoodItemPrice())) {
                final String message = "Invalid Food Item Price";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            }
            final int result = menuHandler.UpdateFoodPrice(menuItem);
            final String successMessage = "Food item price updated successfully";
            final String failureMessage = "Failed to update food item price";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update order status to ready for pickup
     *
     * @param id ID of the order
     * @return ResponseEntity with a success/failure message
     */
    @PutMapping("/orderStatus")
    public ResponseEntity<MessageResponseDto> updateOrderStatusToReadyForPickup(@RequestParam("id") final int id) {
        try {
            if (!defaultInputDataValidator.validateId(id)) {
                final String message = "Invalid ID";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            }
            final int result = hotelOrderService.updateOrderStatus(id);
            final String successMessage = "Order status updated successfully";
            final String failureMessage = "Failed to update order status";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a food item from a hotel menu
     *
     * @param hotelId ID of the hotel
     * @param foodItemId ID of the foodItem
     * @return ResponseEntity with a success/failure message
     */
    @DeleteMapping("/foodItemFromHotel")
    public ResponseEntity<MessageResponseDto> deleteFoodItem(@RequestParam("hotelId") final int hotelId,
                                                             @RequestParam("foodItemId") final int foodItemId) {
        try {
            if (!defaultInputDataValidator.validateId(hotelId)) {
                final String message = "Invalid Hotel ID";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            } else if (!defaultInputDataValidator.validateId(foodItemId)) {
                final String message = "Invalid Food Item ID";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            }
            final int result = menuHandler.removeFoodItem(hotelId, foodItemId);
            final String successMessage = "Food item deleted successfully";
            final String failureMessage = "Failed to delete food item";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a food item from registered food items
     *
     * @param id ID of the food item to delete
     * @return ResponseEntity with a success/failure message
     */
    @DeleteMapping("/foodItem")
    public ResponseEntity<MessageResponseDto> deleteFoodItemFromRegisteredFoodItems (@RequestParam("id") final int id) {
        try {
            if (!defaultInputDataValidator.validateId(id)) {
                final String message = "Invalid Food Item ID";
                return getInvalidInputResponseMessageDtoResponseEntity(message);
            }
            final int result = foodItemManagementService.deleteFoodItemFromDatabase(id);
            final String successMessage = "Food item deleted successfully";
            final String failureMessage = "Failed to delete food item";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<MessageResponseDto> getInvalidInputResponseMessageDtoResponseEntity(final String message) {
        messageResponseDto.setMessage(message);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(messageResponseDto);
    }

    private static ResponseEntity<MessageResponseDto> getOutcomeResponseEntity(final int result,
                                                                               final String successMessage,
                                                                               final String failureMessage,
                                                                               MessageResponseDto messageResponseDto) {
        if (result > 0) {
            messageResponseDto.setMessage(successMessage);
            return ResponseEntity.ok(messageResponseDto);
        } else {
            messageResponseDto.setMessage(failureMessage);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(messageResponseDto);
        }
    }

}