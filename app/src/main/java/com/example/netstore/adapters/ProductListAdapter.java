package com.example.netstore.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.netstore.R;
import com.example.netstore.models.Product;

import java.util.List;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> productList;

    public ProductListAdapter(@NonNull Context context, @NonNull List<Product> productList) {
        super(context, R.layout.product_list_item_fragment, productList);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.product_list_item_fragment, parent, false);
        }

        Product currentProduct = productList.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.text_view_name);
        TextView descriptionTextView = listItemView.findViewById(R.id.text_view_description);
        ImageView photoImageView = listItemView.findViewById(R.id.image_view_photo); // TODO
        TextView priceTextView = listItemView.findViewById(R.id.text_view_price);
        TextView countTextView = listItemView.findViewById(R.id.text_view_count);

        nameTextView.setText(currentProduct.name != null ? currentProduct.name : "");
        descriptionTextView.setText(currentProduct.description != null ? currentProduct.description : "");
        priceTextView.setText(String.format("%.2f", currentProduct.price) + " руб.");
        countTextView.setText("В наличии: " + currentProduct.count);

        if (currentProduct.photoPath != null) {
            Glide.with(context).load(Uri.parse(currentProduct.photoPath)).into(photoImageView);
        } else {
            photoImageView.setImageResource(R.drawable.placeholder_image);
        }

        return listItemView;
    }
}
