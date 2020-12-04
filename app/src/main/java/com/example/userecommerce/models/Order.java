package com.example.userecommerce.models;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Order {
    public String userName, userAddress;
    private String userNumber;
    public Map<String, CartItem> map;
    public int subTotal;
    public int status;
    public Timestamp timestamp;

    public Order(String userName, String userAddress,String userNumber, Map<String, CartItem> map, int subTotal, int status) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userNumber = userNumber;
        this.map = map;
        this.subTotal = subTotal;
        this.status = status;
        this.timestamp = timestamp.now();
    }

    public static class OrderStatus {

        public static final int PLACED = 1 // Initially (U)
                , DELIVERED = 0, DECLINED = -1;     //(A)

    }

}
