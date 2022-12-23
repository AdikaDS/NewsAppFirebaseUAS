package com.example.uaspraktikumpemrogramanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailBeritaActivity extends AppCompatActivity {

    TextView judulBerita, author, detailBerita;
    String txtJudul, txtAuthor, txtDetail;
    Berita berita;
    DatabaseReference mDatabaseReference, mDatabaseReference1;

    private static SharedPreferences mSharedPref;
    private final static String sharedPrefFile = "com.example.uaspraktikumpemrogramanandroid";
    private final static String USERNAME_KEY = "username-key";
    private final static String KATEGORI_KEY = "kategori-key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        Bundle bundle = getIntent().getExtras();
        txtJudul = bundle.getString("title");
        txtAuthor = bundle.getString("author");
        txtDetail = bundle.getString("detail");

        judulBerita = findViewById(R.id.judul);
        author = findViewById(R.id.author);
        detailBerita = findViewById(R.id.isi_berita);

        judulBerita.setText(txtJudul);
        author.setText(txtAuthor);
        detailBerita.setText(txtDetail);

        // Mengatur shared preference
        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        String usernameParsing = mSharedPref.getString(USERNAME_KEY, "");

        // Menerima parsing data
        String kategoriParsing = mSharedPref.getString(KATEGORI_KEY,"");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(usernameParsing).child("Berita").child("Kategori").child(kategoriParsing);
        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("BeritaAll").child("Kategori").child(kategoriParsing);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more, menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_edit:
                Intent intent = new Intent(DetailBeritaActivity.this, EditBeritaActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("title", txtJudul);
                bundle.putString("author", txtAuthor);
                bundle.putString("detail", txtDetail);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.menu_delete:
                mDatabaseReference.child(txtJudul).removeValue();
                mDatabaseReference1.child(txtJudul).removeValue();
                Toast.makeText(DetailBeritaActivity.this, "Delete Data Sucessfully", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(DetailBeritaActivity.this, ListBeritaActivity.class);
                startActivity(intent1);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void readData() {
//        berita = new Berita();
//        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChildren()) {
//                    for (DataSnapshot data : snapshot.getChildren()) {
//                        keyBerita = data.getKey();
//                        // Mengirim data ke Firebase Realtime Database
//                        berita.setJudulBerita(data.child("judulBerita").getValue().toString());
//                        berita.setAuthors(data.child("authors").getValue().toString());
//                        berita.setIsi(data.child("isi").getValue().toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void saveData() {

    }
}