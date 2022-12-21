package com.example.uaspraktikumpemrogramanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    EditText nama, username, password, tanggalLahir;
    Button register;
    DatabaseReference mDatabaseReference;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Mengatur antara id xml dengan variabel class
        nama = findViewById(R.id.nama_lengkap);
        username = findViewById(R.id.username_register);
        password = findViewById(R.id.password_register);
        tanggalLahir = findViewById(R.id.tgl_lahir);
        register = findViewById(R.id.btn_register);

        // Mengatur ke Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        clickerButton();
        user = new User();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valueNama = nama.getText().toString();
                String valueUsername = username.getText().toString();
                String valuePassword = password.getText().toString();
                String valueTtl = tanggalLahir.getText().toString();

                if (valueNama.isEmpty() || valueUsername.isEmpty() || valuePassword.isEmpty() || valueTtl.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Data tidak boleh kosong !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
                            // Mengecek apakah sudah ada username yang terdaftar atau belum
                            if (snapshot.hasChild(valueUsername)) {
                                Toast.makeText(RegisterActivity.this, "Username telah terdaftar", Toast.LENGTH_SHORT).show();
                            } else {
                                // Mengirim data ke Firebase Realtime Database
                                user.setNamaLengkap(valueNama);
                                user.setPassword(valuePassword);
                                user.setTanggalLahir(valueTtl);

                                // Kita menggunakan maxID sebagai unique didentity untuk semua user
                                mDatabaseReference.child(valueUsername).setValue(user);
                                Toast.makeText(RegisterActivity.this, "Sign Up telah berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
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

    public void clickerButton() {
        tanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerTtl();
            }
        });
    }

    public void showDatePickerTtl() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "date-picker");
    }

    public void processDatePickerResultTtl(int day, int month, int year) {
        String dayString = Integer.toString(day);
        String monthString = Integer.toString(month + 1);
        String yearString = Integer.toString(year);

//        umur = (2022 - year);
//        String totalUmur = Integer.toString(umur);
//        hasilUmur.setText(totalUmur);

        String dateMessage = dayString + "/" + monthString + "/" + yearString;
        tanggalLahir.setText(dateMessage);
    }

}