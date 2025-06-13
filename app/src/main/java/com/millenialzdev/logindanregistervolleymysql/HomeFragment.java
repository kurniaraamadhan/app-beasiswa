package com.millenialzdev.logindanregistervolleymysql;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.net.Uri;


public class HomeFragment extends Fragment implements NotifikasiAdapter.OnItemClickListener {

    private static final String API_URL_DASHBOARD_STATS = "http://192.168.100.4/my_api_android/dashboard_stats.php";
    private static final String API_URL_BERKAS = "http://192.168.100.4/my_api_android/upload_berkas.php"; // Untuk mengambil berkas ditolak

    private TextView welcomeText;
    private TextView currentDateText;
    private TextView tvNewApplicants, tvPendingFiles, tvApprovedFiles, tvTotalStudents;
    private CardView cardPendaftarBaru, cardKelolaBerkas, cardDaftarMahasiswa, cardPengaturanAkun;

    private RecyclerView rvNotifikasi;
    private NotifikasiAdapter notifikasiAdapter;
    private List<Notifikasi> notifikasiList;
    private TextView tvEmptyNotifikasiState;

    private RequestQueue requestQueue;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        welcomeText = view.findViewById(R.id.welcome_text);
        currentDateText = view.findViewById(R.id.current_date_text);
        tvNewApplicants = view.findViewById(R.id.tv_new_applicants);
        tvPendingFiles = view.findViewById(R.id.tv_pending_files);
        tvApprovedFiles = view.findViewById(R.id.tv_approved_files);
        tvTotalStudents = view.findViewById(R.id.tv_total_students);

        cardPendaftarBaru = view.findViewById(R.id.card_pendaftar_baru);
        cardKelolaBerkas = view.findViewById(R.id.card_kelola_berkas);
        cardDaftarMahasiswa = view.findViewById(R.id.card_daftar_mahasiswa);
        cardPengaturanAkun = view.findViewById(R.id.card_pengaturan_akun);

        rvNotifikasi = view.findViewById(R.id.rv_notifikasi);
        tvEmptyNotifikasiState = view.findViewById(R.id.tv_empty_notifikasi_state);

        notifikasiList = new ArrayList<>(); // Inisialisasi kosong
        notifikasiAdapter = new NotifikasiAdapter(notifikasiList, this);
        rvNotifikasi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotifikasi.setAdapter(notifikasiAdapter);
        // updateEmptyNotifikasiState() akan dipanggil setelah data diambil

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID")); // Koreksi format tanggal
        String currentDate = sdf.format(new Date());
        currentDateText.setText(currentDate);

        // Panggil metode untuk mengambil statistik dari API
        fetchDashboardStats();
        // Panggil metode untuk mengambil notifikasi dari berkas ditolak
        fetchRejectedFilesForNotifications();


        cardPendaftarBaru.setOnClickListener(v -> {
            if (getContext() == null) return;
            Toast.makeText(getContext(), "Membuka Pendaftar Baru...", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.nav_pendaftaran);
            }
        });

        cardKelolaBerkas.setOnClickListener(v -> {
            if (getContext() == null) return;
            Toast.makeText(getContext(), "Membuka Kelola Berkas...", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.nav_berkas_mahasiswa);
            }
        });

        cardDaftarMahasiswa.setOnClickListener(v -> {
            if (getContext() == null) return;
            Toast.makeText(getContext(), "Membuka Formulir Data Diri Mahasiswa...", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new DataDiriFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setToolbarTitle("Formulir Data Diri");
            }
        });

        cardPengaturanAkun.setOnClickListener(v -> {
            if (getContext() == null) return;
            Toast.makeText(getContext(), "Membuka Pengaturan Akun...", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new PengaturanAkunFragment()); // Buka PengaturanAkunFragment
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setToolbarTitle("Pengaturan Akun"); // Update judul toolbar
            }
        });
    }

    // Metode untuk mengambil statistik dashboard dari API
    private void fetchDashboardStats() {
        if (requestQueue == null || getContext() == null) {
            if (getContext() != null) Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL_DASHBOARD_STATS,
                response -> {
                    if (getContext() == null) return;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONObject data = jsonResponse.getJSONObject("data");
                            tvNewApplicants.setText(String.valueOf(data.getInt("new_applicants")));
                            tvPendingFiles.setText(String.valueOf(data.getInt("pending_files")));
                            tvApprovedFiles.setText(String.valueOf(data.getInt("approved_files")));
                            tvTotalStudents.setText(String.valueOf(data.getInt("total_students")));

                            // Update notifikasi berdasarkan statistik
                            generateNotificationsFromStats(data.getInt("new_applicants"), data.getInt("rejected_files"));

                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), "Gagal memuat statistik: " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON statistik: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    if (getContext() == null) return;
                    Toast.makeText(getContext(), "Error jaringan saat memuat statistik: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });
        requestQueue.add(stringRequest);
    }

    // Metode untuk mengambil berkas ditolak (untuk notifikasi)
    private void fetchRejectedFilesForNotifications() {
        if (requestQueue == null || getContext() == null) {
            if (getContext() != null) Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil data dengan status "Ditolak Pemprov"
        String url = API_URL_BERKAS + "?status=" + Uri.encode("Ditolak Pemprov");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (getContext() == null) return;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray berkasArray = jsonResponse.getJSONArray("berkas");
                            // Kita hanya perlu jumlahnya, tapi juga bisa ambil detail jika ingin notifikasi lebih spesifik
                            // generateNotificationsFromRejected(berkasArray.length()); // Jika hanya jumlah
                            // Atau bisa loop untuk notifikasi per berkas jika diperlukan
                            // Contoh: jika ada notifikasi "berkas X ditolak karena Y"
                        } else {
                            String message = jsonResponse.getString("message");
                            // Toast.makeText(getContext(), "Gagal memuat data berkas ditolak untuk notifikasi: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Toast.makeText(getContext(), "Error parsing JSON berkas ditolak untuk notifikasi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (getContext() == null) return;
                    // Toast.makeText(getContext(), "Error jaringan saat memuat berkas ditolak untuk notifikasi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        requestQueue.add(stringRequest);
    }


    // Metode untuk menghasilkan notifikasi berdasarkan statistik
    private void generateNotificationsFromStats(int newApplicants, int rejectedFiles) {
        notifikasiList.clear(); // Bersihkan notifikasi sebelumnya

        if (rejectedFiles > 0) {
            notifikasiList.add(new Notifikasi(
                    "Berkas Ditolak",
                    "Ada " + rejectedFiles + " berkas yang baru saja ditolak oleh Pemprov. Mohon periksa.",
                    R.drawable.ic_error, "red"
            ));
        }

        if (newApplicants > 0) {
            notifikasiList.add(new Notifikasi(
                    "Pendaftar Baru",
                    "Ada " + newApplicants + " pendaftar baru yang menunggu pengunggahan berkas.",
                    R.drawable.ic_person_add, "orange"
            ));
        }

        // Contoh notifikasi statis yang tetap bisa digabung jika perlu (misal: pengumuman umum)
        notifikasiList.add(new Notifikasi(
                "Deadline Beasiswa A",
                "Batas unggah berkas Beasiswa A adalah 30 Juni 2025. Pastikan semua berkas sudah diunggah.",
                R.drawable.ic_warning, "blue"
        ));
        notifikasiList.add(new Notifikasi(
                "Update Sistem",
                "Aplikasi telah diperbarui ke versi terbaru. Nikmati fitur baru!",
                R.drawable.ic_check_circle, "green"
        ));


        notifikasiAdapter.updateList(notifikasiList);
        updateEmptyNotifikasiState(notifikasiList.isEmpty());
    }


    @Override
    public void onNotifikasiClick(Notifikasi notifikasi) {
        if (getContext() == null) return;
        Toast.makeText(getContext(), "Notifikasi: " + notifikasi.getJudul() + "\n" + notifikasi.getDeskripsi(), Toast.LENGTH_LONG).show();
        // Navigasi berdasarkan jenis notifikasi
        if (notifikasi.getJudul().equals("Berkas Ditolak")) { // Sesuaikan judul jika berubah
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.nav_berkas_ditolak);
            }
        } else if (notifikasi.getJudul().equals("Pendaftar Baru")) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.nav_pendaftaran);
            }
        }
        // TODO: Tambahkan navigasi untuk notifikasi lain (misal: "Deadline Beasiswa" ke kalender/jadwal)
    }

    private void updateEmptyNotifikasiState(boolean isEmpty) {
        if (isEmpty) {
            tvEmptyNotifikasiState.setVisibility(View.VISIBLE);
            rvNotifikasi.setVisibility(View.GONE);
        } else {
            tvEmptyNotifikasiState.setVisibility(View.GONE);
            rvNotifikasi.setVisibility(View.VISIBLE);
        }
    }
}