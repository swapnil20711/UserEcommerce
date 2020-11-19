package com.example.userecommerce.controllers;

import com.example.userecommerce.databinding.ProductItemWbMultiVbBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.Product;

public class MultipleVBandProductWBViewBinder {
    private com.example.userecommerce.databinding.ProductItemWbMultiVbBinding b;
    private Product product;
    private Cart cart;

    public void bind(ProductItemWbMultiVbBinding b, Product product, Cart cart){

        this.b = b;
        this.product = product;
        this.cart = cart;
    }
}
