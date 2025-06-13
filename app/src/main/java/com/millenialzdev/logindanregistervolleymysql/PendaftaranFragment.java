package com.millenialzdev.logindanregistervolleymysql;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PendaftaranFragment extends Fragment implements PendaftarAdapter.OnItemClickListener {

    private static final String API_URL_MAHASISWA = "http://192.168.100.4/my_api_android/mahasiswa_crud.php";

    private RecyclerView rvPendaftarBaru;
    private PendaftarAdapter pendaftarAdapter;
    private List<Pendaftar> pendaftarList;
    private List<Pendaftar> originalPendaftarList;
    private TextView tvEmptyState;
    private EditText etSearchPendaftar;

    private RequestQueue requestQueue;
    private String loggedInKampus;
    private String loggedInRole;


    public PendaftaranFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            requestQueue = Volley.newRequestQueue(getContext());
            // Ambil kampus dan role Staff TU yang login dari SharedPreferences
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            loggedInKampus = sharedPreferences.getString("kampus", ""); // <--- BARU: Ambil kampus
            loggedInRole = sharedPreferences.getString("role", "android"); // <--- BARU: Ambil role
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendaftaran, container, false);

        rvPendaftarBaru = view.findViewById(R.id.rv_pendaftar_baru);
        tvEmptyState = view.findViewById(R.id.tv_empty_state_pendaftar);
        etSearchPendaftar = view.findViewById(R.id.et_search_pendaftar);

        pendaftarList = new ArrayList<>();
        originalPendaftarList = new ArrayList<>();

        pendaftarAdapter = new PendaftarAdapter(pendaftarList, this);
        rvPendaftarBaru.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPendaftarBaru.setAdapter(pendaftarAdapter);

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchPendaftarData();
    }

    private void fetchPendaftarData() {
        if (loggedInKampus.isEmpty() && !loggedInRole.equals("developer")) { // <--- BARU: Validasi kampus
            Toast.makeText(getContext(), "Informasi kampus tidak ditemukan. Mohon login ulang.", Toast.LENGTH_LONG).show();
            updateEmptyState(true);
            return;
        }

        if (requestQueue == null || getContext() == null) {
            if (getContext() != null) Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kirim parameter kampus dan role
        String url = API_URL_MAHASISWA + "?kampus=" + Uri.encode(loggedInKampus) +
                "&role=" + Uri.encode(loggedInRole);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (getContext() == null) return;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray mahasiswaArray = jsonResponse.getJSONArray("mahasiswa");
                            originalPendaftarList.clear();
                            pendaftarList.clear();

                            for (int i = 0; i < mahasiswaArray.length(); i++) {
                                JSONObject pendaftarObject = mahasiswaArray.getJSONObject(i);
                                String nama = pendaftarObject.getString("nama_lengkap");
                                String nim = pendaftarObject.getString("nim");
                                String status = pendaftarObject.getString("status_pendaftaran");
                                String tanggalLahir = pendaftarObject.getString("tanggal_lahir");
                                String jenisKelamin = pendaftarObject.getString("jenis_kelamin");
                                String nomorTelepon = pendaftarObject.getString("nomor_telepon");
                                String email = pendaftarObject.getString("email");
                                String alamat = pendaftarObject.getString("alamat");
                                String programStudi = pendaftarObject.getString("program_studi");
                                String angkatan = pendaftarObject.getString("angkatan");
                                String ipk = pendaftarObject.getString("ipk");
                                String tanggalDaftar = pendaftarObject.getString("tanggal_daftar");

                                Pendaftar pendaftar = new Pendaftar(
                                        nama, nim, status, tanggalLahir, jenisKelamin,
                                        nomorTelepon, email, alamat, programStudi, angkatan, ipk, tanggalDaftar
                                );
                                originalPendaftarList.add(pendaftar);
                            }
                            pendaftarList.addAll(originalPendaftarList);
                            pendaftarAdapter.updateList(pendaftarList);

                            updateEmptyState(pendaftarList.isEmpty());
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), "Gagal memuat data: " + message, Toast.LENGTH_LONG).show();
                            updateEmptyState(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        updateEmptyState(true);
                    }
                },
                error -> {
                    if (getContext() == null) return;
                    Toast.makeText(getContext(), "Error jaringan: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    updateEmptyState(true);
                });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDetailClick(Pendaftar pendaftar) {
        if (getContext() == null) return;
        PendaftarDetailFragment detailFragment = PendaftarDetailFragment.newInstance(pendaftar);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle("Detail Pendaftar");
        }
    }

    private void filter(String text) {
        List<Pendaftar> filteredList = new ArrayList<>();
        for (Pendaftar item : originalPendaftarList) {
            if (item.getNama().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault())) ||
                    item.getNim().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))) {
                filteredList.add(item);
            }
        }
        pendaftarList.clear();
        pendaftarList.addAll(filteredList);
        pendaftarAdapter.updateList(pendaftarList);
        updateEmptyState(pendaftarList.isEmpty());
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