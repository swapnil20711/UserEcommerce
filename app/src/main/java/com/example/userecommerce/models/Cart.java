package com.example.userecommerce.models;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    public int subTotal = 0, noOfItems;
    Map<String, CartItem> cartItemMap = new HashMap<>();
    Map<String, Integer> totalCartItems = new HashMap<>();

    public int addVariantBasedProductToCart(Product product, Variant variant) {
        String key = product.name + " " + variant.name;
        if (cartItemMap.containsKey(key)) {
            CartItem existingCartItem = cartItemMap.get(key);
            existingCartItem.qty++;
            existingCartItem.price += variant.price;
        } else {
            cartItemMap.put(key, new CartItem(variant.name, variant.price));
        }
        noOfItems++;
        subTotal += variant.price;
        System.out.println(subTotal);
        if (totalCartItems.containsKey(product.name)) {
            int qty = totalCartItems.get(product.name) + 1;
            totalCartItems.put(product.name, qty);
        } else {
            totalCartItems.put(product.name, 1);
        }
        return (int) cartItemMap.get(key).qty;
    }

    public int removeVariantBasedProductFromCart(Product product, Variant variant) {
        String key = product.name + " " + variant.name;
        //Condition to check that the product is in cart or not
        if (cartItemMap.containsKey(key)) {
            noOfItems--;
            subTotal -= variant.price;
            cartItemMap.get(key).qty--;
            if (cartItemMap.get(key).qty == 0) {
                cartItemMap.remove(key);
            }

            int qty = totalCartItems.get(product.name) - 1;
            totalCartItems.put(product.name, qty);
            if (qty == 0) {
                totalCartItems.remove(product.name);
            }
        }
        return cartItemMap.containsKey(key) ? (int) cartItemMap.get(key).qty : 0;
    }

    public void updateWBProductInCart(Product product, float quantity) {
        int newPrice = (int) (product.price * quantity);
        if (cartItemMap.containsKey(product.name)) {
            subTotal -= cartItemMap.get(product.name).price;
            cartItemMap.put(product.name, new CartItem(product.name, newPrice, quantity));
            subTotal += newPrice;
        } else {
            noOfItems++;
            cartItemMap.put(product.name, new CartItem(product.name, newPrice, quantity));
            subTotal += newPrice;
        }

    }

    public void removeWBProductInCart(Product product) {
        if (cartItemMap.containsKey(product.name)) {
            noOfItems--;
            subTotal -= cartItemMap.get(product.name).price;
            cartItemMap.remove(product.name);
        }
    }

    public void removeAllVariants(Product product) {
        for (Variant variant : product.variants) {
            String key = product.name + " " + variant.name;
            if (cartItemMap.containsKey(key)) {
                subTotal -= cartItemMap.get(key).price;
                System.out.println(subTotal);
                noOfItems -= cartItemMap.get(key).qty;
                cartItemMap.remove(key);
            }
        }
        totalCartItems.remove(product.name);


    }


    @Override
    public String toString() {
        return "models.Cart{" +
                "subTotal=" + subTotal +
                ", noOfItems=" + noOfItems +
                ", cartItemMap=" + cartItemMap +
                ", totalCartItems=" + totalCartItems +
                '}';
    }
}
