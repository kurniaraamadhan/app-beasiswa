package com.millenialzdev.logindanregistervolleymysql;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// Nama kelas harus sesuai dengan nama file: BerkasDitolakDetailFragment
public class BerkasDitolakDetailFragment extends Fragment {

    private static final String ARG_BERKAS_DITOLAK = "berkas_ditolak_object";

    private Berkas berkasDitolak;
    private Pendaftar terkaitPendaftar;

    private TextView tvDetailBerkasNIM, tvDetailBerkasJenis, tvDetailBerkasStatus, tvDetailBerkasAlasanDitolak;
    private TextView tvDetailMahasiswaNamaBerkas, tvDetailMahasiswaProdiBerkas;
    private Button btnLihatBerkasAsli, btnKembaliKeDaftarDitolak;

    // Dummy list pendaftar untuk simulasi pencarian
    private List<Pendaftar> dummyPendaftarList;


    public BerkasDitolakDetailFragment() {
        // Required empty public constructor
    }

    public static BerkasDitolakDetailFragment newInstance(Berkas berkas) {
        BerkasDitolakDetailFragment fragment = new BerkasDitolakDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BERKAS_DITOLAK, berkas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            berkasDitolak = getArguments().getParcelable(ARG_BERKAS_DITOLAK);
        }

        // Inisialisasi dummy pendaftar list (pastikan sama dengan di BerkasDitolakFragment)
        dummyPendaftarList = new ArrayList<>();
        // PASTIKAN SEMUA PARAMETER ADA SESUAI DENGAN Pendaftar.java (12 parameter)
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

        // Cari pendaftar terkait
        if (berkasDitolak != null) {
            for (Pendaftar p : dummyPendaftarList) {
                if (p.getNim().equals(berkasDitolak.getNim())) {
                    terkaitPendaftar = p;
                    break;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berkas_ditolak_detail, container, false);

        tvDetailBerkasNIM = view.findViewById(R.id.tv_detail_berkas_nim);
        tvDetailBerkasJenis = view.findViewById(R.id.tv_detail_berkas_jenis);
        tvDetailBerkasStatus = view.findViewById(R.id.tv_detail_berkas_status);
        tvDetailBerkasAlasanDitolak = view.findViewById(R.id.tv_detail_berkas_alasan_ditolak);
        tvDetailMahasiswaNamaBerkas = view.findViewById(R.id.tv_detail_mahasiswa_nama_berkas);
        tvDetailMahasiswaProdiBerkas = view.findViewById(R.id.tv_detail_mahasiswa_prodi_berkas);
        btnLihatBerkasAsli = view.findViewById(R.id.btn_lihat_berkas_asli);
        btnKembaliKeDaftarDitolak = view.findViewById(R.id.btn_kembali_ke_daftar_ditolak);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (berkasDitolak != null) {
            tvDetailBerkasNIM.setText("NIM Mahasiswa: " + berkasDitolak.getNim());
            tvDetailBerkasJenis.setText("Jenis Berkas: " + berkasDitolak.getJenisBerkas());
            tvDetailBerkasStatus.setText("Status Verifikasi: " + berkasDitolak.getStatus());
            tvDetailBerkasAlasanDitolak.setText("Alasan Penolakan: " + berkasDitolak.getAlasanDitolak());

            if (terkaitPendaftar != null) {
                tvDetailMahasiswaNamaBerkas.setText("Nama Mahasiswa: " + terkaitPendaftar.getNama());
                tvDetailMahasiswaProdiBerkas.setText("Program Studi: " + terkaitPendaftar.getProgramStudi());
            } else {
                tvDetailMahasiswaNamaBerkas.setText("Nama Mahasiswa: Tidak ditemukan");
                tvDetailMahasiswaProdiBerkas.setText("Program Studi: -");
            }

            btnLihatBerkasAsli.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Membuka berkas asli (simulasi)", Toast.LENGTH_SHORT).show();
                openWebLink("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf");
            });

            btnKembaliKeDaftarDitolak.setOnClickListener(v -> {
                getParentFragmentManager().popBackStack();
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).setToolbarTitle("Berkas Ditolak");
                }
            });

        } else {
            Toast.makeText(getContext(), "Detail berkas tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebLink(String url) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Tidak dapat membuka berkas. URL tidak valid atau tidak ada aplikasi penampil.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}