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

    public BottomNavigationView bottomNavigationView;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbarTitle = findViewById(R.id.toolbar_title);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;
            String title = "";

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                title = "Beranda";
            } else if (itemId == R.id.nav_pendaftaran) {
                selectedFragment = new PendaftaranFragment();
                title = "Pendaftar Beasiswa Baru";
            } else if (itemId == R.id.nav_berkas_mahasiswa) {
                selectedFragment = new BerkasMahasiswaFragment();
                title = "Unggah Berkas Mahasiswa";
            } else if (itemId == R.id.nav_berkas_ditolak) {
                selectedFragment = new BerkasDitolakFragment();
                title = "Berkas Ditolak";
            }

            else if (itemId == R.id.nav_riwayat_upload) {
                selectedFragment = new RiwayatUploadFragment();
                title = "Riwayat Unggahan Berkas";
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
                toolbarTitle.setText(title);
                return true;
            }
            return false;
        });

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

    public void setToolbarTitle(String title) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }
}