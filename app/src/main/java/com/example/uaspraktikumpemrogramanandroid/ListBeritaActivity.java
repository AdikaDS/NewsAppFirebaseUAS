package com.example.uaspraktikumpemrogramanandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ListBeritaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Berita> list = new ArrayList<>();
    TextView hasilUmur;
    Spinner kategori;

    static SharedPreferences mSharedPref;
    final String sharedPrefFile = "com.example.taskloginsharedpreferences";
    final static String NAMA_KEY = "nama-key";
    final static String UMUR_KEY = "umur-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_berita);

        // Mengatur antara id xml dengan variabel class
        recyclerView = findViewById(R.id.reycle_view);
        hasilUmur = findViewById(R.id.umur);
        kategori = findViewById(R.id.category);

        // Mengatur shared preference
        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Mengambil nilai dari shared preference RegisterActivity
        String name = mSharedPref.getString(NAMA_KEY, "");

        showRecyclerList();

        // Setting isi spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_array, R.layout.spinner_list);
        kategori.setAdapter(adapter);

    }

    private void showRecyclerList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BeritaAdapter listAdapter = new BeritaAdapter(list);
        recyclerView.setAdapter(listAdapter);
    }
}