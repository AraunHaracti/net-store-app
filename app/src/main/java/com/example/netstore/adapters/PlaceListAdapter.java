package com.example.netstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.netstore.R;
import com.example.netstore.models.Place;

import java.util.List;

public class PlaceListAdapter extends ArrayAdapter<Place> {
    private Context mContext;
    private List<Place> mPlaces;

    public PlaceListAdapter(@NonNull Context context, @NonNull List<Place> places) {
        super(context, R.layout.place_list_item_fragment, places);
        mContext = context;
        mPlaces = places;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.place_list_item_fragment, parent, false);
        }

        Place currentPlace = mPlaces.get(position);

        // Название товара
        TextView nameTextView = listItemView.findViewById(R.id.text_view_name);
        nameTextView.setText(currentPlace.name);

        // Описание товара
        TextView descriptionTextView = listItemView.findViewById(R.id.text_view_description);
        descriptionTextView.setText(currentPlace.description);

        return listItemView;
    }
}
