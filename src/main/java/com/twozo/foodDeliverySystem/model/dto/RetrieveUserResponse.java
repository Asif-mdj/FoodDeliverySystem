package com.twozo.foodDeliverySystem.model.dto;

public class RetrieveUserResponse {

    private int id;
    private String name;
    private String location;
    private String contact;

    public RetrieveUserResponse() {
    }

    public RetrieveUserResponse(int id, String name, String location, String contact) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.contact = contact;
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
    @Override
    public String toString() {
        return "RetrieveUserResponse [id=" + id + ", name=" + name + ", location=" + location + ", contact=" + contact + "]";
    }

}
