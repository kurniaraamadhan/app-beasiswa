package com.millenialzdev.logindanregistervolleymysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Sembunyikan title default
        }
        toolbarTitle = findViewById(R.id.toolbar_title);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener untuk Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;
            String title = "";

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                title = "Beranda";
            } else if (itemId == R.id.nav_pendaftaran) {
                selectedFragment = new PendaftaranFragment();
                title = "Pendaftaran";
            } else if (itemId == R.id.nav_berkas_mahasiswa) {
                selectedFragment = new BerkasMahasiswaFragment();
                title = "Berkas Mahasiswa";
            } else if (itemId == R.id.nav_berkas_ditolak) {
                selectedFragment = new BerkasDitolakFragment();
                title = "Berkas Ditolak";
            } else if (itemId == R.id.nav_data_diri) {
                selectedFragment = new DataDiriFragment();
                title = "Data Diri Mahasiswa";
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
                toolbarTitle.setText(title); // Update judul toolbar
                return true;
            }
            return false;
        });

        // Set Fragment default saat aplikasi pertama kali dibuka
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}