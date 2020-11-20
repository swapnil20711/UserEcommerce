package com.example.userecommerce.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.userecommerce.databinding.WeightPickerDialogBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.Product;

import java.io.Serializable;

public class WeightPicker implements Serializable {

    private WeightPickerDialogBinding binding;
    private Product product;
    private Cart cart;

    public interface OnWeightSelected {
        void OnWeightPicked(int kg, int g);

        void OnRemoved();
    }


    public void show(Context context, Product product, Cart cart, OnWeightSelected listener) {
        binding = WeightPickerDialogBinding.inflate(LayoutInflater.from(context));
        this.product = product;
        this.cart = cart;
        new AlertDialog.Builder(context).
                setTitle(product.name)
                .setView(binding.getRoot())
                .setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int kg = binding.numberPickerKg.getValue();
                        int g = binding.numberPickerg.getValue() * 50;
                        if (kg == 0 && g == 0) {
                            return;
                        }
                        changeInCart(kg + (g / 1000f));
                        listener.OnWeightPicked(kg, g);
                    }
                })
                .setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cart.removeWBProductInCart(product);
                        listener.OnRemoved();
                    }
                }).show();
        setUpNumberPickers();
        setPreviousSelectedWeight();
    }

    private void setPreviousSelectedWeight() {
        if (cart.cartItemMap.containsKey(product.name)) {
            float qty = cart.cartItemMap.get(product.name).qty;
            binding.numberPickerKg.setValue((int) qty);
            float val = Math.round(((qty - (int) qty) * 1000)/ 50);
            Log.e("weightChanged", val + "");
            binding.numberPickerg.setValue((int) val);
        }
    }

    private void changeInCart(float v) {
        cart.updateWBProductInCart(product, v);
        Log.e("changeInCart", cart + "");
    }

    private void setUpNumberPickers() {
        double minQty = product.minQty * 1000;
        int weightKg = (int) (minQty / 1000);
        int weightG = (int) (minQty % 1000) / 50;
        Log.e("weightPicker", weightKg + "kg " + weightG + " g");
        binding.numberPickerKg.setMinValue(weightKg);
        binding.numberPickerKg.setMaxValue(10);
        binding.numberPickerg.setMinValue(weightG);
        binding.numberPickerg.setMaxValue(19);
        setPickerG(" g", binding);
        setPickerKg();
        binding.numberPickerKg.setValue(weightKg);
        binding.numberPickerg.setValue(weightG);
//        setUpNumberPickerListener(weightKg, weightG);
    }

    /*private void setUpNumberPickerListener(int weightKg, int weightG) {
        binding.numberPickerKg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal > weightKg) {
                    binding.numberPickerg.setMinValue(0);
                    setPickerG(" g", binding);
                } else {
                    binding.numberPickerg.setMinValue(weightG / 50);
                    binding.numberPickerg.setMaxValue(19);
                    setPickerG(" g", binding);
                }
            }
        });
    }*/

    private void setPickerKg() {
        binding.numberPickerKg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + "kg";
            }

        });
        View firstItem = binding.numberPickerKg.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

    private void setPickerG(String s, WeightPickerDialogBinding binding) {
        binding.numberPickerg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value * 50 + s;
            }

        });

        View firstItem = binding.numberPickerg.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

}
