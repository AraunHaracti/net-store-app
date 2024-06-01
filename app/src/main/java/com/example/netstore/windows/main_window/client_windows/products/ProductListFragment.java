package com.example.netstore.windows.main_window.client_windows.products;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.adapters.ProductListAdapter;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.config.SharedPreferencesConfig;
import com.example.netstore.databinding.ListItemsFragmentBinding;
import com.example.netstore.models.Product;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.ProductViewModel;
import com.example.netstore.viewModels.ShoppingCartViewModel;
import com.google.gson.Gson;

import java.util.List;

public class ProductListFragment extends Fragment {
    private ListItemsFragmentBinding binding;
    private List<Product> productList;
    private ProductListAdapter productListAdapter;

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

        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDoingsDialog((Product) parent.getItemAtPosition(position));
                return false;
            }
        });
    }

    public void showDoingsDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите действие для места: " + product.name)
                .setItems(new String[] {"Добавить в корзину"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                SharedPreferences preferences = getActivity().getSharedPreferences(SharedPreferencesConfig.SP_FILE_TAG, Context.MODE_PRIVATE);
                                User currentUser = new Gson().fromJson(preferences.getString(SharedPreferencesConfig.SP_USER_TAG, ""), User.class);

                                ShoppingCartViewModel viewModel = new ShoppingCartViewModel();

                                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                                    @Override
                                    public void onChanged(ObserverObject observerObject) {
                                        if (observerObject.tag == "add product cart" && observerObject.status) {
                                            Toast.makeText(getContext(), "Добавление товара в корзину", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Ошибка при добавлении", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                viewModel.addProductInShoppingCart(currentUser, product);

                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateList() {

        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
            @Override
            public void onChanged(ObserverObject observerObject) {
                if(observerObject.tag == "get list product" && observerObject.status) {
                    productList = (List<Product>) observerObject.item;
                    productListAdapter = new ProductListAdapter(getContext(), productList);
                    binding.listView.setAdapter(productListAdapter);
                }
            }
        });
        productViewModel.getProducts();
    }
}
