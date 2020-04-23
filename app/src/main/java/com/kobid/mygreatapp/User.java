package com.kobid.mygreatapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.lang.reflect.Array;

@IgnoreExtraProperties
public class User {

    private String name;
    private String email;
    private String city;
    private String userId;
    private Array requestId;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String userId, String city) {
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.city= city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public Array getRequestId() { return requestId; }

    public void setRequestId(Array requestId) { this.requestId = requestId; }
}