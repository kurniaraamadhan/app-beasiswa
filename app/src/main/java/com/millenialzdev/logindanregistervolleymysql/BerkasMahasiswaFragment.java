package com.millenialzdev.logindanregistervolleymysql;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BerkasMahasiswaFragment extends Fragment {

    private TextInputLayout tilSearchMahasiswa;
    private TextInputEditText etSearchMahasiswa;
    private TextView tvSelectedMahasiswaInfo;
    private Spinner spinnerJenisBerkas;
    private Button btnPilihBerkas, btnUnggahBerkas;
    private TextView tvNamaFile;

    private Uri selectedFileUri; // URI dari file yang dipilih
    private String selectedMahasiswaNIM; // NIM mahasiswa yang dipilih

    // Launcher untuk memilih file
    private ActivityResultLauncher<Intent> filePickerLauncher;

    public BerkasMahasiswaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berkas_mahasiswa, container, false);

        // Inisialisasi Views
        tilSearchMahasiswa = view.findViewById(R.id.til_search_mahasiswa_upload);
        etSearchMahasiswa = view.findViewById(R.id.et_search_mahasiswa_upload);
        tvSelectedMahasiswaInfo = view.findViewById(R.id.tv_selected_mahasiswa_info);
        spinnerJenisBerkas = view.findViewById(R.id.spinner_jenis_berkas);
        btnPilihBerkas = view.findViewById(R.id.btn_pilih_berkas);
        btnUnggahBerkas = view.findViewById(R.id.btn_unggah_berkas);
        tvNamaFile = view.findViewById(R.id.tv_nama_file);

        // Siapkan Spinner untuk Jenis Berkas
        setupJenisBerkasSpinner();

        // Inisialisasi ActivityResultLauncher
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedFileUri = result.getData().getData();
                        if (selectedFileUri != null) {
                            tvNamaFile.setText("File Terpilih: " + getFileName(selectedFileUri));
                        } else {
                            tvNamaFile.setText("File Terpilih: Gagal mengambil file");
                        }
                    } else {
                        tvNamaFile.setText("File Terpilih: Belum ada file");
                        selectedFileUri = null;
                    }
                }
        );

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Listener untuk mencari mahasiswa
        tilSearchMahasiswa.setEndIconOnClickListener(v -> searchMahasiswa());
        etSearchMahasiswa.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                searchMahasiswa();
                return true;
            }
            return false;
        });

        // Listener untuk tombol Pilih Berkas
        btnPilihBerkas.setOnClickListener(v -> openFilePicker());

        // Listener untuk tombol Unggah Berkas
        btnUnggahBerkas.setOnClickListener(v -> unggahBerkas());
    }

    private void setupJenisBerkasSpinner() {
        List<String> jenisBerkasList = new ArrayList<>();
        jenisBerkasList.add("Pilih Jenis Berkas"); // Placeholder
        jenisBerkasList.add("Transkrip Nilai");
        jenisBerkasList.add("Surat Keterangan Aktif Kuliah");
        jenisBerkasList.add("KHS Semester Ganjil");
        jenisBerkasList.add("Surat Pernyataan Tidak Mampu");
        jenisBerkasList.add("Sertifikat Prestasi Akademik");
        jenisBerkasList.add("Kartu Keluarga");
        // Tambahkan jenis berkas lainnya sesuai kebutuhan

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, jenisBerkasList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisBerkas.setAdapter(adapter);
    }

    private void searchMahasiswa() {
        String query = etSearchMahasiswa.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(getContext(), "Masukkan NIM atau Nama Mahasiswa", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implementasi logika pencarian mahasiswa dari database/API
        // Ini adalah contoh dummy, nantinya ganti dengan pencarian yang sebenarnya
        if (query.equalsIgnoreCase("2023001") || query.equalsIgnoreCase("Andi Wijaya")) {
            selectedMahasiswaNIM = "2023001";
            tvSelectedMahasiswaInfo.setText("Mahasiswa Terpilih: Andi Wijaya (2023001)");
            Toast.makeText(getContext(), "Mahasiswa ditemukan: Andi Wijaya", Toast.LENGTH_SHORT).show();
        } else {
            selectedMahasiswaNIM = null;
            tvSelectedMahasiswaInfo.setText("Mahasiswa Terpilih: - (Tidak ditemukan)");
            Toast.makeText(getContext(), "Mahasiswa tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Mengizinkan semua jenis file
        // intent.setType("application/pdf"); // Hanya mengizinkan PDF
        // intent.setType("image/*"); // Hanya mengizinkan gambar
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Pilih File Berkas"));
    }

    private String getFileName(Uri uri) {
        // Metode sederhana untuk mendapatkan nama file dari URI
        // Mungkin perlu penanganan yang lebih kompleks untuk beberapa jenis URI
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void unggahBerkas() {
        String jenisBerkas = spinnerJenisBerkas.getSelectedItem().toString();

        // Validasi
        if (selectedMahasiswaNIM == null || selectedMahasiswaNIM.isEmpty()) {
            Toast.makeText(getContext(), "Mohon cari dan pilih mahasiswa terlebih dahulu.", Toast.LENGTH_LONG).show();
            return;
        }
        if (jenisBerkas.equals("Pilih Jenis Berkas")) {
            Toast.makeText(getContext(), "Mohon pilih jenis berkas.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedFileUri == null) {
            Toast.makeText(getContext(), "Mohon pilih file berkas yang akan diunggah.", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implementasi logika unggah berkas ke server atau simpan ke database lokal
        // Di sini kamu akan mengirim selectedFileUri dan jenisBerkas ke backend
        // Misalnya menggunakan Retrofit untuk REST API atau Firebase Storage
        Toast.makeText(getContext(), "Mengunggah berkas '" + jenisBerkas + "' untuk NIM " + selectedMahasiswaNIM + "...", Toast.LENGTH_LONG).show();

        // Contoh: Simulasi unggah berhasil
        new android.os.Handler().postDelayed(() -> {
            Toast.makeText(getContext(), "Berkas berhasil diunggah!", Toast.LENGTH_SHORT).show();
            resetForm(); // Reset form setelah unggah berhasil
        }, 2000); // Simulasi delay 2 detik
    }

    private void resetForm() {
        etSearchMahasiswa.setText("");
        tvSelectedMahasiswaInfo.setText("Mahasiswa Terpilih: -");
        spinnerJenisBerkas.setSelection(0); // Kembali ke "Pilih Jenis Berkas"
        tvNamaFile.setText("File Terpilih: Belum ada file");
        selectedFileUri = null;
        selectedMahasiswaNIM = null;
    }
}