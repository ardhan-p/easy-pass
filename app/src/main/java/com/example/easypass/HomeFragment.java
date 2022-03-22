package com.example.easypass;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easypass.database.AppDatabase;
import com.example.easypass.database.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FloatingActionButton addLoginBtn;
    private ArrayList<Login> loginsArrayList;
    private RecyclerView loginsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        loginsRecyclerView = homeView.findViewById(R.id.home_password_list);
        loginsArrayList = new ArrayList<Login>();

        LoginObjectListAdapter adapter = new LoginObjectListAdapter(loginsArrayList);
        loginsRecyclerView.setAdapter(adapter);
        loginsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        addLoginBtn = (FloatingActionButton) homeView.findViewById(R.id.add_login_fab);

        addLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createLoginActivityIntent = new Intent(getActivity(), CreateLoginActivity.class);
                startActivity(createLoginActivityIntent);
            }
        });

        return homeView;
    }
}