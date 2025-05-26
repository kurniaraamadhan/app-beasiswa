package com.millenialzdev.logindanregistervolleymysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Login extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {

                    String url = Db_Contract.urlLogin;

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("Selamat Datang")) {
                                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Gagal: " + response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected java.util.Map<String, String> getParams() {
                            java.util.Map<String, String> params = new java.util.HashMap<>();
                            params.put("username", username);
                            params.put("password", password);
                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

                } else {
                    Toast.makeText(getApplicationContext(), "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}