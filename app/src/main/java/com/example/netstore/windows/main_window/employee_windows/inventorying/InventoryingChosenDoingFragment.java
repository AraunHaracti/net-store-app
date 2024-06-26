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

import com.example.netstore.R;
import com.example.netstore.databinding.InventoryintChosenDoingFragmentBinding;

public class InventoryingChosenDoingFragment extends Fragment {
    private InventoryintChosenDoingFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = InventoryintChosenDoingFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new ReceiveProductFragment(), "receive_product")
                        .addToBackStack("inventorying_products")
                        .commit();
            }
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new SendProductFragment(), "send_product")
                        .addToBackStack("inventorying_products")
                        .commit();
            }
        });

        binding.moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new MoveProductFragment(), "move_product")
                        .addToBackStack("inventorying_products")
                        .commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setSPNull();
    }

    private void setSPNull() {
        SharedPreferences.Editor editorSP = getActivity().getSharedPreferences(Config.SP_FILE_NAME, Context.MODE_PRIVATE).edit();

        editorSP.putString(Config.SP_TAG_NAME_RECEIVE_PRODUCT, "");
        editorSP.putString(Config.SP_TAG_NAME_RECEIVE_PLACE, "");

        editorSP.putString(Config.SP_TAG_NAME_SEND_PRODUCT, "");
        editorSP.putString(Config.SP_TAG_NAME_SEND_PLACE, "");

        editorSP.putString(Config.SP_TAG_NAME_MOVE_PRODUCT, "");
        editorSP.putString(Config.SP_TAG_NAME_MOVE_PLACE_BEGIN, "");
        editorSP.putString(Config.SP_TAG_NAME_MOVE_PLACE_END, "");

        editorSP.apply();
    }
}
