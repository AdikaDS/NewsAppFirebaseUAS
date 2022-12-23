package com.example.uaspraktikumpemrogramanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    DatabaseReference mDatabaseReference;

    static SharedPreferences mSharedPref;
    final String sharedPrefFile = "com.example.uaspraktikumpemrogramanandroid";
    final static String USERNAME_KEY = "username-key";
    final static String LOGIN_STATUS = "login-status";
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Mengatur antara id xml dengan variabel class
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        login = findViewById(R.id.btn_login);

        // Mengatur ke Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        // Mengatur shared preference
        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        isLoggedIn = mSharedPref.getBoolean(LOGIN_STATUS, false);

        // Membuat Intent menjadi global variabel
        Intent intent = new Intent(LoginActivity.this, CariBeritaActivity.class);
        if (isLoggedIn) {
            startActivity(intent);
            finish();
        }

        // Ketika klik login akan menjalankan fungtion dibawah
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valueUsername = username.getText().toString();
                String valuePassword = password.getText().toString();

                if (valueUsername.isEmpty() || valuePassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Data tidak boleh kosong !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // Mengecek apakah sudah ada username yang terdaftar atau belum
                            if (snapshot.hasChild(valueUsername)) {

                                // Ketika username telah terdaftar
                                // Maka sekarang get password dari realtime firebase database dan cocokkan dengan usernamenya
                                String getPassword = snapshot.child(valueUsername).child("password").getValue(String.class);
                                if (getPassword.equals(valuePassword)) {
                                    saveData();
                                    Toast.makeText(LoginActivity.this, "Login Behasil",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    setLoggedIn();
                                    finish();
                                } else {
                                    showAlertDialog();
                                }
                            } else {
                                showAlertDialog();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void setLoggedIn () {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(LOGIN_STATUS, true);
        editor.apply();
    }

    public static void logout() {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(LOGIN_STATUS, false);
        editor.apply();
    }

    public void showAlertDialog () {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertBuilder.setTitle("Login Gagal");
        alertBuilder.setMessage("Username atau password salah, silahkan coba lagi!");

        alertBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                username.getText().clear();
                password.getText().clear();
            }
        });
        alertBuilder.show();
    }

    public void tvRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void saveData() {
        // Mendapatkan nilai string dari edit text
        String valueUsername = username.getText().toString();

        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(USERNAME_KEY, valueUsername);
        editor.apply();
    }


}