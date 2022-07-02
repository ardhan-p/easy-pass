package com.example.easypass.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easypass.login.CreateLoginActivity;
import com.example.easypass.login.LoginObjectListAdapter;
import com.example.easypass.R;
import com.example.easypass.database.AppDatabase;
import com.example.easypass.database.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FloatingActionButton addLoginBtn;
    private ArrayList<Login> loginsArrayList;
    private RecyclerView loginsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        // needed to get the passphrase for SQLCipher
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        char[] masterPassword = pref.getString(getString(R.string.prefs_master_password), null).toCharArray();

        // initialises access with local room database
        final byte[] passphrase = SQLiteDatabase.getBytes(masterPassword);
        final SupportFactory factory = new SupportFactory(passphrase);
        final AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "easypass-db")
                .openHelperFactory(factory)
                .allowMainThreadQueries()
                .build();

        // gets all logins to view in home page
        loginsRecyclerView = homeView.findViewById(R.id.home_password_list);
        loginsArrayList = (ArrayList<Login>) db.loginDao().getAllLogins();

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