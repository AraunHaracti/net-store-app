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

import java.util.List;

public class ClientListAdapter extends ArrayAdapter<Client> {
    private Context mContext;
    private List<Client> mClients;

    public ClientListAdapter(@NonNull Context context, @NonNull List<Client> clients) {
        super(context, R.layout.client_info_fragment, clients);
        mContext = context;
        mClients = clients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.client_info_fragment, parent, false);
        }

        Client currentClient = mClients.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.text_view_firstname);
        if (currentClient.name != null) {
            nameTextView.setText(currentClient.name);
        }

        TextView lastnameTextView = listItemView.findViewById(R.id.text_view_lastname);
        if (currentClient.surname != null) {
            lastnameTextView.setText(currentClient.surname);
        }

        TextView birthdayTextView = listItemView.findViewById(R.id.text_view_birthday);
        if (currentClient.birthday != null) {
            birthdayTextView.setText(currentClient.birthday.toString());
        }

        TextView emailTextView = listItemView.findViewById(R.id.text_view_email);
        if (currentClient.email != null) {
            emailTextView.setText(currentClient.email);
        }

        TextView addressTextView = listItemView.findViewById(R.id.text_view_address);
        if (currentClient.address != null) {
            addressTextView.setText(currentClient.address);
        }

        return listItemView;
    }
}
