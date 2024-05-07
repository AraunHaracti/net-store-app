package com.example.netstore.windows.main_window.client_windows.account;

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
import com.example.netstore.config.SharedPreferencesConfig;
import com.example.netstore.databinding.ClientAccountFragmentBinding;
import com.example.netstore.models.Client;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.UserViewModel;
import com.example.netstore.windows.main_window.employee_windows.account.WorkWithUserFragment;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

public class ClientAccountFragment extends Fragment {
    private ClientAccountFragmentBinding binding;
    private Client currentClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ClientAccountFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserViewModel viewModel = new UserViewModel();

        viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
            @Override
            public void onChanged(ObserverObject observerObject) {
                if (observerObject.tag == "get client" && observerObject.status == true) {
                    currentClient = ((Client) observerObject.item);
                    setValues(currentClient);
                }
                else {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesConfig.SP_FILE_TAG, Context.MODE_PRIVATE);
        User user = new Gson().fromJson(sharedPreferences.getString(SharedPreferencesConfig.SP_USER_TAG, ""), User.class);
        viewModel.getClient(user);

        binding.editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new WorkWithUserFragment(currentClient), "user_item")
                        .addToBackStack("account_employee")
                        .commit();
            }
        });
    }

    private void setValues(Client client) {
        binding.textViewFirstname.setText(client.name != null ? client.name : "");
        binding.textViewLastname.setText(client.surname != null ? client.surname : "");
        binding.textViewBirthday.setText(client.birthday != null ? new SimpleDateFormat("dd MMM yyyy г.").format(client.birthday) : "");
        binding.textViewEmail.setText(client.email != null ? client.email : "");
        binding.textViewAddress.setText(client.address != null ? client.address : "");
    }
}
