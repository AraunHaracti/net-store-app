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
    private Context context;
    private List<Place> placeList;

    public PlaceListAdapter(@NonNull Context context, @NonNull List<Place> placeList) {
        super(context, R.layout.place_list_item_fragment, placeList);
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.place_list_item_fragment, parent, false);
        }

        Place currentPlace = placeList.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.text_view_name);
        TextView descriptionTextView = listItemView.findViewById(R.id.text_view_description);

        nameTextView.setText(currentPlace.name != null ? currentPlace.name : "");
        descriptionTextView.setText(currentPlace.description != null ? currentPlace.description : "");

        return listItemView;
    }
}
