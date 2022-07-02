package com.example.easypass.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easypass.login.LoginObjectListAdapter;
import com.example.easypass.R;
import com.example.easypass.database.AppDatabase;
import com.example.easypass.database.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private FloatingActionButton searchLoginBtn;
    private ArrayList<Login> loginArrayList;
    private RecyclerView loginsRecyclerView;
    private TextInputEditText searchLoginInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View searchView = inflater.inflate(R.layout.fragment_search, container, false);

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

        searchLoginInput = searchView.findViewById(R.id.loginSearchInput);
        loginsRecyclerView = searchView.findViewById(R.id.search_password_list);
        searchLoginBtn = (FloatingActionButton) searchView.findViewById(R.id.search_login_fab);
        loginArrayList = new ArrayList<Login>();

        LoginObjectListAdapter adapter = new LoginObjectListAdapter(loginArrayList);
        loginsRecyclerView.setAdapter(adapter);
        loginsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        searchLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = searchLoginInput.getText().toString();
                adapter.setData(db.loginDao().getSearchedLogins(searchTerm));
            }
        });

        return searchView;
    }
}