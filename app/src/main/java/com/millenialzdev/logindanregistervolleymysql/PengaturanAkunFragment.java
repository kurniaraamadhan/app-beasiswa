package com.millenialzdev.logindanregistervolleymysql;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PengaturanAkunFragment extends Fragment {

    // URL API untuk update profil
    private static final String API_URL_UPDATE_PROFILE = "http://192.168.100.4/my_api_android/update_profile.php"; // Ganti dengan URL PHP kamu
    // URL API untuk mengambil data profil (jika dibutuhkan)
    private static final String API_URL_FETCH_PROFILE = "http://192.168.100.4/my_api_android/update_profile.php"; // Ganti dengan URL PHP kamu

    private ImageView ivProfilePhoto;
    private TextView tvUsername, tvRole, tvKampus; // Tambahkan tvKampus
    private Button btnGantiFotoProfil, btnUbahPassword, btnLogout;
    private TextInputEditText etPasswordLama, etPasswordBaru, etKonfirmasiPasswordBaru;
    private TextInputLayout tilPasswordLama, tilPasswordBaru, tilKonfirmasiPasswordBaru;

    private String loggedInUsername;
    private String loggedInIdUser; // Tambahkan id_user
    private String loggedInKampus; // Tambahkan kampus
    private String loggedInRole; // Tambahkan role
    private Uri selectedProfilePhotoUri;

    private RequestQueue requestQueue;
    private ActivityResultLauncher<Intent> photoPickerLauncher;

    public PengaturanAkunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            requestQueue = Volley.newRequestQueue(getContext());
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            loggedInUsername = sharedPreferences.getString("username", "Pengguna");
            loggedInIdUser = sharedPreferences.getString("id_user", ""); // Ambil id_user
            loggedInKampus = sharedPreferences.getString("kampus", "Tidak Diketahui"); // Ambil kampus
            loggedInRole = sharedPreferences.getString("role", "android"); // Ambil role

            // Inisialisasi ActivityResultLauncher untuk pemilihan foto
            photoPickerLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            selectedProfilePhotoUri = result.getData().getData();
                            if (selectedProfilePhotoUri != null) {
                                // Tampilkan foto yang dipilih di ImageView
                                Glide.with(this).load(selectedProfilePhotoUri).into(ivProfilePhoto);
                                Toast.makeText(getContext(), "Foto dipilih. Klik 'Ganti Foto Profil' untuk mengunggah.", Toast.LENGTH_LONG).show();
                                // Langsung unggah foto setelah dipilih
                                unggahFotoProfil();
                            }
                        } else {
                            Toast.makeText(getContext(), "Pemilihan foto dibatalkan.", Toast.LENGTH_SHORT).show();
                            selectedProfilePhotoUri = null;
                        }
                    }
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengaturan_akun, container, false);

        ivProfilePhoto = view.findViewById(R.id.iv_profile_photo);
        tvUsername = view.findViewById(R.id.tv_username);
        tvRole = view.findViewById(R.id.tv_role);
        tvKampus = view.findViewById(R.id.tv_kampus); // Inisialisasi tvKampus

        btnGantiFotoProfil = view.findViewById(R.id.btn_ganti_foto_profil);
        btnUbahPassword = view.findViewById(R.id.btn_ubah_password);
        btnLogout = view.findViewById(R.id.btn_logout);

        etPasswordLama = view.findViewById(R.id.et_password_lama);
        etPasswordBaru = view.findViewById(R.id.et_password_baru);
        etKonfirmasiPasswordBaru = view.findViewById(R.id.et_konfirmasi_password_baru);
        tilPasswordLama = view.findViewById(R.id.til_password_lama);
        tilPasswordBaru = view.findViewById(R.id.til_password_baru);
        tilKonfirmasiPasswordBaru = view.findViewById(R.id.til_konfirmasi_password_baru);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername.setText(loggedInUsername);
        tvRole.setText("Role: " + loggedInRole);
        tvKampus.setText("Kampus: " + loggedInKampus); // Set teks untuk kampus

        // Ambil URL foto profil dari SharedPreferences dan tampilkan
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String profilePhotoUrl = sharedPreferences.getString("profile_photo_url", "");
        if (!profilePhotoUrl.isEmpty() && !profilePhotoUrl.equals("null")) {
            Glide.with(this).load(profilePhotoUrl).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(ivProfilePhoto);
        } else {
            ivProfilePhoto.setImageResource(R.drawable.ic_default_profile);
        }

        btnGantiFotoProfil.setOnClickListener(v -> chooseProfilePhoto()); // Klik tombol ganti foto profil
        ivProfilePhoto.setOnClickListener(v -> chooseProfilePhoto()); // Klik foto juga bisa pilih foto
        btnUbahPassword.setOnClickListener(v -> ubahPassword());
        btnLogout.setOnClickListener(v -> logout());
    }

    // --- Metode untuk Fungsionalitas ---

    // Metode untuk memilih foto profil
    private void chooseProfilePhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Hanya izinkan gambar
        photoPickerLauncher.launch(Intent.createChooser(intent, "Pilih Foto Profil"));
    }

    // Helper method untuk mendapatkan File dari Uri (sama seperti di BerkasMahasiswaFragment)
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
            if (getContext() != null) Toast.makeText(getContext(), "Gagal menyiapkan foto untuk unggah: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    // Helper method untuk mendapatkan nama file dari Uri (sama seperti di BerkasMahasiswaFragment)
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


    // Metode untuk mengunggah foto profil
    private void unggahFotoProfil() {
        if (selectedProfilePhotoUri == null) {
            // Ini tidak akan terjadi jika chooseProfilePhoto dipanggil duluan
            if (getContext() != null) Toast.makeText(getContext(), "Pilih foto profil terlebih dahulu.", Toast.LENGTH_SHORT).show();
            return;
        }

        File fileToUpload = getFileFromUri(selectedProfilePhotoUri);
        if (fileToUpload == null) {
            if (getContext() != null) Toast.makeText(getContext(), "Gagal membaca file foto.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> stringParts = new HashMap<>();
        stringParts.put("id_user", loggedInIdUser); // Kirim id_user untuk identifikasi di PHP
        stringParts.put("username", loggedInUsername); // Juga kirim username

        MultipartRequest multipartRequest = new MultipartRequest(API_URL_UPDATE_PROFILE,
                response -> {
                    if (getContext() == null) return;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                        if (success) {
                            String photoUrl = jsonResponse.optString("url_foto_profil", "");
                            if (!photoUrl.isEmpty() && !photoUrl.equals("null")) {
                                Glide.with(this).load(photoUrl).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(ivProfilePhoto);
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("profile_photo_url", photoUrl);
                                editor.apply();
                            } else { // Jika URL kosong/null dari server, tampilkan default
                                ivProfilePhoto.setImageResource(R.drawable.ic_default_profile);
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("profile_photo_url"); // Hapus dari shared prefs
                                editor.apply();
                            }
                            selectedProfilePhotoUri = null; // Reset setelah unggah berhasil
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing response foto profil: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    if (getContext() == null) return;
                    Toast.makeText(getContext(), "Error unggah foto: " + error.getMessage(), Toast.LENGTH_LONG).show();
                },
                fileToUpload,
                "file_profile_photo", // Nama field untuk file di PHP
                stringParts);

        if (requestQueue != null) {
            requestQueue.add(multipartRequest);
        } else {
            if (getContext() != null) Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
        }
    }


    // Metode untuk mengubah password
    private void ubahPassword() {
        String passwordLama = etPasswordLama.getText().toString().trim();
        String passwordBaru = etPasswordBaru.getText().toString().trim();
        String konfirmasiPasswordBaru = etKonfirmasiPasswordBaru.getText().toString().trim();

        // Reset error
        tilPasswordLama.setError(null);
        tilPasswordBaru.setError(null);
        tilKonfirmasiPasswordBaru.setError(null);

        boolean isValid = true;

        if (passwordLama.isEmpty()) { tilPasswordLama.setError("Password lama tidak boleh kosong"); isValid = false; }
        if (passwordBaru.isEmpty()) { tilPasswordBaru.setError("Password baru tidak boleh kosong"); isValid = false; }
        if (konfirmasiPasswordBaru.isEmpty()) { tilKonfirmasiPasswordBaru.setError("Konfirmasi password baru tidak boleh kosong"); isValid = false; }

        if (!passwordBaru.equals(konfirmasiPasswordBaru)) {
            tilKonfirmasiPasswordBaru.setError("Konfirmasi password tidak cocok dengan password baru");
            isValid = false;
        }
        if (passwordBaru.length() < 6) { // Contoh validasi panjang password
            tilPasswordBaru.setError("Password baru minimal 6 karakter");
            isValid = false;
        }

        if (isValid) {
            if (requestQueue == null || getContext() == null) {
                if (getContext() != null) Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL_UPDATE_PROFILE,
                    response -> {
                        if (getContext() == null) return;
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                            if (success) {
                                etPasswordLama.setText("");
                                etPasswordBaru.setText("");
                                etKonfirmasiPasswordBaru.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing response password: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        if (getContext() == null) return;
                        Toast.makeText(getContext(), "Error jaringan ubah password: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_user", loggedInIdUser); // Kirim id_user untuk identifikasi
                    params.put("username", loggedInUsername); // Kirim username juga
                    params.put("password_lama", passwordLama);
                    params.put("password_baru", passwordBaru);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        } else {
            if (getContext() != null) Toast.makeText(getContext(), "Mohon periksa input password.", Toast.LENGTH_SHORT).show();
        }
    }

    // Metode untuk Logout
    private void logout() {
        if (getContext() == null) return;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Hapus semua data di SharedPreferences
        editor.apply();

        Toast.makeText(getContext(), "Anda telah logout.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), Login.class); // Ganti Login.class
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Hapus semua activity sebelumnya dari stack
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish(); // Tutup MainActivity
        }
    }
}