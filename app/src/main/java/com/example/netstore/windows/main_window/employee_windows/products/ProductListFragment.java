package com.example.netstore.windows.main_window.employee_windows.products;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.netstore.adapters.ProductListAdapter;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.databinding.ListItemsWithBtnFragmentBinding;
import com.example.netstore.models.Product;
import com.example.netstore.viewModels.ProductViewModel;

import java.util.List;

public class ProductListFragment extends Fragment {
    private ListItemsWithBtnFragmentBinding binding;

    private List<Product> productList;
    private ProductListAdapter productListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListItemsWithBtnFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateList();

        binding.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new WorkWithProductFragment(), "add product")
                        .addToBackStack("products")
                        .commit();
            }
        });

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
                .setItems(new String[] {"Изменить"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container_fragment, new WorkWithProductFragment(product), "edit product")
                                        .addToBackStack("products")
                                        .commit();

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
