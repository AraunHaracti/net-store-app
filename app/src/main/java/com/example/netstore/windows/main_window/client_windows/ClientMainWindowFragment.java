package com.example.netstore.windows.main_window.client_windows;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netstore.MainActivity;
import com.example.netstore.R;
import com.example.netstore.databinding.ClientMainWindowFragmentBinding;
import com.example.netstore.viewModels.UserViewModel;
import com.example.netstore.windows.main_window.client_windows.account.ClientAccountFragment;
import com.example.netstore.windows.main_window.client_windows.orders.OrderListFragment;
import com.example.netstore.windows.main_window.client_windows.products.ProductListFragment;
import com.example.netstore.windows.main_window.client_windows.shopping_cart.ShoppingCartListFragment;

public class ClientMainWindowFragment extends Fragment {
    private ClientMainWindowFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ClientMainWindowFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new ClientAccountFragment(), "client_account")
                        .addToBackStack("client_main_window")
                        .commit();
            }
        });

        binding.productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new ProductListFragment(), "product_list")
                        .addToBackStack("client_main_window")
                        .commit();
            }
        });

        binding.shoppingCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new ShoppingCartListFragment(), "shopping_cart_list")
                        .addToBackStack("client_main_window")
                        .commit();
            }
        });

        binding.ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new OrderListFragment(), "order_list")
                        .addToBackStack("client_main_window")
                        .commit();
            }
        });

        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserViewModel viewModel = new UserViewModel();
                viewModel.saveCurrentUser(getContext(), null);

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
