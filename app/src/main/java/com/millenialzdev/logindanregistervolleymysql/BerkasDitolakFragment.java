package com.millenialzdev.logindanregistervolleymysql;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context; // Import ini
import android.content.SharedPreferences; // Import ini

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

public class BerkasDitolakFragment extends Fragment implements BerkasDitolakAdapter.OnItemClickListener {

    private static final String API_URL_BERKAS_DITOLAK = "http://192.168.100.4/my_api_android/upload_berkas.php";
    private static final String API_URL_MAHASISWA_SEARCH = "http://192.168.100.4/my_api_android/mahasiswa_crud.php";

    private RecyclerView rvBerkasDitolak;
    private BerkasDitolakAdapter berkasDitolakAdapter;
    private List<Berkas> berkasList;
    private List<Berkas> filteredBerkasList;
    private TextView tvEmptyState;
    private EditText etSearchBerkasDitolak;

    private List<Pendaftar> dummyPendaftarList;
    private RequestQueue requestQueue;
    private String loggedInKampus;
    private String loggedInRole;


    public BerkasDitolakFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            requestQueue = Volley.newRequestQueue(getContext());

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            loggedInKampus = sharedPreferences.getString("kampus", "");
            loggedInRole = sharedPreferences.getString("role", "android");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berkas_ditolak, container, false);

        rvBerkasDitolak = view.findViewById(R.id.rv_berkas_ditolak);
        tvEmptyState = view.findViewById(R.id.tv_empty_state_berkas_ditolak);
        etSearchBerkasDitolak = view.findViewById(R.id.et_search_berkas_ditolak);

        berkasList = new ArrayList<>();
        filteredBerkasList = new ArrayList<>();

        berkasDitolakAdapter = new BerkasDitolakAdapter(filteredBerkasList, this);
        rvBerkasDitolak.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBerkasDitolak.setAdapter(berkasDitolakAdapter);

        etSearchBerkasDitolak.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Dummy pendaftar list (tetap di sini untuk onDetailClick)
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
        fetchBerkasDitolakData();
    }

    private void fetchBerkasDitolakData() {
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
        String url = API_URL_BERKAS_DITOLAK + "?status=" + Uri.encode("Ditolak Pemprov") +
                "&kampus=" + Uri.encode(loggedInKampus) +
                "&role=" + Uri.encode(loggedInRole);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (getContext() == null) return;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray berkasArray = jsonResponse.getJSONArray("berkas");
                            berkasList.clear();
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
                                berkasList.add(berkas);
                            }
                            applyFilters();
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), "Gagal memuat berkas ditolak: " + message, Toast.LENGTH_LONG).show();
                            updateEmptyState(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON berkas ditolak: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        updateEmptyState(true);
                    }
                },
                error -> {
                    if (getContext() == null) return;
                    Toast.makeText(getContext(), "Error jaringan saat memuat berkas ditolak: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    updateEmptyState(true);
                });
        if (requestQueue != null) {
            requestQueue.add(stringRequest);
        } else {
            if (getContext() != null) {
                Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void applyFilters() {
        String searchText = etSearchBerkasDitolak.getText().toString().toLowerCase(Locale.getDefault());

        filteredBerkasList.clear();
        for (Berkas item : berkasList) {
            boolean matchesSearch = item.getNim().toLowerCase(Locale.getDefault()).contains(searchText) ||
                    item.getJenisBerkas().toLowerCase(Locale.getDefault()).contains(searchText) ||
                    (item.getAlasanDitolak() != null && item.getAlasanDitolak().toLowerCase(Locale.getDefault()).contains(searchText));

            if (matchesSearch) {
                filteredBerkasList.add(item);
            }
        }
        berkasDitolakAdapter.updateList(filteredBerkasList);
        updateEmptyState(filteredBerkasList.isEmpty());
    }


    @Override
    public void onDetailClick(Berkas berkas) {
        if (getContext() == null) return;

        Pendaftar targetPendaftar = null;
        for (Pendaftar p : dummyPendaftarList) {
            if (p.getNim().equals(berkas.getNim())) {
                targetPendaftar = p;
                break;
            }
        }

        if (targetPendaftar != null) {
            BerkasDitolakDetailFragment detailFragment = BerkasDitolakDetailFragment.newInstance(berkas);
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, detailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setToolbarTitle("Detail Berkas Ditolak");
            }
        } else {
            Toast.makeText(getContext(), "Detail pendaftar tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
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