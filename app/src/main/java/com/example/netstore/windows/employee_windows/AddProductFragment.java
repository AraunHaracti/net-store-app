package com.example.netstore.windows.employee_windows;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.config.ObserverObject;
import com.example.netstore.databinding.AddProductFragmentBinding;
import com.example.netstore.models.Product;
import com.example.netstore.viewModels.ProductViewModel;

import java.util.Objects;

public class AddProductFragment extends Fragment {
    private AddProductFragmentBinding binding;

    public AddProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = AddProductFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });

        binding.addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product currentProduct = getCurrentProduct();

                if (currentProduct == null)
                    return;

                ProductViewModel viewModel = new ProductViewModel();

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if (Objects.equals(observerObject.tag, "add product") && observerObject.status) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Add failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                viewModel.addProduct(currentProduct);
            }
        });
    }

    @Nullable
    private Product getCurrentProduct() {
        String name = binding.textEditName.getText().toString();
        String description = binding.textEditDescription.getText().toString();

        String priceString = binding.textEditPrice.getText().toString();

        Uri photoUri = this.photoUri;

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Description is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (priceString.isEmpty()) {
            Toast.makeText(getContext(), "Price is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (photoUri == null) {
            Toast.makeText(getContext(), "Photo is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        Product currentProduct = new Product(name, description, photoUri, Double.parseDouble(priceString), 0);
        return currentProduct;
    }

    private Uri photoUri;

    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            photoUri = data.getData();
            binding.imageViewProduct.setImageURI(photoUri);
        }
    }
}