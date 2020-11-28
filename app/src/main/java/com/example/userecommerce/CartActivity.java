package com.example.userecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.userecommerce.databinding.ActivityCartBinding;
import com.example.userecommerce.databinding.CartItemBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.CartItem;

import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding b;
    private CartItemBinding binding;
    private Cart cart;
    public static final String KEY_RESULT="cart";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


        Intent intent = getIntent();
        cart = (Cart) intent.getSerializableExtra("data");

        for (Map.Entry<String, CartItem> cartItemEntry : cart.cartItemMap.entrySet()) {
            binding = CartItemBinding.inflate(getLayoutInflater());
            binding.productName.setText(cartItemEntry.getKey());
            binding.productQtyAndPrice.setText(cartItemEntry.getValue().qty + " kg" + " × " + " ₹" + Math.ceil(cartItemEntry.getValue().price / cartItemEntry.getValue().qty) + "/kg");
            binding.productTotal.setText("₹ " + cartItemEntry.getValue().price + "");
            b.cartItems.addView(binding.getRoot());
            setUpDeleteButtons(b, cart, cartItemEntry.getKey(), cartItemEntry);
        }


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra(KEY_RESULT, cart);
        setResult(RESULT_OK, i);
        finish();
    }

    private void setUpDeleteButtons(ActivityCartBinding b, Cart cart, String key, Map.Entry<String, CartItem> cartItemEntry) {
        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.cartItemMap.remove(key);
                cart.subTotal -= cartItemEntry.getValue().price;
                cart.noOfItems -= cartItemEntry.getValue().qty;
                b.cartItems.removeView((View)v.getParent());
                Log.e("main", cart + "");
            }
        });
    }
}