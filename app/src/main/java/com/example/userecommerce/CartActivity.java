package com.example.userecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.userecommerce.databinding.ActivityCartBinding;
import com.example.userecommerce.databinding.CartItemBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.CartItem;

import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        Intent intent = getIntent();
        Cart cart = (Cart) intent.getSerializableExtra("data");

        for (Map.Entry<String, CartItem> cartItemEntry : cart.cartItemMap.entrySet()) {
//            Toast.makeText(this, "" + cart, Toast.LENGTH_SHORT).show();
            CartItemBinding binding = CartItemBinding.inflate(getLayoutInflater());
            binding.productName.setText(cartItemEntry.getKey());
            binding.productQtyAndPrice.setText(cartItemEntry.getValue().qty + " kg" + " × " + " ₹" + cartItemEntry.getValue().price + "/kg");
            binding.productTotal.setText("₹ " + cartItemEntry.getValue().price + "");
            b.cartItems.addView(binding.getRoot());
        }
    }
}