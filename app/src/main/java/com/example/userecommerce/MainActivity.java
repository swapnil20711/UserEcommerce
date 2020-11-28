package com.example.userecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.userecommerce.adapters.ProductsAdapter;
import com.example.userecommerce.databinding.ActivityMainBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.Inventory;
import com.example.userecommerce.models.Product;
import com.example.userecommerce.models.Variant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private Cart cart = new Cart();
    private ProductsAdapter adapter;
    private MyApp app;
    private List<Product> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        super.onCreate(savedInstanceState);
        app = (MyApp) getApplicationContext();
        loadPreviousData();
    }

    private void setUpProductList() {
        adapter = new ProductsAdapter(this, list, cart);

        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Divider
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        b.recyclerView.addItemDecoration(itemDecor);
        b.recyclerView.setAdapter(adapter);
        b.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                intent.putExtra("data", cart);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void loadPreviousData() {
        if (app.isOffline()) {
            app.showToast(MainActivity.this, "No Internet");
            return;
        }
        app.showLoadingDialog(this);
        app.db.collection("inventory")
                .document("products")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists()) {
                    Inventory inventory = snapshot.toObject(Inventory.class);
                    list = inventory.products;
                }
                else{
                    list=new ArrayList<>();
                }
                setUpProductList();

                app.hideLoadingDialog();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        app.hideLoadingDialog();
                    }
                })
        ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.e("aa gaya", (Cart) data.getSerializableExtra("cart") + "");
                updateCartSummary((Cart) data.getSerializableExtra("cart"));
            }

        }
    }

    public void updateCartSummary(Cart cart) {
        if (cart.noOfItems == 0) {
            b.checkout.setVisibility(View.GONE);
        } else {
            b.checkout.setVisibility(View.VISIBLE);

            b.cartSummary.setText("Total : Rs. " + cart.subTotal + "\n" + cart.noOfItems + " items");
        }

    }
}