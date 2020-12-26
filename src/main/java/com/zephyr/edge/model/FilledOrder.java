package com.zephyr.edge.model;

import java.util.ArrayList;

public class FilledOrder {
    private String uuid;
    private User user;
    private ArrayList<FilledPurchase> purchaseList;
    private boolean paid;

    public FilledOrder() {
    }

    public FilledOrder(String uuid, User user, ArrayList<FilledPurchase> purchaseList, boolean paid) {
        this.uuid = uuid;
        this.user = user;
        this.purchaseList = purchaseList;
        this.paid = paid;
    }

    public String getUuid() {
        return uuid;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<FilledPurchase> getPurchaseList() {
        return purchaseList;
    }

    public boolean isPaid() {
        return paid;
    }

}
