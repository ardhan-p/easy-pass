package com.example.easypass.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.easypass.R;
import com.example.easypass.masterpassword.EditMasterPasswordActivity;

public class SettingsFragment extends Fragment {
    private Button changePasswordBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        changePasswordBtn = settingsView.findViewById(R.id.changeMasterPasswordBtn);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePasswordActivityIntent = new Intent(getActivity(), EditMasterPasswordActivity.class);
                startActivity(changePasswordActivityIntent);
            }
        });

        return settingsView;
    }
}