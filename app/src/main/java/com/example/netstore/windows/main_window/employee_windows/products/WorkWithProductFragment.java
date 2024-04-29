package com.example.netstore.windows.main_window.employee_windows.products;

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

import com.bumptech.glide.Glide;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.config.WorkWithItemMode;
import com.example.netstore.databinding.WorkWithProductFragmentBinding;
import com.example.netstore.models.Product;
import com.example.netstore.viewModels.ProductViewModel;

import java.io.File;
import java.util.Objects;

public class WorkWithProductFragment extends Fragment {
    private WorkWithProductFragmentBinding binding;
    private Product currentProduct;

    private WorkWithItemMode doMode;

    public WorkWithProductFragment() {
        doMode = WorkWithItemMode.Add;
    }

    public WorkWithProductFragment(Product product) {
        this.currentProduct = product;
        doMode = WorkWithItemMode.Edit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = WorkWithProductFragmentBinding.inflate(inflater, container, false);

        if (currentProduct != null) {
            binding.textEditName.setText(currentProduct.name);
            binding.textEditDescription.setText(currentProduct.description);
            binding.textEditPrice.setText(currentProduct.price.toString());

            Glide.with(getContext()).load(Uri.parse(currentProduct.photoPath)).into(binding.imageViewProduct);

            photoUri = Uri.parse(currentProduct.photoPath);
        }

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

        binding.saveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!formCurrentProduct())
                    return;

                ProductViewModel viewModel = new ProductViewModel();

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if ((Objects.equals(observerObject.tag, "add product") || observerObject.tag == "edit product") && observerObject.status) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Save failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                switch (doMode) {
                    case Add:
                        viewModel.addProduct(currentProduct);
                        break;
                    case Edit:
                        viewModel.editProduct(currentProduct);
                        break;
                }
            }
        });
    }

    @Nullable
    private boolean formCurrentProduct() {
        String name = binding.textEditName.getText().toString();
        String description = binding.textEditDescription.getText().toString();

        String priceString = binding.textEditPrice.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Description is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (priceString.isEmpty()) {
            Toast.makeText(getContext(), "Price is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (photoUri == null) {
            Toast.makeText(getContext(), "Photo is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (this.currentProduct == null)
            this.currentProduct = new Product(name, description, photoUri.toString(), Double.parseDouble(priceString), 0);
        else
            this.currentProduct = new Product(currentProduct._id, name, description, photoUri.toString(), currentProduct.price, currentProduct.count);

        return true;
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