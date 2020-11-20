package com.example.userecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userecommerce.controllers.MultipleVBandProductWBViewBinder;
import com.example.userecommerce.controllers.SingleVBProductViewBinder;
import com.example.userecommerce.databinding.ProductItemSingleVbBinding;
import com.example.userecommerce.databinding.ProductItemWbMultiVbBinding;
import com.example.userecommerce.models.Cart;
import com.example.userecommerce.models.Product;

import java.io.Serializable;
import java.util.List;

//Adapter for list of products
public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //Needed for inflating layout
    private Context context;

    //List of data
    private List<Product> productList;
    private Cart cart;
    private final int TYPE_MULTIPLE_VB_OR_WB = 1, TYPE_SINGLE_VB = 0;

    public ProductsAdapter(Context context, List<Product> productList, Cart cart) {
        this.context = context;
        this.productList = productList;
        this.cart = cart;
    }

    //Inflate the view for item and create a ViewHolder object and return
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SINGLE_VB) {
            ProductItemSingleVbBinding binding = ProductItemSingleVbBinding.inflate(
                    LayoutInflater.from(context)
                    , parent
                    , false
            );
            //Create ViewHolder object and return
            return new SingleVBVH(binding);
        } else {
            ProductItemWbMultiVbBinding binding = ProductItemWbMultiVbBinding.inflate(LayoutInflater.from(context), parent, false);
            return new MultipleVBWBVH(binding);
        }

    }

    //Binds the data to view
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productList.get(position);
        if (getItemViewType(position) == TYPE_SINGLE_VB) {
            SingleVBVH viewHolder = (SingleVBVH) holder;
            viewHolder.binding.name.setText(product.name + " " + product.variants.get(0).name);
            viewHolder.binding.price.setText(product.priceString());


            new SingleVBProductViewBinder().bind(viewHolder.binding, product, cart);
        } else {
            MultipleVBWBVH viewHolder = (MultipleVBWBVH) holder;
            viewHolder.binding.name.setText(product.name);
            viewHolder.binding.price.setText(product.priceString());
            new MultipleVBandProductWBViewBinder().bind(viewHolder.binding, product, cart);

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Product product = productList.get(position);
        if (product.type == TYPE_SINGLE_VB || product.variants.size() > 1) {
            return TYPE_MULTIPLE_VB_OR_WB;
        }
        return TYPE_SINGLE_VB;
    }

    //Holds the view for each item
    public static class SingleVBVH extends RecyclerView.ViewHolder {
        ProductItemSingleVbBinding binding;

        public SingleVBVH(@NonNull ProductItemSingleVbBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class MultipleVBWBVH extends RecyclerView.ViewHolder {

        ProductItemWbMultiVbBinding binding;

        public MultipleVBWBVH(@NonNull ProductItemWbMultiVbBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
