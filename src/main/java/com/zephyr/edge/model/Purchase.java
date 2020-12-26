package com.zephyr.edge.model;

public class Purchase {
    private String clothes;
    private Integer amount;

    public Purchase() {
    }

    public Purchase(String clothes) {
        this.clothes = clothes;
        this.amount = 1;
    }

    public Purchase(String clothes, Integer amount) {
        this.clothes = clothes;
        this.amount = amount;
    }

    public String getClothes() {
        return clothes;
    }

    public void setClothes(String clothes) {
        this.clothes = clothes;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}