package com.millenialzdev.logindanregistervolleymysql;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailBerkasActivity extends AppCompatActivity {

    private TextView textDetailBerkas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_berkas);

        textDetailBerkas = findViewById(R.id.text_detail_berkas);

        String namaBerkas = getIntent().getStringExtra("nama_berkas");
        if (namaBerkas != null) {
            textDetailBerkas.setText(namaBerkas);
        }
    }
}
