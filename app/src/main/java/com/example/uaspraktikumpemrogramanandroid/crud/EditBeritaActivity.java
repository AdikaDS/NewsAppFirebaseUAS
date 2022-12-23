package com.example.uaspraktikumpemrogramanandroid.crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uaspraktikumpemrogramanandroid.tambahan.ListBeritaActivity;
import com.example.uaspraktikumpemrogramanandroid.R;
import com.example.uaspraktikumpemrogramanandroid.model.Berita;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditBeritaActivity extends AppCompatActivity {

    EditText judul, author, isiBerita;
    TextView kategori;
    Button editBerita;
    DatabaseReference mDatabaseReference, mDatabaseReference1;

    Berita berita ;

    private static SharedPreferences mSharedPref;
    private final static String sharedPrefFile = "com.example.uaspraktikumpemrogramanandroid";
    private final static String USERNAME_KEY = "username-key";
    private final static String KATEGORI_KEY = "kategori-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_berita);

        // Mengatur antara id xml dengan variabel class
        judul = findViewById(R.id.edt_judul);
        author = findViewById(R.id.edt_author);
        isiBerita = findViewById(R.id.edt_isi_berita);
        kategori = findViewById(R.id.category_edit);
        editBerita = findViewById(R.id.btn_edit_berita);

        // Identifikasi String
        Bundle bundle = getIntent().getExtras();
        String txtJudul = bundle.getString("title");
        String txtAuthor = bundle.getString("author");
        String txtDetail = bundle.getString("detail");

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

        // Mengatur set text di ambil parsing data activity detail
        judul.setText(txtJudul);
        author.setText(txtAuthor);
        isiBerita.setText(txtDetail);

        editBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mendapatkan nilai string dari edit text
                String valueJudul = judul.getText().toString();
                String valueAuthor = author.getText().toString();
                String valueIsiBerita = isiBerita.getText().toString();

                if (valueJudul.isEmpty() || valueAuthor.isEmpty() || valueIsiBerita.isEmpty()) {
                    Toast.makeText(EditBeritaActivity.this, "Data tidak boleh kosong !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Mengirim data ke Firebase Realtime Database
                                berita.setJudulBerita(valueJudul);
                                berita.setAuthors(valueAuthor);
                                berita.setIsi(valueIsiBerita);

                                // Kita menggunakan judul sebagai unique identity untuk semua user
                                mDatabaseReference.child(valueJudul).setValue(berita);
                                mDatabaseReference1.child(valueJudul).setValue(berita);
                                Toast.makeText(EditBeritaActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditBeritaActivity.this, ListBeritaActivity.class);
                                startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }
}