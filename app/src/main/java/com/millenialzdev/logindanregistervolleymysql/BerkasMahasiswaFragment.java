package com.millenialzdev.logindanregistervolleymysql;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context; // Import ini
import android.content.Intent;
import android.content.SharedPreferences; // Import ini
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BerkasMahasiswaFragment extends Fragment implements SelectedFilesAdapter.OnItemRemoveListener {

    private static final String API_URL_UPLOAD_BERKAS = "http://192.168.100.4/my_api_android/upload_berkas.php";
    private static final String API_URL_MAHASISWA_SEARCH = "http://192.168.100.4/my_api_android/mahasiswa_crud.php";


    private TextInputLayout tilSearchMahasiswa;
    private TextInputEditText etSearchMahasiswa;
    private TextView tvSelectedMahasiswaInfo;
    private Spinner spinnerJenisBerkas;
    private Button btnPilihBerkas, btnUnggahBerkas, btnClearSelectedFiles;
    private TextView tvNoFilesSelected;
    private RecyclerView rvSelectedFiles;

    private SelectedFilesAdapter selectedFilesAdapter;
    private List<SelectedFile> selectedFilesList;

    private String selectedMahasiswaNIM;
    private String selectedMahasiswaNama;
    private String loggedInKampus; // <--- BARU: Untuk menyimpan kampus Staff TU
    private String loggedInRole; // <--- BARU: Untuk menyimpan role Staff TU (developer bisa lihat semua)


    private ActivityResultLauncher<Intent> filePickerLauncher;
    private RequestQueue requestQueue;

    public BerkasMahasiswaFragment() {
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

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        if (result.getData().getClipData() != null) {
                            ClipData clipData = result.getData().getClipData();
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri uri = clipData.getItemAt(i).getUri();
                                if (uri != null) {
                                    selectedFilesList.add(new SelectedFile(getFileName(uri), uri));
                                }
                            }
                        } else if (result.getData().getData() != null) {
                            Uri uri = result.getData().getData();
                            if (uri != null) {
                                selectedFilesList.add(new SelectedFile(getFileName(uri), uri));
                            }
                        }
                        updateSelectedFilesUI();
                    } else {
                        Toast.makeText(getContext(), "Pemilihan file dibatalkan.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berkas_mahasiswa, container, false);

        tilSearchMahasiswa = view.findViewById(R.id.til_search_mahasiswa_upload);
        etSearchMahasiswa = view.findViewById(R.id.et_search_mahasiswa_upload);
        tvSelectedMahasiswaInfo = view.findViewById(R.id.tv_selected_mahasiswa_info);
        spinnerJenisBerkas = view.findViewById(R.id.spinner_jenis_berkas);
        btnPilihBerkas = view.findViewById(R.id.btn_pilih_berkas);
        btnUnggahBerkas = view.findViewById(R.id.btn_unggah_berkas);
        btnClearSelectedFiles = view.findViewById(R.id.btn_clear_selected_files);
        tvNoFilesSelected = view.findViewById(R.id.tv_no_files_selected);
        rvSelectedFiles = view.findViewById(R.id.rv_selected_files);

        setupJenisBerkasSpinner();

        selectedFilesList = new ArrayList<>();
        selectedFilesAdapter = new SelectedFilesAdapter(selectedFilesList, this);
        rvSelectedFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSelectedFiles.setAdapter(selectedFilesAdapter);
        updateSelectedFilesUI();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilSearchMahasiswa.setEndIconOnClickListener(v -> searchMahasiswa());
        etSearchMahasiswa.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                searchMahasiswa();
                return true;
            }
            return false;
        });

        btnPilihBerkas.setOnClickListener(v -> openFilePicker());
        btnUnggahBerkas.setOnClickListener(v -> unggahBerkas());
        btnClearSelectedFiles.setOnClickListener(v -> clearSelectedFiles());
    }

    private void setupJenisBerkasSpinner() {
        List<String> jenisBerkasList = new ArrayList<>();
        jenisBerkasList.add("Pilih Jenis Berkas");
        jenisBerkasList.add("Transkrip Nilai");
        jenisBerkasList.add("Surat Keterangan Aktif Kuliah");
        jenisBerkasList.add("KHS Semester Ganjil");
        jenisBerkasList.add("Surat Pernyataan Tidak Mampu");
        jenisBerkasList.add("Sertifikat Prestasi Akademik");
        jenisBerkasList.add("Kartu Keluarga");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
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

        // Kirim parameter kampus dan role
        String url = API_URL_MAHASISWA_SEARCH + "?nim=" + Uri.encode(query) +
                "&kampus=" + Uri.encode(loggedInKampus) +
                "&role=" + Uri.encode(loggedInRole);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (getContext() == null) return;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            JSONArray mahasiswaArray = jsonResponse.getJSONArray("mahasiswa");
                            if (mahasiswaArray.length() > 0) {
                                JSONObject mhsObject = mahasiswaArray.getJSONObject(0);
                                selectedMahasiswaNIM = mhsObject.getString("nim");
                                selectedMahasiswaNama = mhsObject.getString("nama_lengkap");
                                tvSelectedMahasiswaInfo.setText("Mahasiswa Terpilih: " + selectedMahasiswaNama + " (" + selectedMahasiswaNIM + ")");
                                Toast.makeText(getContext(), "Mahasiswa ditemukan: " + selectedMahasiswaNama, Toast.LENGTH_SHORT).show();
                            } else {
                                selectedMahasiswaNIM = null;
                                selectedMahasiswaNama = null;
                                tvSelectedMahasiswaInfo.setText("Mahasiswa Terpilih: - (Tidak ditemukan di kampus Anda)"); // Pesan lebih spesifik
                                Toast.makeText(getContext(), "Mahasiswa tidak ditemukan di kampus Anda.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), "Gagal mencari mahasiswa: " + message, Toast.LENGTH_SHORT).show();
                            selectedMahasiswaNIM = null;
                            selectedMahasiswaNama = null;
                            tvSelectedMahasiswaInfo.setText("Mahasiswa Terpilih: - (Error)");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    if (getContext() == null) return;
                    Toast.makeText(getContext(), "Error jaringan saat mencari mahasiswa: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });
        requestQueue.add(stringRequest);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Pilih File Berkas (Bisa Lebih Dari 1)"));
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
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

    private File getFileFromUri(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File file = new File(requireContext().getCacheDir(), getFileName(uri));
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            if (getContext() != null) Toast.makeText(getContext(), "Gagal menyiapkan file untuk unggah: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void updateSelectedFilesUI() {
        if (selectedFilesList.isEmpty()) {
            rvSelectedFiles.setVisibility(View.GONE);
            btnClearSelectedFiles.setVisibility(View.GONE);
            tvNoFilesSelected.setVisibility(View.VISIBLE);
        } else {
            rvSelectedFiles.setVisibility(View.VISIBLE);
            btnClearSelectedFiles.setVisibility(View.VISIBLE);
            tvNoFilesSelected.setVisibility(View.GONE);
            selectedFilesAdapter.updateList(selectedFilesList);
        }
    }

    private void clearSelectedFiles() {
        selectedFilesList.clear();
        updateSelectedFilesUI();
        Toast.makeText(getContext(), "Semua file terpilih telah dihapus.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveClick(int position) {
        selectedFilesList.remove(position);
        updateSelectedFilesUI();
        Toast.makeText(getContext(), "File dihapus.", Toast.LENGTH_SHORT).show();
    }


    private void unggahBerkas() {
        String jenisBerkas = spinnerJenisBerkas.getSelectedItem().toString();

        if (selectedMahasiswaNIM == null || selectedMahasiswaNIM.isEmpty()) {
            Toast.makeText(getContext(), "Mohon cari dan pilih mahasiswa terlebih dahulu.", Toast.LENGTH_LONG).show();
            return;
        }
        if (jenisBerkas.equals("Pilih Jenis Berkas")) {
            Toast.makeText(getContext(), "Mohon pilih jenis berkas.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedFilesList.isEmpty()) {
            Toast.makeText(getContext(), "Mohon pilih setidaknya satu file berkas yang akan diunggah.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loggedInKampus.isEmpty()) { // <--- BARU: Validasi kampus
            Toast.makeText(getContext(), "Informasi kampus tidak ditemukan. Mohon login ulang.", Toast.LENGTH_LONG).show();
            return;
        }

        btnUnggahBerkas.setEnabled(false);
        Toast.makeText(getContext(), "Memulai proses unggah " + selectedFilesList.size() + " berkas...", Toast.LENGTH_SHORT).show();

        for (int i = 0; i < selectedFilesList.size(); i++) {
            SelectedFile selectedFile = selectedFilesList.get(i);
            File fileToUpload = getFileFromUri(selectedFile.getFileUri());

            if (fileToUpload == null) {
                Toast.makeText(getContext(), "Gagal membaca file: " + selectedFile.getFileName(), Toast.LENGTH_SHORT).show();
                btnUnggahBerkas.setEnabled(true);
                return;
            }

            Map<String, String> stringParts = new HashMap<>();
            stringParts.put("nim_mahasiswa", selectedMahasiswaNIM);
            stringParts.put("jenis_berkas", jenisBerkas + " (" + selectedFile.getFileName() + ")");
            stringParts.put("tanggal_upload", new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID")).format(new Date()));
            stringParts.put("kampus_pengunggah", loggedInKampus); // <--- BARU: Kirim kampus pengunggah

            final int fileIndex = i;
            MultipartRequest multipartRequest = new MultipartRequest(API_URL_UPLOAD_BERKAS,
                    response -> {
                        if (getContext() == null) return;
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                Toast.makeText(getContext(), "File " + selectedFile.getFileName() + " berhasil diunggah.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "File " + selectedFile.getFileName() + " gagal diunggah: " + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing response untuk " + selectedFile.getFileName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (fileIndex == selectedFilesList.size() - 1) {
                                btnUnggahBerkas.setEnabled(true);
                                clearSelectedFiles();
                                Toast.makeText(getContext(), "Proses unggah selesai.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    error -> {
                        if (getContext() == null) return;
                        Toast.makeText(getContext(), "Error jaringan untuk " + selectedFile.getFileName() + ": " + error.getMessage(), Toast.LENGTH_LONG).show();
                        if (fileIndex == selectedFilesList.size() - 1) {
                            btnUnggahBerkas.setEnabled(true);
                        }
                    },
                    fileToUpload,
                    "file_berkas",
                    stringParts);

            if (requestQueue != null) {
                requestQueue.add(multipartRequest);
            } else {
                if (getContext() != null) Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void resetForm() {
        etSearchMahasiswa.setText("");
        tvSelectedMahasiswaInfo.setText("Mahasiswa Terpilih: -");
        spinnerJenisBerkas.setSelection(0);
        clearSelectedFiles();
        selectedMahasiswaNIM = null;
        selectedMahasiswaNama = null;
    }
}