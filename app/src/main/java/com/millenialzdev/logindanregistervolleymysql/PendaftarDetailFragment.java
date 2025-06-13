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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PendaftarDetailFragment extends Fragment implements UploadedBerkasAdapter.OnItemClickListener {

    private static final String ARG_PENDAFTAR = "pendaftar_object";
    private static final String API_URL_BERKAS_BY_NIM = "http://192.168.100.4/my_api_android/upload_berkas.php";

    private Pendaftar pendaftar;

    private TextView tvDetailNama, tvDetailNIM, tvDetailTanggalLahir, tvDetailJenisKelamin,
            tvDetailNomorTelepon, tvDetailEmail, tvDetailAlamat,
            tvDetailProdi, tvDetailAngkatan, tvDetailIPK,
            tvDetailStatusPendaftaran, tvDetailPesanStatus;
    private Button btnEditDataPendaftar;

    private RecyclerView rvUploadedBerkas;
    private UploadedBerkasAdapter uploadedBerkasAdapter;
    private List<Berkas> uploadedBerkasList;
    private TextView tvNoUploadedBerkas;

    private RequestQueue requestQueue;

    public PendaftarDetailFragment() {

    }

    public static PendaftarDetailFragment newInstance(Pendaftar pendaftar) {
        PendaftarDetailFragment fragment = new PendaftarDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PENDAFTAR, pendaftar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pendaftar = getArguments().getParcelable(ARG_PENDAFTAR);
        }
        if (getContext() != null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendaftar_detail, container, false);

        tvDetailNama = view.findViewById(R.id.tv_detail_nama);
        tvDetailNIM = view.findViewById(R.id.tv_detail_nim);
        tvDetailTanggalLahir = view.findViewById(R.id.tv_detail_tanggal_lahir);
        tvDetailJenisKelamin = view.findViewById(R.id.tv_detail_jenis_kelamin);
        tvDetailNomorTelepon = view.findViewById(R.id.tv_detail_nomor_telepon);
        tvDetailEmail = view.findViewById(R.id.tv_detail_email);
        tvDetailAlamat = view.findViewById(R.id.tv_detail_alamat);
        tvDetailProdi = view.findViewById(R.id.tv_detail_prodi);
        tvDetailAngkatan = view.findViewById(R.id.tv_detail_angkatan);
        tvDetailIPK = view.findViewById(R.id.tv_detail_ipk);
        tvDetailStatusPendaftaran = view.findViewById(R.id.tv_detail_status_pendaftaran);
        tvDetailPesanStatus = view.findViewById(R.id.tv_detail_pesan_status);

        btnEditDataPendaftar = view.findViewById(R.id.btn_edit_data_pendaftar);

        rvUploadedBerkas = view.findViewById(R.id.rv_uploaded_berkas);
        tvNoUploadedBerkas = view.findViewById(R.id.tv_no_uploaded_berkas);
        uploadedBerkasList = new ArrayList<>();
        uploadedBerkasAdapter = new UploadedBerkasAdapter(uploadedBerkasList, this);
        rvUploadedBerkas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUploadedBerkas.setAdapter(uploadedBerkasAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (pendaftar != null) {
            tvDetailNama.setText("Nama: " + pendaftar.getNama());
            tvDetailNIM.setText("NIM: " + pendaftar.getNim());
            tvDetailTanggalLahir.setText("Tanggal Lahir: " + pendaftar.getTanggalLahir());
            tvDetailJenisKelamin.setText("Jenis Kelamin: " + pendaftar.getJenisKelamin());
            tvDetailNomorTelepon.setText("Telp: " + pendaftar.getNomorTelepon());
            tvDetailEmail.setText("Email: " + pendaftar.getEmail());
            tvDetailAlamat.setText("Alamat: " + pendaftar.getAlamat());
            tvDetailProdi.setText("Program Studi: " + pendaftar.getProgramStudi());
            tvDetailAngkatan.setText("Angkatan: " + pendaftar.getAngkatan());
            tvDetailIPK.setText("IPK: " + pendaftar.getIpk());
            tvDetailStatusPendaftaran.setText("Status: " + pendaftar.getStatus());

            String pesanStatus;
            int statusColor;
            switch (pendaftar.getStatus()) {
                case "Menunggu Verifikasi Pemprov":
                    pesanStatus = "Berkas sedang dalam proses pemeriksaan oleh tim verifikator Pemprov.";
                    statusColor = getResources().getColor(android.R.color.holo_orange_dark);
                    break;
                case "Diverifikasi Pemprov":
                    pesanStatus = "Berkas telah diverifikasi dan disetujui oleh Pemprov.";
                    statusColor = getResources().getColor(android.R.color.holo_green_dark);
                    break;
                case "Ditolak Pemprov":
                    pesanStatus = "Berkas ditolak oleh Pemprov. Silakan cek bagian Berkas Ditolak.";
                    statusColor = getResources().getColor(android.R.color.holo_red_dark);
                    break;
                default:
                    pesanStatus = "Status tidak diketahui.";
                    statusColor = getResources().getColor(android.R.color.darker_gray);
                    break;
            }
            tvDetailPesanStatus.setText(pesanStatus);
            tvDetailStatusPendaftaran.setTextColor(statusColor);

            fetchUploadedBerkas(pendaftar.getNim());

            btnEditDataPendaftar.setOnClickListener(v -> {
                if (getContext() == null) return;
                Toast.makeText(getContext(), "Membuka halaman edit data untuk " + pendaftar.getNama(), Toast.LENGTH_SHORT).show();
                DataDiriFragment editFragment = DataDiriFragment.newInstance(pendaftar);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, editFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).setToolbarTitle("Edit Data Mahasiswa");
                }
            });
        } else {
            if (getContext() == null) return;
            Toast.makeText(getContext(), "Detail pendaftar tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUploadedBerkas(String nimMahasiswa) {
        if (requestQueue == null || getContext() == null) {
            if (getContext() != null) Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = API_URL_BERKAS_BY_NIM + "?nim=" + Uri.encode(nimMahasiswa);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (getContext() == null) return;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray berkasArray = jsonResponse.getJSONArray("berkas");
                            uploadedBerkasList.clear();
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
                                uploadedBerkasList.add(berkas);
                            }
                            uploadedBerkasAdapter.updateList(uploadedBerkasList);
                            updateUploadedBerkasUIState(uploadedBerkasList.isEmpty());
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), "Gagal memuat berkas pendaftar: " + message, Toast.LENGTH_LONG).show();
                            updateUploadedBerkasUIState(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON berkas pendaftar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        updateUploadedBerkasUIState(true);
                    }
                },
                error -> {
                    if (getContext() == null) return;
                    Toast.makeText(getContext(), "Error jaringan saat memuat berkas pendaftar: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    updateUploadedBerkasUIState(true);
                });
        if (requestQueue != null) {
            requestQueue.add(stringRequest);
        }
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

    private void updateUploadedBerkasUIState(boolean isEmpty) {
        if (isEmpty) {
            rvUploadedBerkas.setVisibility(View.GONE);
            tvNoUploadedBerkas.setVisibility(View.VISIBLE);
        } else {
            rvUploadedBerkas.setVisibility(View.VISIBLE);
            tvNoUploadedBerkas.setVisibility(View.GONE);
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
}