package com.zephyr.edge.model;

public class FilledPurchase {
    private Clothes clothes;
    private Integer amount;

    public FilledPurchase(Clothes clothes, Integer amount) {
        this.clothes = clothes;
        this.amount = amount;
    }

    public Clothes getClothes() {
        return clothes;
    }


    public Integer getAmount() {
        return amount;
    }

}
