package com.example.userecommerce.models;

import java.util.ArrayList;

public class Inventory  {
    public ArrayList<Product>products;

    public Inventory(ArrayList<Product> products) {
        this.products = products;
    }

    public Inventory() {
    }
}
