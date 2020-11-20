package com.example.userecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userecommerce.databinding.ActivityCartBinding;
import com.example.userecommerce.databinding.CartItemBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.CartItem;

import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding b;
    private CartItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        Intent intent = getIntent();
        Cart cart = (Cart) intent.getSerializableExtra("data");

        for (Map.Entry<String, CartItem> cartItemEntry : cart.cartItemMap.entrySet()) {
//            Toast.makeText(this, "" + cart, Toast.LENGTH_SHORT).show();
            binding = CartItemBinding.inflate(getLayoutInflater());
            binding.productName.setText(cartItemEntry.getKey());
            binding.productQtyAndPrice.setText(cartItemEntry.getValue().qty + " kg" + " × " + " ₹" + Math.ceil(cartItemEntry.getValue().price / cartItemEntry.getValue().qty) + "/kg");
            binding.productTotal.setText("₹ " + cartItemEntry.getValue().price + "");
            b.cartItems.addView(binding.getRoot());
            setUpDeleteButtons(b, cart, cartItemEntry.getKey());
        }
    }

    private void setUpDeleteButtons(ActivityCartBinding b, Cart cart, String key) {
        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.cartItemMap.remove(key);
                cart.subTotal=0;
                cart.noOfItems=0;
                Log.e("main",cart+"");
                b.cartItems.removeView((View)v.getParent());
            }
        });
    }
}