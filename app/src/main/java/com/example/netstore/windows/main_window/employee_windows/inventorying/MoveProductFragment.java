package com.example.netstore.windows.main_window.employee_windows.inventorying;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.R;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.databinding.MoveProductFragmentBinding;
import com.example.netstore.models.Place;
import com.example.netstore.models.Product;
import com.example.netstore.viewModels.InventoryingViewModel;
import com.example.netstore.viewModels.PlaceViewModel;
import com.example.netstore.viewModels.ProductViewModel;
import com.google.gson.Gson;

import java.util.List;

public class MoveProductFragment extends Fragment {
    private MoveProductFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MoveProductFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textEditPlaceBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlaceViewModel viewModel = new PlaceViewModel();

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if (observerObject.tag == "get list place" && observerObject.status) {
                            List<Place> placeList = (List<Place>) observerObject.item;

                            InventoryingPlaceListFragment fragment = new InventoryingPlaceListFragment(placeList, Config.SP_TAG_NAME_MOVE_PLACE_BEGIN);

                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container_fragment, fragment, "inventorying_place")
                                    .addToBackStack("move_product")
                                    .commit();
                        }
                    }
                });

                viewModel.getPlaces();
            }
        });

        binding.textEditPlaceEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlaceViewModel viewModel = new PlaceViewModel();

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if (observerObject.tag == "get list place" && observerObject.status) {
                            List<Place> placeList = (List<Place>) observerObject.item;

                            InventoryingPlaceListFragment fragment = new InventoryingPlaceListFragment(placeList, Config.SP_TAG_NAME_MOVE_PLACE_END);

                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container_fragment, fragment, "inventorying_place")
                                    .addToBackStack("move_product")
                                    .commit();
                        }
                    }
                });

                viewModel.getPlaces();
            }
        });

        binding.textEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductViewModel viewModel = new ProductViewModel();

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if (observerObject.tag == "get list product" && observerObject.status) {
                            List<Product> productList = (List<Product>) observerObject.item;

                            InventoryingProductListFragment fragment = new InventoryingProductListFragment(productList, Config.SP_TAG_NAME_MOVE_PRODUCT);

                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container_fragment, fragment, "inventorying_product")
                                    .addToBackStack("move_product")
                                    .commit();
                        }
                    }
                });

                viewModel.getProducts();
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(binding.textEditCount.getText().toString());

                if (currentPlaceBegin != null && currentPlaceEnd != null && currentProduct != null && count >= 0) {
                    InventoryingViewModel viewModel = new InventoryingViewModel();

                    viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                        @Override
                        public void onChanged(ObserverObject observerObject) {
                            if (observerObject.tag == "send" && observerObject.status) {
                                getParentFragmentManager().popBackStack();
                            }
                        }
                    });

                    viewModel.moveInventorying(currentPlaceBegin, currentPlaceEnd, currentProduct, count);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setCurrentPositionsItems();
    }

    private Place currentPlaceBegin;
    private Place currentPlaceEnd;
    private Product currentProduct;
    private int count;

    private void setCurrentPositionsItems() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SP_FILE_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        currentPlaceBegin = gson.fromJson(preferences.getString(Config.SP_TAG_NAME_MOVE_PLACE_BEGIN, ""), Place.class);
        currentPlaceEnd = gson.fromJson(preferences.getString(Config.SP_TAG_NAME_MOVE_PLACE_END, ""), Place.class);
        currentProduct = gson.fromJson(preferences.getString(Config.SP_TAG_NAME_MOVE_PRODUCT, ""), Product.class);

        binding.textEditPlaceBegin.setText(currentPlaceBegin == null ? "" : currentPlaceBegin.name);
        binding.textEditPlaceEnd.setText(currentPlaceEnd == null ? "" : currentPlaceEnd.name);
        binding.textEditProduct.setText(currentProduct == null ? "" : currentProduct.name);
    }
}
