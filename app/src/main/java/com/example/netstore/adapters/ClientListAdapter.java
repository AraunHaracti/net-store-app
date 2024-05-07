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
import com.example.netstore.models.Client;

import java.text.SimpleDateFormat;
import java.util.List;

public class ClientListAdapter extends ArrayAdapter<Client> {
    private Context context;
    private List<Client> clientList;

    public ClientListAdapter(@NonNull Context context, @NonNull List<Client> clientList) {
        super(context, R.layout.client_list_item_fragment, clientList);
        this.context = context;
        this.clientList = clientList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(context).inflate(R.layout.client_list_item_fragment, parent, false);

        Client currentClient = clientList.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.text_view_firstname);
        TextView lastnameTextView = listItemView.findViewById(R.id.text_view_lastname);
        TextView birthdayTextView = listItemView.findViewById(R.id.text_view_birthday);
        TextView emailTextView = listItemView.findViewById(R.id.text_view_email);
        TextView addressTextView = listItemView.findViewById(R.id.text_view_address);

        nameTextView.setText(currentClient.name != null ? currentClient.name : "");
        lastnameTextView.setText(currentClient.surname != null ? currentClient.surname : "");
        birthdayTextView.setText(currentClient.birthday != null ? new SimpleDateFormat("dd MMM yyyy г.").format(currentClient.birthday) : "");
        emailTextView.setText(currentClient.email != null ? currentClient.email : "");
        addressTextView.setText(currentClient.address != null ? currentClient.address : "");

        return listItemView;
    }
}
