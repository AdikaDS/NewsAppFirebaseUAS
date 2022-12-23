package com.example.uaspraktikumpemrogramanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TambahBeritaActivity extends AppCompatActivity {

    EditText judul, author, isiBerita;
    TextView kategori;
    Button tambahBerita;
    DatabaseReference mDatabaseReference, mDatabaseReference1;

    Berita berita;

    private static SharedPreferences mSharedPref;
    private final static String sharedPrefFile = "com.example.uaspraktikumpemrogramanandroid";
    private final static String USERNAME_KEY = "username-key";
    private final static String KATEGORI_KEY = "kategori-key";

    long maxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_berita);

        // Mengatur antara id xml dengan variabel class
        judul = findViewById(R.id.tbh_judul);
        author = findViewById(R.id.tbh_author);
        isiBerita = findViewById(R.id.tbh_isi_berita);
        tambahBerita = findViewById(R.id.btn_add_berita);
        kategori = findViewById(R.id.category_add);

        // Mengatur shared preference
        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        String usernameParsing = mSharedPref.getString(USERNAME_KEY, "");

        // Menerima parsing data
        String kategoriParsing = mSharedPref.getString(KATEGORI_KEY,"");
        kategori.setText(kategoriParsing);

        // Mengatur ke Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(usernameParsing).child("Berita").child("Kategori").child(kategoriParsing);
        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("BeritaAll").child("Kategori").child(kategoriParsing);

        // Memanggil class berita untuk mendapatkan objek didalamnya
        berita = new Berita();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    maxID = (datasnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tambahBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mendapatkan nilai string dari edit text
                String valueJudul = judul.getText().toString();
                String valueAuthor = author.getText().toString();
                String valueIsiBerita = isiBerita.getText().toString();
                if (valueJudul.isEmpty() || valueAuthor.isEmpty() || valueIsiBerita.isEmpty()) {
                    Toast.makeText(TambahBeritaActivity.this, "Data tidak boleh kosong !",
                            Toast.LENGTH_SHORT).show();
                }  else {
                    // Mengirim data ke Firebase Realtime Database
                    berita.setJudulBerita(valueJudul);
                    berita.setAuthors(valueAuthor);
                    berita.setIsi(valueIsiBerita);

                    mDatabaseReference.child(String.valueOf(maxID + 1)).setValue(berita);
                    mDatabaseReference1.push().setValue(berita);
                    Toast.makeText(TambahBeritaActivity.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TambahBeritaActivity.this, ListBeritaActivity.class);
                    startActivity(intent);

                }
            }
        });

    }
}