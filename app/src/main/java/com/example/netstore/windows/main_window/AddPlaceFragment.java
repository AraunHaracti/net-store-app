package com.example.netstore.windows.main_window;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.ObserverObject;
import com.example.netstore.databinding.AddPlaceFragmentBinding;
import com.example.netstore.models.Place;
import com.example.netstore.viewModels.PlaceViewModel;

import java.util.Objects;

public class AddPlaceFragment extends Fragment {
    private AddPlaceFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = AddPlaceFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place currentPlace = getCurrentPlace();

                if (currentPlace == null)
                    return;

                PlaceViewModel viewModel = new PlaceViewModel();

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if (Objects.equals(observerObject.tag, "add place") && observerObject.status) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Add failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                viewModel.addPlace(currentPlace);
            }
        });
    }

    @Nullable
    private Place getCurrentPlace() {
        String name = binding.textEditName.getText().toString();
        String description = binding.textEditDescription.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Description is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        Place currentPlace = new Place(name, description);
        return currentPlace;
    }
}
