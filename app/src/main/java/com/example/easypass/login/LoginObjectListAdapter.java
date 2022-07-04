package com.example.easypass.login;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.easypass.R;
import com.example.easypass.database.AppDatabase;
import com.example.easypass.database.Login;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.List;

// https://guides.codepath.com/android/using-the-recyclerview
public class LoginObjectListAdapter extends RecyclerView.Adapter<LoginObjectListAdapter.ViewHolder> {
    private List<Login> loginList;

    public LoginObjectListAdapter(List<Login> loginList) {
        this.loginList = loginList;
    }

    @NonNull
    @Override
    public LoginObjectListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // creates a loginview object and inflates it with the XML file item_login_password
        View loginView = inflater.inflate(R.layout.item_login_password, parent, false);

        // returns viewholder that contains the newly inflated loginview object
        ViewHolder vh = new ViewHolder(loginView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // sets current viewholder's visual data with the position's login object
        Login currentLogin = loginList.get(position);

        TextView titleText = holder.loginTitle;
        titleText.setText(currentLogin.getLoginTitle());

        TextView usernameText = holder.loginUsername;
        usernameText.setText(currentLogin.getLoginUsername());

        Button copyUsernameBtn = holder.copyUsernameBtn;
        copyUsernameBtn.setEnabled(true);

        Button copyPasswordBtn = holder.copyPasswordBtn;
        copyPasswordBtn.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return loginList.size();
    }

    public void setData(List<Login> loginList) {
        this.loginList = loginList;
        notifyDataSetChanged();
    }

    // contains the login object, placed in a viewholder
    // has constructor to create a new row from login object
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView loginTitle;
        public TextView loginUsername;
        public TextView loginPassword;
        public Button copyUsernameBtn;
        public Button copyPasswordBtn;
        Context currentContext;

        public ViewHolder(View loginView) {
            super(loginView);

            currentContext = loginView.getContext();
            loginTitle = (TextView) loginView.findViewById(R.id.loginItemTitle);
            loginUsername = (TextView) loginView.findViewById(R.id.loginItemUsernameView);
            loginPassword = (TextView) loginView.findViewById(R.id.loginItemPasswordView);
            copyUsernameBtn = (Button) loginView.findViewById(R.id.copyUsernameButton);
            copyPasswordBtn = (Button) loginView.findViewById(R.id.copyPasswordButton);

            loginView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewLoginIntent = new Intent(view.getContext(), ViewLoginActivity.class);
                    viewLoginIntent.putExtra("title", loginTitle.getText().toString());
                    view.getContext().startActivity(viewLoginIntent);
                }
            });

            copyUsernameBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // store username into clipboard
                    ClipboardManager clipboardManager = (ClipboardManager) currentContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("username", loginUsername.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(currentContext, "Username copied!", Toast.LENGTH_SHORT).show();
                }
            });

            copyPasswordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // needed to get the passphrase for SQLCipher
                    SharedPreferences pref = currentContext.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
                    char[] masterPassword = pref.getString(currentContext.getString(R.string.prefs_master_password), null).toCharArray();

                    // initialises access with local room database
                    final byte[] passphrase = SQLiteDatabase.getBytes(masterPassword);
                    final SupportFactory factory = new SupportFactory(passphrase);
                    final AppDatabase db = Room.databaseBuilder(currentContext, AppDatabase.class, "easypass-db")
                            .openHelperFactory(factory)
                            .allowMainThreadQueries()
                            .build();

                    // create new login object
                    Login currentLogin = db.loginDao().getLogin(loginTitle.getText().toString());

                    // store password into clipboard
                    ClipboardManager clipboardManager = (ClipboardManager) currentContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("password", currentLogin.getLoginPassword());
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(currentContext, "Password copied!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
