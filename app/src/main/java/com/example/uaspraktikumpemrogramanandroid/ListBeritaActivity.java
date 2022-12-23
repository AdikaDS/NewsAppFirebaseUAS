package com.example.uaspraktikumpemrogramanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListBeritaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Berita> list = new ArrayList<>();
    TextView hasilUmur, kategori;
    FloatingActionButton fab;
    DatabaseReference mDatabaseReference;

    private static SharedPreferences mSharedPref;
    private final static String sharedPrefFile = "com.example.uaspraktikumpemrogramanandroid";
    private final static String USERNAME_KEY = "username-key";
    private final static String KATEGORI_KEY = "kategori-key";
//    final static String UMUR_KEY = "umur-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_berita);

        // Mengatur antara id xml dengan variabel class
        recyclerView = findViewById(R.id.reycle_view);
        hasilUmur = findViewById(R.id.umur);
        kategori = findViewById(R.id.category);
        fab = findViewById(R.id.fab_add);

        // Mengatur shared preference
        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        String usernameParsing = mSharedPref.getString(USERNAME_KEY, "");

        // Menerima parsing data
        String kategoriParsing = mSharedPref.getString(KATEGORI_KEY,"");
        kategori.setText(kategoriParsing);

        // Mengatur ke Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("BeritaAll").child("Kategori").child(kategoriParsing);

        tampilData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListBeritaActivity.this, TambahBeritaActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showRecyclerList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BeritaAdapter listAdapter = new BeritaAdapter( list, this);
        recyclerView.setAdapter(listAdapter);
    }

    private void tampilData() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        Berita news = item.getValue(Berita.class);
                        news.setKey(item.getKey());
                        list.add(news);
                    }

                    showRecyclerList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_logout:
                LoginActivity.logout();
                Toast.makeText(ListBeritaActivity.this, "Logout Berhasil !",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListBeritaActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_bookmarks:
                Toast.makeText(ListBeritaActivity.this, "Berita yang disimpan/disukai",
                        Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ListBeritaActivity.this, BookmarkActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_search:
                Intent intent2 = new Intent(ListBeritaActivity.this, CariBeritaActivity.class);
                startActivity(intent2);
                mSharedPref.edit().remove(KATEGORI_KEY).apply();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}