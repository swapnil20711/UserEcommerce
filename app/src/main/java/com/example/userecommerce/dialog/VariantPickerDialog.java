package com.example.userecommerce.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.userecommerce.databinding.DialogVariantPickerBinding;
import com.example.userecommerce.databinding.VariantItemBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.Product;
import com.example.userecommerce.models.Variant;

import java.io.Serializable;

public class VariantPickerDialog implements Serializable {
    private Context context;
    private Cart cart;
    private Product product;
    private OnVariantPickedListener listener;
    private DialogVariantPickerBinding b;

    public void show(Context context, Cart cart, Product product, OnVariantPickedListener listener) {
        b = DialogVariantPickerBinding.inflate(LayoutInflater.from(context));

        this.context = context;
        this.cart = cart;
        this.product = product;
        this.listener = listener;
        new AlertDialog.Builder(context)
                .setTitle(product.name)
                .setCancelable(false)
                .setView(b.getRoot())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int qty = cart.totalVariantsCart.get(product.name);
                        if (qty > 0) {
                            listener.OnQtyUpdated(qty);
                        } else {
                            listener.OnRemoved();
                        }
                    }
                })
                .setNegativeButton("REMOVE ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cart.removeAllVariants(product);
                        listener.OnRemoved();
                    }
                }).show()
        ;
        showVariants();
    }

    private void setUpButtons(Variant variant, VariantItemBinding ib) {
        ib.addClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cart.addVariantBasedProductToCart(product, variant);
                Log.e("onClick",cart+"");
                ib.qty.setText(qty + "");

                if (qty == 1) {
                    ib.subClicked.setVisibility(View.VISIBLE);
                    ib.qty.setVisibility(View.VISIBLE);
                }
            }
        });
        ib.subClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cart.removeVariantBasedProductFromCart(product, variant);
                Log.e("onClick",cart+"");
                ib.qty.setText(qty + "");
                if (qty == 0) {
                    ib.subClicked.setVisibility(View.INVISIBLE);
                    ib.qty.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showVariants() {
        for (Variant variant : product.variants) {
            VariantItemBinding ib = VariantItemBinding.inflate(LayoutInflater.from(context), b.getRoot(), true);
            ib.variantName.setText(variant.nameAndPriceString());

            showPreviousQty(variant, ib);
            setUpButtons(variant, ib);
        }
    }

    private void showPreviousQty(Variant variant, VariantItemBinding ib) {
        int qty = cart.getVariantQuantity(variant, product);
        if (qty > 0) {
            ib.subClicked.setVisibility(View.VISIBLE);
            ib.qty.setVisibility(View.VISIBLE);

            ib.qty.setText(qty + "");
        }
    }

    public interface OnVariantPickedListener {
        void OnQtyUpdated(int qty);

        void OnRemoved();
    }
}
