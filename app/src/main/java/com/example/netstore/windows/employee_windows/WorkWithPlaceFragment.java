package com.example.netstore.windows.employee_windows;

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
import com.example.netstore.config.WorkWithItemMode;
import com.example.netstore.databinding.WorkWithPlaceFragmentBinding;
import com.example.netstore.models.Place;
import com.example.netstore.viewModels.PlaceViewModel;

public class WorkWithPlaceFragment extends Fragment {
    private WorkWithPlaceFragmentBinding binding;
    private Place currentPlace = new Place();

    private WorkWithItemMode doMode;

    public WorkWithPlaceFragment() {
        doMode = WorkWithItemMode.Add;
    }

    public WorkWithPlaceFragment(Place place) {
        this.currentPlace = place;
        doMode = WorkWithItemMode.Edit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = WorkWithPlaceFragmentBinding.inflate(inflater, container, false);

        if (currentPlace != null) {
            binding.textEditName.setText(currentPlace.name);
            binding.textEditDescription.setText(currentPlace.description);
        }


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.savePlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                formCurrentPlace();

                if (currentPlace == null)
                    return;

                PlaceViewModel viewModel = new PlaceViewModel();

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if ((observerObject.tag == "add place" || observerObject.tag == "edit place") && observerObject.status) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Save failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                switch (doMode) {
                    case Add:
                        viewModel.addPlace(currentPlace);
                        break;
                    case Edit:
                        viewModel.editPlace(currentPlace);
                        break;
                }
            }
        });
    }

    @Nullable
    private void formCurrentPlace() {
        String name = binding.textEditName.getText().toString();
        String description = binding.textEditDescription.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Description is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        this.currentPlace.name = name;
        this.currentPlace.description = description;
    }
}
