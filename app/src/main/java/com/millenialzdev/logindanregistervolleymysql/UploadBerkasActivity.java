package com.millenialzdev.logindanregistervolleymysql;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UploadBerkasActivity extends AppCompatActivity {

    private EditText editNamaBerkas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_berkas);

        editNamaBerkas = findViewById(R.id.edit_nama_berkas);
        Button buttonUpload = findViewById(R.id.button_upload_berkas);

        buttonUpload.setOnClickListener(v -> {
            String namaBerkas = editNamaBerkas.getText().toString().trim();
            if (!namaBerkas.isEmpty()) {
                Toast.makeText(this, "Berkas '" + namaBerkas + "' berhasil di-upload!", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke MainActivity
            } else {
                Toast.makeText(this, "Nama berkas tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
