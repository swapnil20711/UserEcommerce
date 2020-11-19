package com.example.userecommerce.models;

import java.util.ArrayList;

public class Variant extends ArrayList<Variant> {

    public String name;
    public int price;
    public int qty;

    @Override
    public String toString() {
        return  "Rs "+ price;
    }

    public Variant(String name, int price) {
        this.name = name;
        this.price = price;
    }

}
