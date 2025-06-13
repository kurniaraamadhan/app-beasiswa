package com.millenialzdev.logindanregistervolleymysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
                    Log.d("LOGIN_DEBUG", "Login URL: " + url);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("LOGIN_DEBUG", "Raw Response: " + response);
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");

                                        if (success) {
                                            String message = jsonResponse.getString("message");
                                            String loggedInUser = jsonResponse.getString("username");
                                            String userId = jsonResponse.getString("id_user");
                                            String kampus = jsonResponse.getString("kampus"); // Ambil kampus
                                            String role = jsonResponse.getString("role"); // Ambil role
                                            String fotoProfilUrl = jsonResponse.optString("foto_profile_url", "");

                                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username", loggedInUser);
                                            editor.putString("id_user", userId);
                                            editor.putString("kampus", kampus); // Simpan kampus
                                            editor.putString("role", role); // Simpan role
                                            editor.putString("profile_photo_url", fotoProfilUrl);
                                            editor.apply();

                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        } else {
                                            String message = jsonResponse.getString("message");
                                            Toast.makeText(getApplicationContext(), "Login Gagal: " + message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Error parsing login response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LOGIN_DEBUG", "Volley Error: " + error.toString());
                                    if (error.networkResponse != null) {
                                        Log.e("LOGIN_DEBUG", "Status Code: " + error.networkResponse.statusCode);
                                        String responseData = new String(error.networkResponse.data);
                                        Log.e("LOGIN_DEBUG", "Error Response Data: " + responseData);
                                    }
                                    Toast.makeText(getApplicationContext(), "Error jaringan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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