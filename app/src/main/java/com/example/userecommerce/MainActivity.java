package com.example.userecommerce;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userecommerce.databinding.WeightPickerDialogBinding;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeightPickerDialogBinding binding = WeightPickerDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float minQty = Float.parseFloat(binding.floatingNumber.getText().toString()) * 1000;
                int weightKg = (int) (minQty / 1000);
                int weightG = (int) (minQty % 1000);
                binding.numberPickerKg.setMinValue(weightKg);
                binding.numberPickerKg.setMaxValue(15);
                binding.numberPickerg.setMinValue(weightG / 50);
                binding.numberPickerg.setMaxValue(19);
                setPickerG(" g", binding);
                setPickerKg();
                binding.numberPickerKg.setValue(weightKg);
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
            }

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
        });

    }
}