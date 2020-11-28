package com.example.userecommerce.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.userecommerce.MainActivity;
import com.example.userecommerce.databinding.ProductItemSingleVbBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.Product;

import java.io.Serializable;

public class SingleVBProductViewBinder implements Serializable {
    private ProductItemSingleVbBinding binding;
    private Cart cart = new Cart();

    public void bind(ProductItemSingleVbBinding binding, Product product, Cart cart) {
        this.binding = binding;
        this.cart = cart;


        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.addVariantBasedProductToCart(product, product.variants.get(0));

                updateQtyViews(1);
            }
        });
        binding.incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cart.addVariantBasedProductToCart(product, product.variants.get(0));
                updateQtyViews(qty);
            }
        });
        binding.decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cart.removeVariantBasedProductFromCart(product, product.variants.get(0));
                updateQtyViews(qty);
            }
        });
    }
    private void updateCheckoutSummary() {
        Context context = binding.getRoot().getContext();
        if(context instanceof MainActivity){
            ((MainActivity) context).updateCartSummary(cart);
        } else {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateQtyViews(int qty) {
        if (qty == 1) {
            binding.addBtn.setVisibility(View.GONE);
            binding.qtyGroup.setVisibility(View.VISIBLE);
        } else if (qty == 0) {
            binding.addBtn.setVisibility(View.VISIBLE);
            binding.qtyGroup.setVisibility(View.GONE);
        }
        binding.quantity.setText(qty + "");
        updateCheckoutSummary();
    }
}
