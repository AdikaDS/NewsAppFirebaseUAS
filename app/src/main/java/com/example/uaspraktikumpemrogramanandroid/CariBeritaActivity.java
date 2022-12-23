package com.example.uaspraktikumpemrogramanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CariBeritaActivity extends AppCompatActivity {

    TextView umur;
    Spinner kategori;
    Button cari;

    private static SharedPreferences mSharedPref;
    private final static String sharedPrefFile = "com.example.uaspraktikumpemrogramanandroid";
    private final static String KATEGORI_KEY = "kategori-key";
    final static String UMUR_KEY = "umur-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_berita);

        // Mengatur antara id xml dengan variabel class
        umur = findViewById(R.id.umur_search);
        kategori = findViewById(R.id.category_search);
        cari = findViewById(R.id.btn_cari_berita);

        // Setting isi spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_array, R.layout.spinner_list);
        kategori.setAdapter(adapter);

        if (kategori != null) {
            kategori.setAdapter(adapter);
        }

        // Mengatur shared preference
        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Menerima parsing data
        String umurParsing = mSharedPref.getString(UMUR_KEY,"");
        umur.setText(umurParsing);

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Toast.makeText(CariBeritaActivity.this, "Berita Ditemukan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CariBeritaActivity.this, ListBeritaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveData() {
        // Mengambil nilai spinner
        String kategoriValue = kategori.getSelectedItem().toString();
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(KATEGORI_KEY, kategoriValue);
        editor.apply();
    }
}