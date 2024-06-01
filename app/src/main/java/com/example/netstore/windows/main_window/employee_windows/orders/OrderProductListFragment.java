package com.example.netstore.windows.main_window.employee_windows.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netstore.adapters.ProductListAdapter;
import com.example.netstore.databinding.ListItemsFragmentBinding;
import com.example.netstore.models.Order;

public class OrderProductListFragment extends Fragment {
    private ListItemsFragmentBinding binding;
    private Order order;
    private ProductListAdapter productListAdapter;

    public OrderProductListFragment(Order order) {
        this.order = order;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListItemsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateList();
    }

    private void updateList() {
        productListAdapter = new ProductListAdapter(getContext(), order.productOrderList);
        binding.listView.setAdapter(productListAdapter);
    }
}
