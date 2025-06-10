package com.millenialzdev.logindanregistervolleymysql;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;


import java.util.ArrayList;
import java.util.List;

public class PendaftaranFragment extends Fragment {

    private RecyclerView rvPendaftarBaru;
    private PendaftarAdapter pendaftarAdapter;
    private List<Pendaftar> pendaftarList;
    private TextView tvEmptyState;
    private EditText etSearchPendaftar;

    public PendaftaranFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendaftaran, container, false);

        rvPendaftarBaru = view.findViewById(R.id.rv_pendaftar_baru);
        tvEmptyState = view.findViewById(R.id.tv_empty_state_pendaftar);
        etSearchPendaftar = view.findViewById(R.id.et_search_pendaftar);

        // Inisialisasi daftar pendaftar
        pendaftarList = new ArrayList<>();
        // Contoh data (nantinya dari database/API)
        pendaftarList.add(new Pendaftar("Andi Wijaya", "2023001", "Menunggu Verifikasi"));
        pendaftarList.add(new Pendaftar("Budi Santoso", "2023002", "Diverifikasi"));
        pendaftarList.add(new Pendaftar("Citra Dewi", "2023003", "Menunggu Verifikasi"));
        pendaftarList.add(new Pendaftar("Dewi Lestari", "2023004", "Ditolak"));
        pendaftarList.add(new Pendaftar("Eko Prasetyo", "2023005", "Menunggu Verifikasi"));


        pendaftarAdapter = new PendaftarAdapter(pendaftarList);
        rvPendaftarBaru.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPendaftarBaru.setAdapter(pendaftarAdapter);

        // Atur search functionality
        etSearchPendaftar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        updateEmptyState(); // Periksa status kosong saat pertama kali dimuat

        return view;
    }

    private void filter(String text) {
        List<Pendaftar> filteredList = new ArrayList<>();
        for (Pendaftar item : pendaftarList) {
            if (item.getNama().toLowerCase().contains(text.toLowerCase()) ||
                    item.getNim().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        ((PendaftarAdapter) rvPendaftarBaru.getAdapter()).updateList(filteredList); // Perlu method updateList di adapter
        updateEmptyState(filteredList.isEmpty());
    }

    private void updateEmptyState() {
        if (pendaftarList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvPendaftarBaru.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvPendaftarBaru.setVisibility(View.VISIBLE);
        }
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvPendaftarBaru.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvPendaftarBaru.setVisibility(View.VISIBLE);
        }
    }
}