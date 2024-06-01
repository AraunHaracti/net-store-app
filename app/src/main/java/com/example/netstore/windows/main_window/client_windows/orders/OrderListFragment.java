package com.example.netstore.windows.main_window.client_windows.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.R;
import com.example.netstore.adapters.OrderListAdapter;
import com.example.netstore.adapters.ProductListAdapter;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.config.SharedPreferencesConfig;
import com.example.netstore.databinding.ListItemsFragmentBinding;
import com.example.netstore.databinding.ShoppingCartListFragmentBinding;
import com.example.netstore.models.Order;
import com.example.netstore.models.ShoppingCart;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.OrderViewModel;
import com.example.netstore.viewModels.ShoppingCartViewModel;
import com.example.netstore.windows.main_window.client_windows.account.ClientAccountFragment;
import com.google.gson.Gson;

import java.util.List;

public class OrderListFragment extends Fragment {
    private ListItemsFragmentBinding binding;
    private List<Order> orderList;
    private OrderListAdapter orderListAdapter;

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

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new OrderProductListFragment(orderList.get((int)id)), "order_list_product")
                        .addToBackStack("order_list")
                        .commit();
            }
        });
    }

    private void updateList() {

        OrderViewModel orderViewModel = new OrderViewModel();
        orderViewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
            @Override
            public void onChanged(ObserverObject observerObject) {
                if(observerObject.tag == "get orders" && observerObject.status) {
                    orderList = (List<Order>) observerObject.item;
                    orderListAdapter = new OrderListAdapter(getContext(), orderList);
                    binding.listView.setAdapter(orderListAdapter);
                }
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesConfig.SP_FILE_TAG, Context.MODE_PRIVATE);

        User user = new Gson().fromJson(sharedPreferences.getString(SharedPreferencesConfig.SP_USER_TAG, ""), User.class);

        orderViewModel.getOrders(user);
    }
}
