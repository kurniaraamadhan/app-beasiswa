package com.millenialzdev.logindanregistervolleymysql;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast; // Untuk contoh toast saat klik

public class HomeFragment extends Fragment {

    private TextView welcomeText;
    private TextView currentDateText;
    private TextView tvNewApplicants, tvPendingFiles, tvApprovedFiles, tvTotalStudents;
    private CardView cardPendaftarBaru, cardKelolaBerkas, cardDaftarMahasiswa, cardPengaturanAkun;
    private TextView tvAnnouncement;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi Views
        welcomeText = view.findViewById(R.id.welcome_text);
        currentDateText = view.findViewById(R.id.current_date_text);
        tvNewApplicants = view.findViewById(R.id.tv_new_applicants);
        tvPendingFiles = view.findViewById(R.id.tv_pending_files);
        tvApprovedFiles = view.findViewById(R.id.tv_approved_files);
        tvTotalStudents = view.findViewById(R.id.tv_total_students);
        tvAnnouncement = view.findViewById(R.id.tv_announcement);

        cardPendaftarBaru = view.findViewById(R.id.card_pendaftar_baru);
        cardKelolaBerkas = view.findViewById(R.id.card_kelola_berkas);
        cardDaftarMahasiswa = view.findViewById(R.id.card_daftar_mahasiswa);
        cardPengaturanAkun = view.findViewById(R.id.card_pengaturan_akun);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Atur Data & Fungsionalitas ---

        // Set Tanggal Hari Ini
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID")); // Format tanggal Indonesia
        String currentDate = sdf.format(new Date());
        currentDateText.setText(currentDate);

        // Contoh data statistik (Nantinya ini bisa diambil dari database/API)
        tvNewApplicants.setText("15"); // Contoh: Jumlah pendaftar baru
        tvPendingFiles.setText("3");   // Contoh: Berkas yang masih pending
        tvApprovedFiles.setText("120"); // Contoh: Total berkas disetujui
        tvTotalStudents.setText("350"); // Contoh: Total mahasiswa terdaftar

        // Contoh Notifikasi
        // tvAnnouncement.setText("Batas akhir pengajuan berkas beasiswa tahap 2 adalah 30 Juni 2025.");


        // --- Atur Listener untuk Quick Actions ---
        cardPendaftarBaru.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Membuka Pendaftar Baru...", Toast.LENGTH_SHORT).show();
            // TODO: Tambahkan navigasi ke PendaftaranFragment atau Activity Pendaftar Baru
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.nav_pendaftaran);
            }
        });

        cardKelolaBerkas.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Membuka Kelola Berkas...", Toast.LENGTH_SHORT).show();
            // TODO: Tambahkan navigasi ke BerkasMahasiswaFragment
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.nav_berkas_mahasiswa);
            }
        });

        cardDaftarMahasiswa.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Membuka Daftar Mahasiswa...", Toast.LENGTH_SHORT).show();
            // TODO: Tambahkan navigasi ke DataDiriFragment
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.nav_data_diri);
            }
        });

        cardPengaturanAkun.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Membuka Pengaturan Akun...", Toast.LENGTH_SHORT).show();
            // TODO: Tambahkan navigasi ke Fragment/Activity Pengaturan
            // Misalnya: Jika ada Fragment terpisah untuk pengaturan akun staff TU.
        });
    }
}