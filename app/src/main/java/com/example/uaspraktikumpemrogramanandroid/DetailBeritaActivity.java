package com.example.uaspraktikumpemrogramanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailBeritaActivity extends AppCompatActivity {

    TextView judulBerita, author, detailBerita;
    String txtJudul, txtAuthor, txtDetail;
    int img_photo;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        Bundle bundle = getIntent().getExtras();
        txtJudul = bundle.getString("title");
        txtAuthor = bundle.getString("author");
        txtDetail = bundle.getString("detail");
        img_photo = bundle.getInt("picture");

        judulBerita = findViewById(R.id.judul);
        author = findViewById(R.id.author);
        detailBerita = findViewById(R.id.isi_berita);
        img = findViewById(R.id.gambar_berita);

        judulBerita.setText(txtJudul);
        author.setText(txtAuthor);
        detailBerita.setText(txtDetail);

//        Glide.with(DetailBerita.this)
//                .load(img_photo)
//                .apply(new RequestOptions().override(370,370))
//                .into(img);

    }
}