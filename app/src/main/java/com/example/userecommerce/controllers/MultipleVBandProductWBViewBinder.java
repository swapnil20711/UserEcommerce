package com.example.userecommerce.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.userecommerce.MainActivity;
import com.example.userecommerce.databinding.ProductItemWbMultiVbBinding;
import com.example.userecommerce.dialog.VariantPickerDialog;
import com.example.userecommerce.dialog.WeightPicker;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.Product;

public class MultipleVBandProductWBViewBinder {
    private ProductItemWbMultiVbBinding b;
    private Product product;
    private Cart cart;

    public void bind(ProductItemWbMultiVbBinding b, Product product, Cart cart) {
        this.b = b;
        this.product = product;
        this.cart = cart;


        b.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        b.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.type == product.WEIGHT_BASED) {
                    showWeightBasedDialog();
                } else {
                    showVariantPickerDialog();

                }
            }
        });
    }

    private void showEditDialog() {
        if (product.type==product.WEIGHT_BASED){
            showWeightBasedDialog();
        }
        else {
            showVariantPickerDialog();
        }
    }

    private void showVariantPickerDialog() {
        Context context=b.getRoot().getContext();
        new VariantPickerDialog().show(context, cart, product, new VariantPickerDialog.OnVariantPickedListener() {
            @Override
            public void OnQtyUpdated(int qty) {
                updateQtyString(qty+"");
            }

            @Override
            public void OnRemoved() {
                hideQty();
            }
        });
    }

    private void showWeightBasedDialog() {
        Context context = b.getRoot().getContext();
        new WeightPicker().show(context, product, cart, new WeightPicker.OnWeightSelected() {
            @Override
            public void OnWeightPicked(int kg, int g) {
                updateQtyString(kg + "kg " + g + "g");
            }

            @Override
            public void OnRemoved() {
                hideQty();
            }
        });
    }

    private void hideQty() {
        b.qtyGroup.setVisibility(View.GONE);
        b.addBtn.setVisibility(View.VISIBLE);

        updateCheckoutSummary();
    }

    private void updateQtyString(String s) {
        b.addBtn.setVisibility(View.GONE);
        b.editBtn.setVisibility(View.VISIBLE);
        b.quantity.setText(s);
        updateCheckoutSummary();
    }

    private void updateCheckoutSummary() {
        Context context = b.getRoot().getContext();
        if (context instanceof MainActivity) {
            ((MainActivity) context).updateCartSummary(cart);
        } else {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}
