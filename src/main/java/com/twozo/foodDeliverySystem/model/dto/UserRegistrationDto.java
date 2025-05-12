package com.twozo.foodDeliverySystem.model.dto;

public class UserRegistrationDto {

    private String name;
    private String location;
    private String contact;
    private String password;

    public UserRegistrationDto(String name, String location, String contact, String password) {
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.password = password;
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