package com.millenialzdev.logindanregistervolleymysql;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1001;

    private Button buttonPilihFile, buttonUploadBerkas;
    private TextView textNamaFile;
    private Uri fileUri;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        buttonPilihFile = findViewById(R.id.button_pilih_file);
        buttonUploadBerkas = findViewById(R.id.button_upload_berkas);
        textNamaFile = findViewById(R.id.text_nama_file);

        buttonPilihFile.setOnClickListener(v -> openFilePicker());

        buttonUploadBerkas.setOnClickListener(v -> {
            if (fileUri != null) {
                uploadFile(fileUri);
            } else {
                Toast.makeText(this, "Pilih file terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Semua tipe file, bisa diubah misal "application/pdf"
        startActivityForResult(Intent.createChooser(intent, "Pilih Berkas"), PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            fileUri = data.getData();

            // Ambil nama file
            fileName = getFileName(fileUri);
            textNamaFile.setText(fileName);
            buttonUploadBerkas.setEnabled(true);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void uploadFile(Uri uri) {
        // Simpan sementara file ke cache untuk bisa dibaca OkHttp
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File tempFile = new File(getCacheDir(), fileName);
            OutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buf = new byte[8192];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            // Upload pakai OkHttp
            OkHttpClient client = new OkHttpClient();

            RequestBody fileBody = RequestBody.create(tempFile, MediaType.parse(getContentResolver().getType(uri)));
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName, fileBody)
                    // Kalau perlu kirim username juga
                    .addFormDataPart("username", "username_android") // ganti sesuai user login
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.100.4/my_api_android/api-upload.php") // Ganti url server kamu
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, java.io.IOException e) {
                    runOnUiThread(() -> Toast.makeText(UploadActivity.this, "Upload gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws java.io.IOException {
                    String resp = response.body().string();
                    runOnUiThread(() -> Toast.makeText(UploadActivity.this, "Response: " + resp, Toast.LENGTH_LONG).show());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error upload file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
