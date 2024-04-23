package com.example.netstore.windows.main_window.employee_windows.inventorying;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netstore.R;
import com.example.netstore.adapters.ProductListAdapter;
import com.example.netstore.databinding.ListItemsWithBtnFragmentBinding;
import com.example.netstore.models.Product;
import com.example.netstore.windows.main_window.employee_windows.products.WorkWithProductFragment;
import com.google.gson.Gson;

import java.util.List;

public class InventoryingProductListFragment extends Fragment {
    private ListItemsWithBtnFragmentBinding binding;

    private List<Product> productList;
    private ProductListAdapter productListAdapter;
    private String configTag;

    public InventoryingProductListFragment(List<Product> productList, String configTag) {
        this.productList = productList;
        this.configTag = configTag;
    }

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
                .setItems(new String[] {"Выбрать", "Изменить"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                SharedPreferences.Editor spEditor = getContext().getSharedPreferences(Config.SP_FILE_NAME, Context.MODE_PRIVATE).edit();
                                spEditor.putString(configTag, new Gson().toJson(product));
                                spEditor.apply();

                                getParentFragmentManager().popBackStack();

                                break;
                            case 1:

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
        productListAdapter = new ProductListAdapter(getContext(), productList);
        binding.listView.setAdapter(productListAdapter);
    }
}
