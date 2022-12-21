package com.example.uaspraktikumpemrogramanandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ListBeritaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Berita> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_berita);

        recyclerView = findViewById(R.id.reycle_view);

        showRecyclerList();

    }

    private void showRecyclerList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BeritaAdapter listAdapter = new BeritaAdapter(list);
        recyclerView.setAdapter(listAdapter);
    }
}