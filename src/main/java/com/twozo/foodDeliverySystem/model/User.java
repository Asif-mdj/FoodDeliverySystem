package com.twozo.foodDeliverySystem.model;

/**
 * Represents a generic user in the food delivery system.
 * This interface defines common functionality for all user types
 * like customers, hotels and delivery agents.
 */
public class User {

    private int id;
    private String name;
    private String location;
    private String contact;
    private String password;

    public User() {
    }

    public User(int id, String name, String location, String contact, String password) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}