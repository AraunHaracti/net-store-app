package com.example.netstore.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.netstore.R;
import com.example.netstore.models.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private List<Product> mProducts;

    public ProductListAdapter(@NonNull Context context, @NonNull List<Product> products) {
        super(context, R.layout.product_list_item_fragment, products);
        mContext = context;
        mProducts = products;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.product_list_item_fragment, parent, false);
        }

        Product currentProduct = mProducts.get(position);

        // Название товара
        TextView nameTextView = listItemView.findViewById(R.id.text_view_name);
        nameTextView.setText(currentProduct.name);

        // Описание товара
        TextView descriptionTextView = listItemView.findViewById(R.id.text_view_description);
        descriptionTextView.setText(currentProduct.description);

        // Фотография товара
        ImageView photoImageView = listItemView.findViewById(R.id.image_view_photo); // TODO
        if (currentProduct.photoPath != null) {

            File file = null;
            try {
                file = File.createTempFile(currentProduct.photoPath, "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            FirebaseStorage fStorage = FirebaseStorage.getInstance();
            fStorage.getReference().child(currentProduct.photoPath).getFile(file);

            photoImageView.setImageURI(Uri.fromFile(new File(currentProduct.photoPath, "jpg")));
        } else {
            photoImageView.setImageResource(R.drawable.placeholder_image);
        }

        // Цена товара
        TextView priceTextView = listItemView.findViewById(R.id.text_view_price);
        priceTextView.setText(String.format("%.2f", currentProduct.price) + " руб.");

        // Количество товара
        TextView countTextView = listItemView.findViewById(R.id.text_view_count);
        countTextView.setText("В наличии: " + currentProduct.count);

        // Изменение цвета элемента списка, если товара нет
        if (currentProduct.count == 0) {
            listItemView.setBackgroundColor(Color.LTGRAY);
        } else {
            listItemView.setBackgroundColor(Color.WHITE);
        }

        return listItemView;
    }
}
