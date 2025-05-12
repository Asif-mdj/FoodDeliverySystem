package com.twozo.foodDeliverySystem.controller;

import com.twozo.foodDeliverySystem.model.dto.MessageResponseDto;
import com.twozo.foodDeliverySystem.model.dto.RetrieveUserResponse;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.service.AvailabilityManagerService;
import com.twozo.foodDeliverySystem.validation.InputDataValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * REST controller for managing delivery agent operations.
 */
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final AvailabilityManagerService availabilityManagerService;
    private final InputDataValidator defaultInputDataValidator;
    private MessageResponseDto messageResponseDto;

    /**
     * Constructor for DeliveryController.
     *
     * @param availabilityManagerService Service to manage delivery agent availability
     */
    public DeliveryController(final AvailabilityManagerService availabilityManagerService,
                              final InputDataValidator defaultInputDataValidator,
                              MessageResponseDto messageResponseDto) {
        this.availabilityManagerService = availabilityManagerService;
        this.defaultInputDataValidator = defaultInputDataValidator;
        this.messageResponseDto = messageResponseDto;
    }

    /**
     * Gets a list of all available delivery agents.
     *
     * @return ResponseEntity containing a list of available agents or no content if none found
     */
    @GetMapping("/availableAgents")
    public ResponseEntity<?> getAvailableAgents() {
        try {
            final Collection<RetrieveUserResponse> agents = availabilityManagerService.getAvailableAgents();
            if (agents.isEmpty()) {
                final String errorMessage = "No available agents found";
                messageResponseDto.setMessage(errorMessage);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageResponseDto);
            } else {
                return ResponseEntity.ok(agents);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Gets count of available delivery agents.
     *
     * @return ResponseEntity containing a count of available agents or no content if zero
     */
    @GetMapping("/availableAgentsCount")
    public ResponseEntity<MessageResponseDto> getAvailableAgentsCount() {
        try {
            final int count = availabilityManagerService.getAvailableAgentsCount();
            final String successMessage = "Available Agents Count: " + count;
            if (count == 0) {
                return ResponseEntity.noContent().build();
            } else {
                messageResponseDto.setMessage(successMessage);
                return ResponseEntity.ok(messageResponseDto);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates availability status of a delivery agent.
     *
     * @param id ID of the delivery agent.
     * @param availability the availability status of the delivery agent either true or false.
     * @return ResponseEntity with success message or error status
     */
    @PostMapping("/agentStatus")
    public ResponseEntity<MessageResponseDto> changeAgentStatus(@RequestParam("id") final int id,
                                                                @RequestParam("availability") final boolean availability) {
        try {
            if (!defaultInputDataValidator.validateId(id)) {
                final String message = "Invalid ID";
                messageResponseDto.setMessage(message);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(messageResponseDto);
            } else if (!defaultInputDataValidator.validateAvailability(availability)) {
                final String message = "Invalid Availability Status";
                messageResponseDto.setMessage(message);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(messageResponseDto);
            }
            final int result = availabilityManagerService.changeStatus(id, availability);
            if (result > 0) {
                messageResponseDto.setMessage("Agent status changed successfully");
                return ResponseEntity.ok(messageResponseDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}