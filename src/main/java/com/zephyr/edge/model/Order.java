package com.zephyr.edge.model;

import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private String uuid;
    private String userID;
    private ArrayList<Purchase> purchaseList;
    private boolean paid;

    public Order() {
    }

    public Order(String userID) {
        this.uuid = UUID.randomUUID().toString();
        this.userID = userID;
        this.paid = false;
        this.purchaseList = new ArrayList<>();

    }

    public Order(String userID, Purchase purchase) {
        this.uuid = UUID.randomUUID().toString();
        this.userID = userID;
        this.paid = false;

        ArrayList<Purchase> constructPurchaseList = new ArrayList<>();
        constructPurchaseList.add(purchase);
        this.purchaseList = constructPurchaseList;
    }

    public Order(String userID, ArrayList<Purchase> purchaseList) {
        this.uuid = UUID.randomUUID().toString();
        this.userID = userID;
        this.paid = false;
        this.purchaseList = purchaseList;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(ArrayList<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}