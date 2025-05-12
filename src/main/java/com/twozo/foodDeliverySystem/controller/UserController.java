package com.twozo.foodDeliverySystem.controller;

import com.twozo.foodDeliverySystem.model.dto.MessageResponseDto;
import com.twozo.foodDeliverySystem.model.dto.RetrieveUserResponse;
import com.twozo.foodDeliverySystem.model.dto.UserRegistrationDto;
import com.twozo.foodDeliverySystem.exceptionHandler.DataBaseAccessError;
import com.twozo.foodDeliverySystem.service.UserServices;
import com.twozo.foodDeliverySystem.validation.InputDataValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * REST controller for managing user operations
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServices userManagementService;
    private final InputDataValidator defaultInputDataValidator;
    private MessageResponseDto messageResponseDto;

    /**
     * Constructor for dependency injection
     *
     * @param userManagementService Service for user management operations
     */
    public UserController(final UserServices userManagementService,
                          final InputDataValidator defaultInputDataValidator,
                          MessageResponseDto messageResponseDto) {
        this.defaultInputDataValidator = defaultInputDataValidator;
        this.userManagementService = userManagementService;
        this.messageResponseDto = messageResponseDto;
    }

    /**
     * Get user by contact information
     *
     * @param userType Type of user
     * @param contact contact detail to search for.
     * @return ResponseEntity containing user if found, or appropriate status code
     */
    @PostMapping("/userByContact")
    public ResponseEntity<?> getUserByContact(@RequestParam("userType") final String userType,
                                              @RequestParam("contact") final String contact) {
        try {
            if (!defaultInputDataValidator.validateUserType(userType)) {
                final String errorMessage = "Invalid User Type";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            final RetrieveUserResponse userToFind = userManagementService.getUserByContact(userType, contact);
            if (userToFind == null) {
                final String errorMessage = "User not found";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.ok(userToFind);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get user by ID
     *
     * @param userType Type of user
     * @param id ID of the user
     * @return ResponseEntity containing user if found, or appropriate status code
     */
    @PostMapping("/userById")
    public ResponseEntity<?> getUserById(@RequestParam("userType") final String userType,
                                         @RequestParam("id") final int id) {
        try {
            if (!defaultInputDataValidator.validateUserType(userType)) {
                final String errorMessage = "Invalid User Type";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
            } else if (!defaultInputDataValidator.validateId(id)) {
                final String errorMessage = "Invalid User ID";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            final RetrieveUserResponse userToFind = userManagementService.getUserById(userType, id);
            if (userToFind == null) {
                final String errorMessage = "User not found";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.ok(userToFind);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all users of a specified type
     *
     * @param userType Type of users to retrieve
     * @return ResponseEntity containing a list of users if found, or appropriate status code
     */
    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers(@RequestParam("userType") final String userType) {
        try {
            if (!defaultInputDataValidator.validateUserType(userType)) {
                final String errorMessage = "Invalid User Type";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            final Collection<RetrieveUserResponse> users = userManagementService.getAllUser(userType);
            if (users.isEmpty()) {
                final String errorMessage = "No users found";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.ok(users);
            }
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get count of users of a specified type
     *
     * @param userType Type of users to count
     * @return ResponseEntity containing user count if found, or appropriate status code
     */
    @GetMapping("/userCount")
    public ResponseEntity<MessageResponseDto> getUserCount(@RequestParam("userType") final String userType) {
        try {
            if (!defaultInputDataValidator.validateUserType(userType)) {
                final String errorMessage = "Invalid User Type";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            final int count = userManagementService.getUserCount(userType);
            final String successMessage = "User count is " + count;
            final String noUserCountMessage = "No users found";
            return getOutcomeDtoResponseEntity(count, successMessage, noUserCountMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Register new user
     *
     * @param userType Type of user to register
     * @param user     User object containing registration details
     * @return ResponseEntity with success/failure message and appropriate status code
     */
    @PostMapping("/userRegistration")
    public ResponseEntity<MessageResponseDto> registerUser(@RequestParam("userType") final String userType,
                                                           @RequestBody final UserRegistrationDto user) {
        try {
            ResponseEntity<MessageResponseDto> UNPROCESSABLE_ENTITY =
                    getInvalidUserDataResponseMessageDtoResponseEntity(userType, user);
            if (UNPROCESSABLE_ENTITY != null) return UNPROCESSABLE_ENTITY;
            final int result = userManagementService.registerUser(userType, user);
            final String successMessage = "User registered successfully, your user id: "+result;
            final String failureMessage = "Failed to register user, please enter valid new user contact details";
            return getOutcomeDtoResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update user profile
     *
     * @param userType Type of user
     * @param user     User object containing updated profile details
     * @return ResponseEntity with success/failure message and appropriate status code
     */
    @PutMapping("/userProfile")
    public ResponseEntity<MessageResponseDto> updateUserProfile(@RequestParam("userType") final String userType,
                                                                @RequestBody final UserRegistrationDto user) {
        try {
            ResponseEntity<MessageResponseDto> UNPROCESSABLE_ENTITY =
                    getInvalidUserDataResponseMessageDtoResponseEntity(userType, user);
            if (UNPROCESSABLE_ENTITY != null) return UNPROCESSABLE_ENTITY;
            final int result = userManagementService.updateProfile(userType, user);
            final String successMessage = "User profile updated successfully";
            final String failureMessage = "Failed to update user profile, please enter valid user credentials";
            return getOutcomeDtoResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update user password
     *
     * @param userType        Type of user
     * @param user with a new password and user id to update.
     * @return ResponseEntity with success/failure message and appropriate status code
     */
    @PutMapping("/userPassword")
    public ResponseEntity<MessageResponseDto> updateUserPassword(@RequestParam("userType") final String userType,
                                                                 @RequestBody final UserRegistrationDto user) {
        try {
            ResponseEntity<MessageResponseDto> UNPROCESSABLE_ENTITY =
                    getInvalidUserDataResponseMessageDtoResponseEntity(userType, user);
            if (UNPROCESSABLE_ENTITY != null) return UNPROCESSABLE_ENTITY;
            final int result = userManagementService.updatePassword(userType, user);
            final String successMessage = "User password updated successfully";
            final String failureMessage = "Failed to update user password, please enter valid user credentials";
            return getOutcomeDtoResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete user
     *
     * @param userType Type of user
     * @param id ID of the user to delete
     * @return ResponseEntity with success/failure message and appropriate status code
     */
    @DeleteMapping("/userDeletion")
    public ResponseEntity<MessageResponseDto> deleteUser(@RequestParam("userType") final String userType,
                                                         @RequestParam("id") final int id) {
        try {
            if (!defaultInputDataValidator.validateUserType(userType)) {
                final String errorMessage = "Invalid User Type";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
            } else if (!defaultInputDataValidator.validateId(id)) {
                final String errorMessage = "Invalid User ID";
                return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            final int result = userManagementService.remove(userType, id);
            final String successMessage = "User deleted successfully";
            final String failureMessage = "Failed to delete user, please enter valid user id";
            return getOutcomeDtoResponseEntity(result, successMessage, failureMessage, messageResponseDto);
        } catch (DataBaseAccessError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private static ResponseEntity<MessageResponseDto> getOutcomeDtoResponseEntity(final int result,
                                                                                  final String successMessage,
                                                                                  final String failureMessage,
                                                                                  MessageResponseDto messageResponseDto) {
        if (result > 0) {
            messageResponseDto.setMessage(successMessage);
        } else {
            messageResponseDto.setMessage(failureMessage);
        }
        return ResponseEntity.ok(messageResponseDto);
    }
    
    private ResponseEntity<MessageResponseDto>
    getInvalidUserDataResponseMessageDtoResponseEntity(final String userType,
                                                       final UserRegistrationDto user) {
        if (!defaultInputDataValidator.validateUserType(userType)) {
            final String errorMessage = "Invalid User Type";
            return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (!defaultInputDataValidator.validateName(user.getName())) {
            final String errorMessage = "Invalid User Name";
            return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (!defaultInputDataValidator.validateContact(user.getContact())) {
            final String errorMessage = "Invalid User Contact";
            return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (!defaultInputDataValidator.validateLocation(user.getLocation())) {
            final String errorMessage = "Invalid User Location";
            return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (!defaultInputDataValidator.validatePassword(user.getPassword())) {
            final String errorMessage = "Invalid User Password";
            return getInvalidInputResponseEntity(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return null;
    }

    private ResponseEntity<MessageResponseDto> getInvalidInputResponseEntity(final String errorMessage,
                                                                             final HttpStatus unprocessableEntity) {
        messageResponseDto.setMessage(errorMessage);
        return ResponseEntity.status(unprocessableEntity).body(messageResponseDto);
    }
    
}