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
import com.example.netstore.models.Order;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderListAdapter extends ArrayAdapter<Order> {
    private Context context;
    private List<Order> orderList;

    public OrderListAdapter(@NonNull Context context, @NonNull List<Order> orderList) {
        super(context, R.layout.order_list_item_fragment, orderList);
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.order_list_item_fragment, parent, false);
        }

        Order currentOrder = orderList.get(position);

        TextView dateCreateTextView = listItemView.findViewById(R.id.text_view_date_create);
        TextView statusTextView = listItemView.findViewById(R.id.text_view_status);
        TextView totalPayableTextView = listItemView.findViewById(R.id.text_view_total_payable);

        dateCreateTextView.setText(currentOrder.dateCreate != null ? new SimpleDateFormat("dd MMM yyyy г.").format(currentOrder.dateCreate) : "");
        statusTextView.setText(currentOrder.status != null ? currentOrder.status : "");
        totalPayableTextView.setText(String.format("%.2f", currentOrder.totalPayable) + " руб.");

        return listItemView;
    }
}
