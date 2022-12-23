package com.example.uaspraktikumpemrogramanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

public class TambahBeritaActivity extends AppCompatActivity {

    private NotificationManager mnotificationManager;
    private final static String CHANNEL_ID = "primary-channel";
    private int NOTIFICATION_ID = 0;

    EditText judul, author, isiBerita;
    TextView kategori;
    Button tambahBerita;
    DatabaseReference mDatabaseReference, mDatabaseReference1;

    Berita berita;

    private static SharedPreferences mSharedPref;
    private final static String sharedPrefFile = "com.example.uaspraktikumpemrogramanandroid";
    private final static String USERNAME_KEY = "username-key";
    private final static String KATEGORI_KEY = "kategori-key";

    int key = 0;

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

        // Mengatur notif
        mnotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "app notif", NotificationManager.IMPORTANCE_HIGH);
            mnotificationManager.createNotificationChannel(notificationChannel);
        }

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

        tambahBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mendapatkan nilai string dari edit text
                String valueJudul = judul.getText().toString();
                String valueAuthor = author.getText().toString();
                String valueIsiBerita = isiBerita.getText().toString();
                sendNotification();
                if (valueJudul.isEmpty() || valueAuthor.isEmpty() || valueIsiBerita.isEmpty()) {
                    Toast.makeText(TambahBeritaActivity.this, "Data tidak boleh kosong !",
                            Toast.LENGTH_SHORT).show();

                }  else {
                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChild(valueJudul)) {
                                Toast.makeText(TambahBeritaActivity.this, "Judul telah terdaftar", Toast.LENGTH_SHORT).show();
                            } else {
                                judul.setText(berita.getJudulBerita());
                                author.setText(berita.getAuthors());
                                isiBerita.setText(berita.getIsi());

                                // Mengirim data ke Firebase Realtime Database
                                berita.setJudulBerita(valueJudul);
                                berita.setAuthors(valueAuthor);
                                berita.setIsi(valueIsiBerita);

                                // Kita menggunakan judul sebagai unique identity untuk semua user
                                mDatabaseReference.child(valueJudul).setValue(berita);
                                mDatabaseReference1.child(valueJudul).setValue(berita);
                                Toast.makeText(TambahBeritaActivity.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TambahBeritaActivity.this, ListBeritaActivity.class);
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

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent(this, ListBeritaActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent
                .getActivity(this, NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notifyBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Berhasil Input")
                        .setContentText("Input berita sukses dilakukan!")
                        .setSmallIcon(R.drawable.ic_baseline_lightbulb).setContentIntent(notificationPendingIntent);
        return notifyBuilder;
    }

    private void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mnotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }
}