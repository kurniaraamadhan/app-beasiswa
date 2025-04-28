package com.millenialzdev.logindanregistervolleymysql;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private BerkasAdapter berkasAdapter;
    private List<String> berkasList = new ArrayList<>();

    private static final int REQUEST_CODE = 100; // untuk memilih file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup Drawer Layout
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // RecyclerView Setup
        recyclerView = findViewById(R.id.recycler_berkas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        berkasAdapter = new BerkasAdapter(berkasList);
        recyclerView.setAdapter(berkasAdapter);

        // Tombol Upload Berkas
        Button uploadButton = findViewById(R.id.button_upload);
        uploadButton.setOnClickListener(v -> openFilePicker());

        // Tombol untuk menambah berkas baru (simulasi)
        Button tambahBerkasButton = findViewById(R.id.tambahBerkasButton);
        tambahBerkasButton.setOnClickListener(v -> tambahBerkas("Berkas Baru"));
    }

    // Fungsi untuk menambah berkas dinamis
    private void tambahBerkas(String namaBerkas) {
        berkasList.add(namaBerkas);
        berkasAdapter.notifyDataSetChanged();
    }

    // Fungsi untuk membuka file picker
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");  // Mengizinkan memilih file apapun
        startActivityForResult(Intent.createChooser(intent, "Pilih Berkas"), REQUEST_CODE);
    }

    // Menangani hasil pemilihan file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String filePath = data.getData().toString(); // Mendapatkan path file yang dipilih
            Toast.makeText(this, "File dipilih: " + filePath, Toast.LENGTH_SHORT).show();
            tambahBerkas("Berkas File: " + filePath); // Menambahkan file ke daftar berkas
        }
    }

    // Mengatur navigasi drawer item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_upload) {
            Toast.makeText(this, "Upload Berkas", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_not_verified) {
            Toast.makeText(this, "Berkas Tidak Lolos", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Menangani tombol back pada drawer
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
