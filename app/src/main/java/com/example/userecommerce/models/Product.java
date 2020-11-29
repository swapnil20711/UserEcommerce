package com.example.userecommerce.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {

    //To check the type of product 
    public static final byte WEIGHT_BASED = 0;
    public static final byte VARIANT_BASED = 1;
    //Weight based product vars
    public int price;
    public String name;
    public int qty;
    public float minQty;
    //Type to store WEIGHT_BASED & PRODUCT_BASED
    public int type;
    public List<Variant> variants;


    public Product(){

    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", minQty='" + minQty + '\'' +
                '}';
    }

    public void fromVariantStrings(String[] vs) {
        variants = new ArrayList<>();
        for (String s : vs) {
            String[] v = s.split(",");
            variants.add(new Variant(v[0], Integer.parseInt(v[1])));
            Log.e("main", variants.toString());
        }
    }

    public String variantsString() {
        String variantsString = variants.toString();
        return variantsString
                .replaceFirst("\\[", "")
                .replaceFirst("]", "")
                .replaceAll(",", ", ");
    }

    public String minQtyToString() {
        //float (2.0) -> String (2kg)
        //float (0.050) -> String (50g)

        if (minQty < 1) {
            int g = (int) (minQty * 1000);
            return g + "g";
        }

        return ((int) minQty) + "kg";
    }

    public String priceString() {
        if (type == WEIGHT_BASED) {
            return "Rs." + price + "/kg";
        }
        return variantsString();
    }

}
