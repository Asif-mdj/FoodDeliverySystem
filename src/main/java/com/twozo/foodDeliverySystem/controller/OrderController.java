package com.twozo.foodDeliverySystem.controller;

import com.twozo.foodDeliverySystem.model.OrderStatus;
import com.twozo.foodDeliverySystem.model.dto.OrderListResponse;
import com.twozo.foodDeliverySystem.model.dto.MessageResponseDto;
import com.twozo.foodDeliverySystem.model.dto.OrderPlacementRequest;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.model.FoodItem;
import com.twozo.foodDeliverySystem.service.OrderService;
import com.twozo.foodDeliverySystem.validation.InputDataValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;


/**
 * REST controller for managing order operations
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final InputDataValidator defaultInputDataValidator;
    private MessageResponseDto messageResponseDto;

    public OrderController(final OrderService orderService,
                           final InputDataValidator defaultInputDataValidator,
                           MessageResponseDto messageResponseDto) {
        this.orderService = orderService;
        this.defaultInputDataValidator = defaultInputDataValidator;
        this.messageResponseDto = messageResponseDto;
    }

    /**
     * Get a list of available orders
     *
     * @return ResponseEntity containing a list of available orders
     */
    @GetMapping("/availableOrders")
    public ResponseEntity<?> getAvailableOrders() {
        try {
            final Collection<OrderListResponse> orders = orderService.getAvailableOrders();
            if (orders.isEmpty()) {
                final String errorMessage = "No available orders found";
                return getNoContentResponseEntity(errorMessage);
            } else {
                return ResponseEntity.ok(orders);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<MessageResponseDto> getNoContentResponseEntity(String errorMessage) {
        messageResponseDto.setMessage(errorMessage);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageResponseDto);
    }

    /**
     * Get a list of allocated orders
     *
     * @return ResponseEntity containing a list of allocated orders
     */
    @GetMapping("/allocatedOrders")
    public ResponseEntity<?> getAllocatedOrders() {
        try {
            final Collection<OrderListResponse> orders = orderService.getAllocatedOrders();
            if (orders.isEmpty()) {
                final String errorMessage = "No allocated orders found";
                return getNoContentResponseEntity(errorMessage);
            } else {
                return ResponseEntity.ok(orders);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a list of completed orders
     *
     * @return ResponseEntity containing a list of completed orders
     */
    @GetMapping("/completedOrders")
    public ResponseEntity<?> getCompletedOrders() {
        try {
            final Collection<OrderListResponse> orders = orderService.getCompletedOrders();
            if (orders.isEmpty()) {
                final String errorMessage = "No completed orders found";
                return getNoContentResponseEntity(errorMessage);
            } else {
                return ResponseEntity.ok(orders);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get food items for a specific order
     *
     * @param id ID of the order
     * @return ResponseEntity containing a collection of food items in the order
     */
    @PostMapping("/orderedFoodItems")
    public ResponseEntity<?> getOrderedFoodItem(@RequestParam("id") final int id) {
        try {
            if (!defaultInputDataValidator.validateId(id)) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
            final Collection<FoodItem> orderedFoodItem = orderService.getOrderedFoodItem(id);
            if (orderedFoodItem.isEmpty()) {
                final String errorMessage = "No food items found for order id: " + id;
                return getNoContentResponseEntity(errorMessage);
            } else {
                return ResponseEntity.ok(orderedFoodItem);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get count of available orders
     *
     * @return ResponseEntity containing available order count
     */
    @GetMapping("/availableOrderCount")
    public ResponseEntity<MessageResponseDto> getAvailableOrderCount() {
        try {
            final int result = orderService.getAvailableOrdersCount();
            final String orderCountMessage = "Available Order Count: " + result;
            final String noOrderCountMessage = "No orders found";
            return getOutcomeResponseEntity(result, orderCountMessage, noOrderCountMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get count of allocated orders
     *
     * @return ResponseEntity containing allocated order count
     */
    @GetMapping("/allocatedOrderCount")
    public ResponseEntity<MessageResponseDto> getAllocatedOrderCount() {
        try {
            final int result = orderService.getAllocatedOrdersCount();
            final String orderCountMessage = "Allocated Order Count: " + result;
            final String noOrderCountMessage = "No orders found";
            return getOutcomeResponseEntity(result, orderCountMessage, noOrderCountMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get count of completed orders
     *
     * @return ResponseEntity containing a completed order count
     */
    @GetMapping("/completedOrderCount")
    public ResponseEntity<MessageResponseDto> getCompletedOrderCount() {
        try {
            final int result = orderService.getCompletedOrderCount();
            final String orderCountMessage = "Completed Order Count: " + result;
            final String noOrderCountMessage = "No orders found";
            return getOutcomeResponseEntity(result, orderCountMessage, noOrderCountMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Place a new order
     *
     * @param orderPlacementRequest with details od order to be placed.
     * @return ResponseEntity containing order placement status
     */
    @PostMapping("/orderPlacement")
    public ResponseEntity<MessageResponseDto> placeOrder(@RequestBody final OrderPlacementRequest orderPlacementRequest) {
        try {
            final String customerContact = orderPlacementRequest.getCustomerContact();
            final int hotelId = orderPlacementRequest.getHotelId();
            final LocalDateTime localDateTime = orderPlacementRequest.getDateTime();
            final String deliveryLocation = orderPlacementRequest.getDeliveryLocation();
            final Collection<FoodItem> orderFoodItems = orderPlacementRequest.getOrderedFoodItems();
            if (!defaultInputDataValidator.validateContact(customerContact)) {
                final String errorMessage = "Invalid customer contact";
                return getInvalidInputResponseEntity(errorMessage);
            } else if (!defaultInputDataValidator.validateId(hotelId)) {
                final String errorMessage = "Invalid hotel id";
                return getInvalidInputResponseEntity(errorMessage);
            } else if (!defaultInputDataValidator.validateDateTime(localDateTime)) {
                final String errorMessage = "Invalid date time";
                return getInvalidInputResponseEntity(errorMessage);
            } else if (!defaultInputDataValidator.validateLocation(deliveryLocation)) {
                final String errorMessage = "Invalid delivery location";
                return getInvalidInputResponseEntity(errorMessage);
            } else if (Objects.nonNull(orderFoodItems)) {
                if (orderFoodItems.isEmpty()) {
                    final String errorMessage = "Order food list is empty";
                    return getInvalidInputResponseEntity(errorMessage);
                }
                for (FoodItem foodItem : orderFoodItems) {
                    if (!defaultInputDataValidator.validateId(foodItem.getId())) {
                        final String errorMessage = "Invalid food item Id";
                        return getInvalidInputResponseEntity(errorMessage);
                    } else if (!defaultInputDataValidator.validatePrice(foodItem.getPrice())) {
                        final String errorMessage = "Invalid food item price";
                        return getInvalidInputResponseEntity(errorMessage);
                    }
                }
            }
            final int result = orderService.placeOrder(orderPlacementRequest);
            final String successMessage = "Your order has been placed successfully. Order Id: " + result;
            final String failureMessage = "No orders found";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError d) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Assign order to delivery agent
     *
     * @param orderId ID of the order
     * @param deliveryAgentId ID of the delivery agent
     * @return ResponseEntity containing assignment status
     */
    @PutMapping("/accept")
    public ResponseEntity<MessageResponseDto> acceptOrder(@RequestParam("orderId") final int orderId,
                                                          @RequestParam("deliveryAgentId") final int deliveryAgentId) {
        try {
            if (!defaultInputDataValidator.validateId(orderId)) {
                final String errorMessage = "Invalid order id";
                return getInvalidInputResponseEntity(errorMessage);
            } else if (!defaultInputDataValidator.validateId(deliveryAgentId)) {
                final String errorMessage = "Invalid delivery agent";
                return getInvalidInputResponseEntity(errorMessage);
            }
            final int result = orderService.acceptOrder(orderId, deliveryAgentId);
            final String successMessage = "Order assigned successfully";
            final String failureMessage = "Failed to assign order, please enter valid order id";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Mark the order as completed by the delivery agent
     *
     * @param orderId ID of the order
     * @return ResponseEntity containing completion status
     */
    @PutMapping("/updateStatus")
    public ResponseEntity<MessageResponseDto> updateOrderStatus(@RequestParam("orderId") final int orderId,
                                                                @RequestParam("orderStatus") final String orderStatus) {
        try {
            OrderStatus orderStatusEnum = OrderStatus.valueOf(orderStatus);
            if (!defaultInputDataValidator.validateId(orderId)) {
                final String errorMessage = "Invalid order id";
                return getInvalidInputResponseEntity(errorMessage);
            } else if (!defaultInputDataValidator.validateOrderStatus(orderStatusEnum)) {
                final String errorMessage = "Invalid order status";
                return getInvalidInputResponseEntity(errorMessage);
            }
            final int result = orderService.updateOrderStatus(orderId, orderStatusEnum);
            final String successMessage = "Order status update successfully";
            final String failureMessage = "Failed to update order status, please enter valid order id";
            return getOutcomeResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<MessageResponseDto> getInvalidInputResponseEntity(String errorMessage) {
        messageResponseDto.setMessage(errorMessage);
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
            return ResponseEntity.ok(messageResponseDto);
        }

    }

}