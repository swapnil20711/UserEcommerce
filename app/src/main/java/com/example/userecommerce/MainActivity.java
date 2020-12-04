package com.example.userecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.userecommerce.adapters.ProductsAdapter;
import com.example.userecommerce.databinding.ActivityMainBinding;
import com.example.userecommerce.databinding.UserDetailsBinding;
import com.example.userecommerce.fcmsender.FCMSender;
import com.example.userecommerce.fcmsender.MessageFormatter;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.CartItem;
import com.example.userecommerce.models.Inventory;
import com.example.userecommerce.models.Order;
import com.example.userecommerce.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


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
        subscribeToTopic();
        loadPreviousData();
    }

    private void sendNotification(String order) {
        String message = MessageFormatter
                .getSampleMessage("admin", "Order placed", order, "https://cdn.pixabay.com/photo/2020/03/02/21/54/editorial-4897078_960_720.jpg");

        new FCMSender().send(message, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this).setTitle("Failure").setMessage(e.toString()).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this).setTitle("Success").setMessage(response.toString()).show();
                    }
                });
            }
        });
    }

    private void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("users");
        Toast.makeText(this, "Subscribed", Toast.LENGTH_SHORT).show();
    }

    private void setUpProductList() {
        adapter = new ProductsAdapter(this, list, cart);

        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Divider
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        b.recyclerView.addItemDecoration(itemDecor);
        b.recyclerView.setAdapter(adapter);
        b.checkout.setOnClickListener(new View.OnClickListener() {
            UserDetailsBinding binding = UserDetailsBinding.inflate(getLayoutInflater());
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setView(binding.getRoot())
                        .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (binding.userNumber.getText().toString().isEmpty() || binding.userAddress.getText().toString().isEmpty() || binding.userName.getText().toString().isEmpty()) {
                                    Toast.makeText(app, "All fields are required", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {
                                    submitOrder(binding.userName.getText().toString(), binding.userAddress.getText().toString(), binding.userNumber.getText().toString());
                                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                                    intent.putExtra("data", cart);

                                    startActivityForResult(intent, 1);
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();

            }
        });
    }

    private void submitOrder(String userName, String userAddress, String userNumber) {
        app.db.collection("orders").add(new Order(userName, userAddress, userNumber, cart.cartItemMap, cart.subTotal, Order.OrderStatus.PLACED))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String order_id = documentReference.getId();
                        documentReference.update("order_id", order_id);
                        sendNotification(order_id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Order failed", Toast.LENGTH_SHORT).show();
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
                } else {
                    list = new ArrayList<>();
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