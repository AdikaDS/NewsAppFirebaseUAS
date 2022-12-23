package com.example.uaspraktikumpemrogramanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RegisterActivity extends AppCompatActivity {

    EditText nama, username, password, tanggalLahir;
    TextView tester;
    Button register;
    DatabaseReference mDatabaseReference;
    User user;
    int umur;

    SharedPreferences mSharedPref;
    final String sharedPrefFile = "com.example.uaspraktikumpemrogramanandroid";
    final static String NAMA_KEY = "nama-key";
    final static String TTL_KEY = "ttl-key";


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

        tester = findViewById(R.id.tester);

        // Mengatur ke Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        // Mengatur shared preference
        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        clickerButton();

//        userAge = String.valueOf(umur);
//        tester.setText(userAge);
//        String umurStr = String.valueOf();
//        tester.setText(umurStr);

        // Memanggil class user untuk mendapatkan objek didalamnya
        user = new User();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Mendapatkan nilai string dari edit text
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
                                user.setUsername(valueUsername);
                                user.setNamaLengkap(valueNama);
                                user.setPassword(valuePassword);
                                user.setTanggalLahir(valueTtl);

                                // Kita menggunakan username sebagai unique identity untuk semua user
                                mDatabaseReference.child(valueUsername).setValue(user);
                                Toast.makeText(RegisterActivity.this, "Sign Up telah berhasil", Toast.LENGTH_SHORT).show();

                                // Mengatur umur lalu di parsing data umur yang didapat ke activity lain
//                                int hasilUmur = umur;
//                                String hasilUmurStr = String.valueOf(hasilUmur);
//                                intent.putExtra("kodeUmur", hasilUmurStr);
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
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

    private void saveData() {

        // Mendapatkan nilai string dari edit text
        String valueNama = nama.getText().toString();
        String valueTtl = tanggalLahir.getText().toString();

        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(NAMA_KEY, valueNama);
        editor.putString(TTL_KEY, valueTtl);
//        editor.putString(UMUR_KEY, hasilUmurStr);
        editor.apply();
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

        Calendar currentDate = new GregorianCalendar();
        umur = currentDate.get(Calendar.YEAR) - year;
        if (month > currentDate.get(Calendar.MONTH) || (month == currentDate.get(Calendar.MONTH) &&
                day > currentDate.get(Calendar.DAY_OF_MONTH)))
        {umur--;}
//        umur = (2022 - year);
//        userAge = Integer.toString(umur);
//        tester.setText(userAge);

        String dateMessage = dayString + "/" + monthString + "/" + yearString;
        tanggalLahir.setText(dateMessage);
    }
    

}