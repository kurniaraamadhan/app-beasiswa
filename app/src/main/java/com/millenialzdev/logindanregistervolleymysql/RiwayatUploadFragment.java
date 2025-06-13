package com.millenialzdev.logindanregistervolleymysql;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

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

public class RiwayatUploadFragment extends Fragment implements RiwayatUploadAdapter.OnItemClickListener {

    private static final String API_URL_BERKAS_RIWAYAT = "http://192.168.100.4/my_api_android/upload_berkas.php";
    private static final String API_URL_MAHASISWA_SEARCH = "http://192.168.100.4/my_api_android/mahasiswa_crud.php";

    private RecyclerView rvRiwayatUpload;
    private RiwayatUploadAdapter riwayatUploadAdapter;
    private List<Berkas> riwayatBerkasList;
    private List<Berkas> filteredRiwayatBerkasList;

    private TextView tvEmptyState;
    private EditText etSearchRiwayat;
    private Spinner spinnerFilterStatus;

    private List<Pendaftar> dummyPendaftarList;
    private RequestQueue requestQueue;
    private String loggedInKampus;
    private String loggedInRole;


    public RiwayatUploadFragment() {
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
        View view = inflater.inflate(R.layout.fragment_riwayat_upload, container, false);

        rvRiwayatUpload = view.findViewById(R.id.rv_riwayat_upload);
        tvEmptyState = view.findViewById(R.id.tv_empty_state_riwayat);
        etSearchRiwayat = view.findViewById(R.id.et_search_riwayat);
        spinnerFilterStatus = view.findViewById(R.id.spinner_filter_status);

        riwayatBerkasList = new ArrayList<>();
        filteredRiwayatBerkasList = new ArrayList<>();

        riwayatUploadAdapter = new RiwayatUploadAdapter(filteredRiwayatBerkasList, this);
        rvRiwayatUpload.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRiwayatUpload.setAdapter(riwayatUploadAdapter);

        setupFilterStatusSpinner();

        etSearchRiwayat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Dummy pendaftar list (tetap di sini untuk onLihatDetailPendaftarClick)
        // ... (data dummy pendaftar sama seperti sebelumnya, pastikan 12 parameter) ...
        dummyPendaftarList = new ArrayList<>();
        dummyPendaftarList.add(new Pendaftar(
                "Andi Wijaya", "2023001", "Menunggu Verifikasi Pemprov",
                "10/01/2002", "Laki-laki", "081234567890", "andi.w@email.com",
                "Jl. Mawar No.1, Jakarta", "Teknik Informatika", "2023", "3.85",
                "10 Juni 2025"
        ));
        dummyPendaftarList.add(new Pendaftar(
                "Budi Santoso", "2023002", "Diverifikasi Pemprov",
                "05/03/2001", "Laki-laki", "081122334455", "budi.s@email.com",
                "Jl. Anggrek No.2, Bandung", "Sistem Informasi", "2022", "3.60",
                "09 Juni 2025"
        ));
        dummyPendaftarList.add(new Pendaftar(
                "Citra Dewi", "2023003", "Menunggu Verifikasi Pemprov",
                "22/07/2003", "Perempuan", "087899887766", "citra.d@email.com",
                "Jl. Melati No.3, Surabaya", "Desain Komunikasi Visual", "2023", "3.90",
                "08 Juni 2025"
        ));
        dummyPendaftarList.add(new Pendaftar(
                "Dewi Lestari", "2023004", "Ditolak Pemprov",
                "15/11/2000", "Perempuan", "085611223344", "dewi.l@email.com",
                "Jl. Kenanga No.4, Yogyakarta", "Akuntansi", "2021", "3.45",
                "07 Juni 2025"
        ));
        dummyPendaftarList.add(new Pendaftar(
                "Eko Prasetyo", "2023005", "Menunggu Verifikasi Pemprov",
                "01/09/2002", "Laki-laki", "082100998877", "eko.p@email.com",
                "Jl. Kamboja No.5, Malang", "Manajemen", "2022", "3.75",
                "06 Juni 2025"
        ));
        dummyPendaftarList.add(new Pendaftar(
                "Fajar Rahman", "2023010", "Ditolak Pemprov",
                "12/04/2003", "Laki-laki", "081312345678", "fajar.r@email.com",
                "Jl. Pinus No.10, Jakarta", "Teknik Elektro", "2023", "3.20",
                "05 Juni 2025"
        ));
        dummyPendaftarList.add(new Pendaftar(
                "Gita Indah", "2023015", "Ditolak Pemprov",
                "08/08/2002", "Perempuan", "087787654321", "gita.i@email.com",
                "Jl. Cemara No.15, Semarang", "Kedokteran", "2022", "3.55",
                "04 Juni 2025"
        ));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchRiwayatBerkas();
    }


    private void setupFilterStatusSpinner() {
        List<String> statusOptions = new ArrayList<>();
        statusOptions.add("Semua Status");
        statusOptions.add("Menunggu Verifikasi Pemprov");
        statusOptions.add("Diverifikasi Pemprov");
        statusOptions.add("Ditolak Pemprov");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, statusOptions);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterStatus.setAdapter(adapter);

        spinnerFilterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchRiwayatBerkas() {
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
        String url = API_URL_BERKAS_RIWAYAT + "?kampus=" + Uri.encode(loggedInKampus) +
                "&role=" + Uri.encode(loggedInRole);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (getContext() == null) return;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray berkasArray = jsonResponse.getJSONArray("berkas");
                            riwayatBerkasList.clear();
                            for (int i = 0; i < berkasArray.length(); i++) {
                                JSONObject berkasObject = berkasArray.getJSONObject(i);
                                String nim = berkasObject.getString("nim_mahasiswa");
                                String jenis = berkasObject.getString("jenis_berkas");
                                String status = berkasObject.getString("status_verifikasi");
                                String alasanDitolak = berkasObject.optString("alasan_ditolak", "");
                                String tanggalUpload = berkasObject.getString("tanggal_upload");
                                String urlBerkas = berkasObject.getString("url_berkas");
                                String namaMahasiswa = berkasObject.getString("nama_mahasiswa");

                                Berkas berkas = new Berkas(nim, jenis, status, alasanDitolak, tanggalUpload, urlBerkas, namaMahasiswa);
                                riwayatBerkasList.add(berkas);
                            }
                            applyFilters();
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), "Gagal memuat riwayat berkas: " + message, Toast.LENGTH_LONG).show();
                            updateEmptyState(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON riwayat berkas: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        updateEmptyState(true);
                    }
                },
                error -> {
                    if (getContext() == null) return;
                    Toast.makeText(getContext(), "Error jaringan saat memuat riwayat berkas: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    updateEmptyState(true);
                });
        requestQueue.add(stringRequest);
    }


    private void applyFilters() {
        String searchText = etSearchRiwayat.getText().toString().toLowerCase(Locale.getDefault());
        String selectedStatus = spinnerFilterStatus.getSelectedItem().toString();

        filteredRiwayatBerkasList.clear();

        for (Berkas berkas : riwayatBerkasList) {
            boolean matchesSearch = berkas.getNim().toLowerCase(Locale.getDefault()).contains(searchText) ||
                    berkas.getJenisBerkas().toLowerCase(Locale.getDefault()).contains(searchText) ||
                    berkas.getNamaMahasiswa().toLowerCase(Locale.getDefault()).contains(searchText);

            boolean matchesStatus = selectedStatus.equals("Semua Status") ||
                    berkas.getStatus().equals(selectedStatus);

            if (matchesSearch && matchesStatus) {
                filteredRiwayatBerkasList.add(berkas);
            }
        }
        riwayatUploadAdapter.updateList(filteredRiwayatBerkasList);
        updateEmptyState(filteredRiwayatBerkasList.isEmpty());
    }

    @Override
    public void onLihatBerkasClick(Berkas berkas) {
        if (getContext() == null) return;
        if (berkas.getUrlBerkas() != null && !berkas.getUrlBerkas().isEmpty()) {
            openWebLink(berkas.getUrlBerkas());
        } else {
            Toast.makeText(getContext(), "URL berkas tidak tersedia.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLihatDetailPendaftarClick(Berkas berkas) {
        if (getContext() == null) return;
        Pendaftar targetPendaftar = null;
        for (Pendaftar p : dummyPendaftarList) {
            if (p.getNim().equals(berkas.getNim())) {
                targetPendaftar = p;
                break;
            }
        }

        if (targetPendaftar != null) {
            PendaftarDetailFragment detailFragment = PendaftarDetailFragment.newInstance(targetPendaftar);
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, detailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setToolbarTitle("Detail Pendaftar");
            }
        } else {
            Toast.makeText(getContext(), "Detail pendaftar tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebLink(String url) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            if (getContext() == null) return;
            Toast.makeText(getContext(), "Tidak dapat membuka berkas. URL tidak valid atau tidak ada aplikasi penampil.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvRiwayatUpload.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvRiwayatUpload.setVisibility(View.VISIBLE);
        }
    }
}