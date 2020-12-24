package com.zephyr.edge.model;


public class Clothes {
    private String uuid;
    private String name;
    private String color;
    private String size;
    private String gender;
    private String brand;
    private double price;
    private String type;

    public Clothes() {
    }

//    public Clothes(String name, String color, String size, String gender, String brand, double price, String type) {
//        this.uuid = UUID.randomUUID().toString();
//        this.name = name;
//        this.color = color;
//        this.size = size;
//        this.gender = gender;
//        this.brand = brand;
//        this.price = price;
//        this.type = type;
//    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}