package com.zephyr.edge.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

public class User {

    private String uuid;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String streetName;
    private String number;
    private String postalCode;
    private String city;
    private String phoneNumber;

    @Enumerated(EnumType.ORDINAL)
    private Role role = Role.normalUser;

    public enum Role {
        admin, normalUser;
    }

    public User() {
    }

    public User(String userName, String firstName, String lastName, String email, String password, String streetName, String number, String postalCode, String city, String phoneNumber, Role role) {
        this.uuid = UUID.randomUUID().toString();
        setUserName(userName);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setStreetName(streetName);
        setNumber(number);
        setPostalCode(postalCode);
        setCity(city);
        setPhoneNumber(phoneNumber);
        if (role == null) {
            this.role = Role.normalUser;
        } else {
            this.role = role;
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }
}

