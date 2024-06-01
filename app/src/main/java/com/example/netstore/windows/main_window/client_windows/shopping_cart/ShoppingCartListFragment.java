package com.example.netstore.windows.main_window.client_windows.shopping_cart;

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

import com.example.netstore.adapters.ProductListAdapter;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.config.SharedPreferencesConfig;
import com.example.netstore.databinding.ListItemsFragmentBinding;
import com.example.netstore.databinding.ShoppingCartListFragmentBinding;
import com.example.netstore.models.Product;
import com.example.netstore.models.ShoppingCart;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.ProductViewModel;
import com.example.netstore.viewModels.ShoppingCartViewModel;
import com.example.netstore.windows.main_window.employee_windows.inventorying.Config;
import com.google.gson.Gson;

import java.util.List;

public class ShoppingCartListFragment extends Fragment {

    private ShoppingCartListFragmentBinding binding;

    private ShoppingCart shoppingCart;
    private ProductListAdapter productListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ShoppingCartListFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateList();

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartViewModel shoppingCartViewModel = new ShoppingCartViewModel();
                shoppingCartViewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if(observerObject.tag == "get shopping cart" && observerObject.status) {
                            shoppingCart = (ShoppingCart) observerObject.item;
                            productListAdapter = new ProductListAdapter(getContext(), shoppingCart.cartItems);
                            binding.listView.setAdapter(productListAdapter);
                        }
                    }
                });

                shoppingCartViewModel.formOrder(shoppingCart);
            }
        });
    }

    private void updateList() {

        ShoppingCartViewModel shoppingCartViewModel = new ShoppingCartViewModel();
        shoppingCartViewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
            @Override
            public void onChanged(ObserverObject observerObject) {
                if(observerObject.tag == "get shopping cart" && observerObject.status) {
                    shoppingCart = (ShoppingCart) observerObject.item;
                    productListAdapter = new ProductListAdapter(getContext(), shoppingCart.cartItems);
                    binding.listView.setAdapter(productListAdapter);
                }
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesConfig.SP_FILE_TAG, Context.MODE_PRIVATE);

        User user = new Gson().fromJson(sharedPreferences.getString(SharedPreferencesConfig.SP_USER_TAG, ""), User.class);

        shoppingCartViewModel.getShoppingCart(user);
    }
}
