package com.millenialzdev.logindanregistervolleymysql;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class BerkasDitolakFragment extends Fragment {

    private RecyclerView rvBerkasDitolak;
    private BerkasDitolakAdapter berkasDitolakAdapter;
    private List<Berkas> berkasList;
    private TextView tvEmptyState;
    private EditText etSearchBerkasDitolak;

    public BerkasDitolakFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout utama fragment_berkas_ditolak.xml
        View view = inflater.inflate(R.layout.fragment_berkas_ditolak, container, false);

        // Cari View berdasarkan ID dari fragment_berkas_ditolak.xml
        rvBerkasDitolak = view.findViewById(R.id.rv_berkas_ditolak); // ID RECYCLERVIEW UTAMA
        tvEmptyState = view.findViewById(R.id.tv_empty_state_berkas_ditolak);
        etSearchBerkasDitolak = view.findViewById(R.id.et_search_berkas_ditolak);

        berkasList = new ArrayList<>();
        // Contoh data berkas yang ditolak (dengan 4 argumen)
        berkasList.add(new Berkas("2023004", "Surat Pernyataan Tidak Mampu", "Ditolak", "Dokumen tidak lengkap."));
        berkasList.add(new Berkas("2023010", "Fotokopi Kartu Keluarga", "Ditolak", "Gambar blur, tidak dapat diverifikasi."));
        berkasList.add(new Berkas("2023015", "Surat Keterangan Penghasilan Orang Tua", "Ditolak", "Format tidak sesuai."));

        berkasDitolakAdapter = new BerkasDitolakAdapter(berkasList);
        rvBerkasDitolak.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBerkasDitolak.setAdapter(berkasDitolakAdapter);

        etSearchBerkasDitolak.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        updateEmptyState(berkasList.isEmpty());

        return view;
    }

    private void filter(String text) {
        List<Berkas> filteredList = new ArrayList<>();
        for (Berkas item : berkasList) {
            if (item.getNim().toLowerCase().contains(text.toLowerCase()) ||
                    item.getJenisBerkas().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (berkasDitolakAdapter != null) {
            berkasDitolakAdapter.updateList(filteredList);
        }
        updateEmptyState(filteredList.isEmpty());
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvBerkasDitolak.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvBerkasDitolak.setVisibility(View.VISIBLE);
        }
    }
}