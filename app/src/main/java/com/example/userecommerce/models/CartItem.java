package com.example.userecommerce.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    public String name;
    public int price;
    public float qty;

    public CartItem(String name, int price, float qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public CartItem(String name, int price) {
        this.name = name;
        this.price = price;
        qty = 1;
    }

    @Override
    public String toString() {
        return "models.CartItem{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                '}';
    }
}
