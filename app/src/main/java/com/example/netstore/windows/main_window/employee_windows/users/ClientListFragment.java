package com.example.netstore.windows.main_window.employee_windows.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.adapters.ClientListAdapter;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.databinding.ListItemsFragmentBinding;
import com.example.netstore.models.Client;
import com.example.netstore.viewModels.UserViewModel;

import java.util.List;

public class ClientListFragment extends Fragment {
    private ListItemsFragmentBinding binding;
    private List<Client> clientList;
    private ClientListAdapter clientListAdapter;

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
    }

    private void updateList() {
        UserViewModel userViewModel = new UserViewModel();
        userViewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
            @Override
            public void onChanged(ObserverObject observerObject) {
                if(observerObject.tag == "get list client" && observerObject.status) {
                    clientList = (List<Client>) observerObject.item;
                    clientListAdapter = new ClientListAdapter(getContext(), clientList);
                    binding.listView.setAdapter(clientListAdapter);
                }
            }
        });

        userViewModel.getClients();
    }
}
