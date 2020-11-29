package com.example.userecommerce.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Variant extends ArrayList<Variant> implements Serializable {

    public String name;
    public int price;
    public int qty;

    @Override
    public String toString() {
        return "Rs " + price;
    }

    public String nameAndPriceString() {
        return name + "-Rs." + price;
    }


    public Variant(String name, int price) {
        this.name = name;
        this.price = price;
    }
    public Variant(){

    }

}
