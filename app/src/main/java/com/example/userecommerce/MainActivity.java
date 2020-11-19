package com.example.userecommerce;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.userecommerce.adapters.ProductsAdapter;
import com.example.userecommerce.databinding.ActivityMainBinding;
import com.example.userecommerce.databinding.WeightPickerDialogBinding;
import com.example.userecommerce.dialog.WeightPicker;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.Product;
import com.example.userecommerce.models.Variant;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private Cart cart = new Cart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        super.onCreate(savedInstanceState);
        Variant v = new Variant("1kg", 45);
        Variant v1 = new Variant("2kg", 90);
        List<Product> list = Arrays.asList(new Product("kiwi", Arrays.asList(v)),
                new Product("apple",45,1.5f),
                new Product("kiwi",Arrays.asList(v,v1))
        );
        ProductsAdapter adapter = new ProductsAdapter(this, list, cart);

        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Divider
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        b.recyclerView.addItemDecoration(itemDecor);
        b.recyclerView.setAdapter(adapter);
    }

    public void updateCartSummary() {
        if (cart.noOfItems == 0) {
            b.checkout.setVisibility(View.GONE);
        } else {
            b.checkout.setVisibility(View.VISIBLE);

            b.cartSummary.setText("Total : Rs. " + cart.subTotal + "\n" + cart.noOfItems + " items");
        }

    }
}