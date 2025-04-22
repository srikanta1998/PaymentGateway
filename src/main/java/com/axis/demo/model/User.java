package com.axis.demo.model;

public class User {

    private String email;
    private String password;

    // Constructors (optional but helpful)
    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{email='" + email + "', password='" + password + "'}";
    }

}
