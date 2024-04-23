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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.R;
import com.example.netstore.adapters.PlaceListAdapter;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.databinding.ListItemsWithBtnFragmentBinding;
import com.example.netstore.models.Place;
import com.example.netstore.viewModels.PlaceViewModel;
import com.example.netstore.windows.main_window.employee_windows.places.WorkWithPlaceFragment;
import com.google.gson.Gson;

import java.util.List;

public class InventoryingPlaceListFragment extends Fragment {
    private ListItemsWithBtnFragmentBinding binding;

    private List<Place> placeList;
    private PlaceListAdapter placeListAdapter;
    private String configTag;

    public InventoryingPlaceListFragment(List<Place> placeList, String configTag) {
        this.placeList = placeList;
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
                        .replace(R.id.container_fragment, new WorkWithPlaceFragment(), "add place")
                        .addToBackStack("places")
                        .commit();
            }
        });

        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDoingsDialog((Place) parent.getItemAtPosition(position));
                return false;
            }
        });
    }

    public void showDoingsDialog(Place place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите действие для места: " + place.name)
                .setItems(new String[] {"Выбрать", "Изменить", "Удалить"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                SharedPreferences.Editor spEditor = getContext().getSharedPreferences(Config.SP_FILE_NAME, Context.MODE_PRIVATE).edit();
                                spEditor.putString(configTag, new Gson().toJson(place));
                                spEditor.apply();

                                getParentFragmentManager().popBackStack();

                                break;
                            case 1:

                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container_fragment, new WorkWithPlaceFragment(place), "edit place")
                                        .addToBackStack("places")
                                        .commit();

                                break;
                            case 2:

                                if(!(place.products == null || place.products.isEmpty())) {
                                    Toast.makeText(getContext(), "Не удалось удалить, место занято", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                                PlaceViewModel viewModel = new PlaceViewModel();
                                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                                    @Override
                                    public void onChanged(ObserverObject observerObject) {
                                        if (observerObject.tag == "delete place" && observerObject.status) {
                                            Toast.makeText(getContext(), ("Место \"" + place.name + "\" удалено"), Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getContext(), ("Не удалось удалить"), Toast.LENGTH_SHORT).show();
                                        }

                                        updateList();
                                    }
                                });

                                viewModel.deletePlace(place);

                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateList() {
        placeListAdapter = new PlaceListAdapter(getContext(), placeList);
        binding.listView.setAdapter(placeListAdapter);
    }
}
