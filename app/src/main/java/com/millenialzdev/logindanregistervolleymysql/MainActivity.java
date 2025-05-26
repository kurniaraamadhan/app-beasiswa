package com.millenialzdev.logindanregistervolleymysql;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerBerkas;
    private Button tambahBerkasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // pastikan ini nama layout xml kamu

        recyclerBerkas = findViewById(R.id.recycler_berkas);
        tambahBerkasButton = findViewById(R.id.tambahBerkasButton);

        // Setup RecyclerView (sementara kosong, pakai LinearLayoutManager)
        recyclerBerkas.setLayoutManager(new LinearLayoutManager(this));
        // nanti kamu bisa pasang adapter sesuai data berkas yang kamu punya

        // Tombol tambah berkas baru klik -> buka UploadActivity
        tambahBerkasButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
            startActivity(intent);
        });
    }
}
