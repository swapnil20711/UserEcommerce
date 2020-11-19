package com.example.userecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.userecommerce.databinding.ActivityLinearLayoutDemoBinding;
import com.example.userecommerce.databinding.VariantItemBinding;
import com.example.userecommerce.models.Variant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinearLayoutDemoActivity extends AppCompatActivity {

    private ActivityLinearLayoutDemoBinding b;
    private VariantItemBinding ib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLinearLayoutDemoBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        showVariants();

    }

    private void showVariants() {
        List<Variant> variants = Arrays.asList(new Variant("apple", 2),
                new Variant("hello", 12)
        );
        for (Variant variant : variants) {
            ib = VariantItemBinding.inflate(getLayoutInflater());
            ib.qty.setText(variant.qty+"");
            ib.addClicked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ib.qty.setText((variant.qty++)+"");
                }
            });
            ib.variantName.setText(variant.name);
            b.list.addView(ib.getRoot());
        }
    }
}