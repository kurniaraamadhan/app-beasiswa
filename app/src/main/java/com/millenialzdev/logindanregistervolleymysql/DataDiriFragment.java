package com.millenialzdev.logindanregistervolleymysql;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataDiriFragment extends Fragment {

    private static final String ARG_PENDAFTAR_EDIT = "pendaftar_for_edit";
    private static final String API_URL_MAHASISWA = "http://192.168.100.4/my_api_android/mahasiswa_crud.php";

    private TextInputEditText etNamaLengkap, etNIM, etTanggalLahir,
            etNomorTelepon, etEmail, etAlamat,
            etProgramStudi, etAngkatan, etIPK;
    private TextInputLayout tilNamaLengkap, tilNIM, tilTanggalLahir,
            tilNomorTelepon, tilEmail, tilAlamat,
            tilProgramStudi, tilAngkatan, tilIPK;
    private RadioGroup rgJenisKelamin;
    private RadioButton rbLakiLaki, rbPerempuan;
    private Button btnSimpanData;

    private Calendar calendar;
    private Pendaftar pendaftarToEdit;

    private RequestQueue requestQueue;
    private String loggedInKampus;
    private String loggedInRole; // Tambahkan ini jika dibutuhkan di DataDiriFragment

    public DataDiriFragment() {
        // Required empty public constructor
    }

    public static DataDiriFragment newInstance(Pendaftar pendaftar) {
        DataDiriFragment fragment = new DataDiriFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PENDAFTAR_EDIT, pendaftar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            requestQueue = Volley.newRequestQueue(getContext());
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            loggedInKampus = sharedPreferences.getString("kampus", "");
            loggedInRole = sharedPreferences.getString("role", "android"); // Ambil role juga
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            pendaftarToEdit = getArguments().getParcelable(ARG_PENDAFTAR_EDIT);
        }

        View view = inflater.inflate(R.layout.fragment_data_diri, container, false);

        tilNamaLengkap = view.findViewById(R.id.til_nama_lengkap);
        tilNIM = view.findViewById(R.id.til_nim);
        tilTanggalLahir = view.findViewById(R.id.til_tanggal_lahir);
        tilNomorTelepon = view.findViewById(R.id.til_nomor_telepon);
        tilEmail = view.findViewById(R.id.til_email);
        tilAlamat = view.findViewById(R.id.til_alamat);
        tilProgramStudi = view.findViewById(R.id.til_program_studi);
        tilAngkatan = view.findViewById(R.id.til_angkatan);
        tilIPK = view.findViewById(R.id.til_ipk);

        etNamaLengkap = view.findViewById(R.id.et_nama_lengkap);
        etNIM = view.findViewById(R.id.et_nim);
        etTanggalLahir = view.findViewById(R.id.et_tanggal_lahir);
        etNomorTelepon = view.findViewById(R.id.et_nomor_telepon);
        etEmail = view.findViewById(R.id.et_email);
        etAlamat = view.findViewById(R.id.et_alamat);
        etProgramStudi = view.findViewById(R.id.et_program_studi);
        etAngkatan = view.findViewById(R.id.et_angkatan);
        etIPK = view.findViewById(R.id.et_ipk);

        rgJenisKelamin = view.findViewById(R.id.rg_jenis_kelamin);
        rbLakiLaki = view.findViewById(R.id.rb_laki_laki);
        rbPerempuan = view.findViewById(R.id.rb_perempuan);

        btnSimpanData = view.findViewById(R.id.btn_simpan_data_mahasiswa);

        calendar = Calendar.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (pendaftarToEdit != null) {
            fillFormWithData(pendaftarToEdit);
            btnSimpanData.setText("PERBARUI DATA MAHASISWA");
            etNIM.setEnabled(false);
            tilNIM.setEnabled(false);
        } else {
            btnSimpanData.setText("SIMPAN DATA MAHASISWA");
            etNIM.setEnabled(true);
            tilNIM.setEnabled(true);
        }

        etTanggalLahir.setOnClickListener(v -> showDatePicker());
        tilTanggalLahir.setEndIconOnClickListener(v -> showDatePicker());

        btnSimpanData.setOnClickListener(v -> simpanAtauPerbaruiDataMahasiswa());
    }

    private void fillFormWithData(Pendaftar pendaftar) {
        etNamaLengkap.setText(pendaftar.getNama());
        etNIM.setText(pendaftar.getNim());

        if (pendaftar.getTanggalLahir() != null && !pendaftar.getTanggalLahir().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date date = sdf.parse(pendaftar.getTanggalLahir());
                calendar.setTime(date);
                updateLabelTanggalLahir();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (pendaftar.getJenisKelamin() != null) {
            if (pendaftar.getJenisKelamin().equalsIgnoreCase("Laki-laki")) {
                rbLakiLaki.setChecked(true);
            } else if (pendaftar.getJenisKelamin().equalsIgnoreCase("Perempuan")) {
                rbPerempuan.setChecked(true);
            }
        }

        etNomorTelepon.setText(pendaftar.getNomorTelepon());
        etEmail.setText(pendaftar.getEmail());
        etAlamat.setText(pendaftar.getAlamat());
        etProgramStudi.setText(pendaftar.getProgramStudi());
        etAngkatan.setText(pendaftar.getAngkatan());
        etIPK.setText(pendaftar.getIpk());
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelTanggalLahir();
        };

        new DatePickerDialog(getContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabelTanggalLahir() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        etTanggalLahir.setText(sdf.format(calendar.getTime()));
    }

    private void simpanAtauPerbaruiDataMahasiswa() {
        tilNamaLengkap.setError(null);
        tilNIM.setError(null);
        tilTanggalLahir.setError(null);
        tilNomorTelepon.setError(null);
        tilEmail.setError(null);
        tilAlamat.setError(null);
        tilProgramStudi.setError(null);
        tilAngkatan.setError(null);
        tilIPK.setError(null);

        String namaLengkap = etNamaLengkap.getText().toString().trim();
        String nim = etNIM.getText().toString().trim();
        String tanggalLahir = etTanggalLahir.getText().toString().trim();
        String nomorTelepon = etNomorTelepon.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();
        String programStudi = etProgramStudi.getText().toString().trim();
        String angkatan = etAngkatan.getText().toString().trim();
        String ipk = etIPK.getText().toString().trim();

        int selectedGenderId = rgJenisKelamin.getCheckedRadioButtonId();
        final String[] jenisKelamin = {""};
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = getView().findViewById(selectedGenderId);
            jenisKelamin[0] = selectedRadioButton.getText().toString();
        }

        boolean isValid = true;

        if (namaLengkap.isEmpty()) { tilNamaLengkap.setError("Nama Lengkap tidak boleh kosong"); isValid = false; }
        if (etNIM.isEnabled() && nim.isEmpty()) { tilNIM.setError("NIM tidak boleh kosong"); isValid = false; }
        else if (etNIM.isEnabled() && nim.length() != 10) { tilNIM.setError("NIM harus 10 digit"); isValid = false; }
        if (tanggalLahir.isEmpty()) { tilTanggalLahir.setError("Tanggal Lahir tidak boleh kosong"); isValid = false; }
        if (nomorTelepon.isEmpty()) { tilNomorTelepon.setError("Nomor Telepon tidak boleh kosong"); isValid = false; }
        if (email.isEmpty()) { tilEmail.setError("Email tidak boleh kosong"); isValid = false; }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { tilEmail.setError("Format email tidak valid"); isValid = false; }
        if (alamat.isEmpty()) { tilAlamat.setError("Alamat tidak boleh kosong"); isValid = false; }
        if (programStudi.isEmpty()) { tilProgramStudi.setError("Program Studi tidak boleh kosong"); isValid = false; }
        if (angkatan.isEmpty()) { tilAngkatan.setError("Angkatan tidak boleh kosong"); isValid = false; }
        else {
            try {
                int year = Integer.parseInt(angkatan);
                if (year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR) + 1) {
                    tilAngkatan.setError("Tahun angkatan tidak valid"); isValid = false;
                }
            } catch (NumberFormatException e) {
                tilAngkatan.setError("Angkatan harus berupa angka"); isValid = false;
            }
        }
        if (ipk.isEmpty()) { tilIPK.setError("IPK tidak boleh kosong"); isValid = false; }
        else {
            try {
                double ipkValue = Double.parseDouble(ipk);
                if (ipkValue < 0.0 || ipkValue > 4.0) {
                    tilIPK.setError("IPK tidak valid (0.0 - 4.0)"); isValid = false;
                }
            } catch (NumberFormatException e) {
                tilIPK.setError("IPK harus berupa angka desimal"); isValid = false;
            }
        }
        if (selectedGenderId == -1) {
            Toast.makeText(getContext(), "Jenis Kelamin harus dipilih", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            String tanggalDaftar;
            if (pendaftarToEdit != null && pendaftarToEdit.getTanggalDaftar() != null) {
                tanggalDaftar = pendaftarToEdit.getTanggalDaftar();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
                tanggalDaftar = sdf.format(new Date());
            }

            if (loggedInKampus.isEmpty() && !loggedInRole.equals("developer")) {
                Toast.makeText(getContext(), "Informasi kampus tidak ditemukan. Mohon login ulang.", Toast.LENGTH_LONG).show();
                return;
            }

            if (requestQueue == null || getContext() == null) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "RequestQueue belum diinisialisasi.", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL_MAHASISWA,
                    response -> {
                        if (getContext() == null) return;
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                            if (success) {
                                if (pendaftarToEdit != null) {
                                    getParentFragmentManager().popBackStack();
                                    if (getActivity() instanceof MainActivity) {
                                        ((MainActivity) getActivity()).setToolbarTitle("Detail Pendaftar");
                                    }
                                } else {
                                    resetForm();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        if (getContext() == null) return;
                        Toast.makeText(getContext(), "Error jaringan: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("nim", nim);
                    params.put("nama_lengkap", namaLengkap);
                    params.put("tanggal_lahir", tanggalLahir);
                    params.put("jenis_kelamin", jenisKelamin[0]); // Fix error di sini
                    params.put("nomor_telepon", nomorTelepon);
                    params.put("email", email);
                    params.put("alamat", alamat);
                    params.put("program_studi", programStudi);
                    params.put("angkatan", angkatan);
                    params.put("ipk", ipk);
                    params.put("tanggal_daftar", tanggalDaftar);
                    params.put("kampus_mahasiswa", loggedInKampus);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        } else {
            if (getContext() != null)
                Toast.makeText(getContext(), "Mohon lengkapi semua data dengan benar.", Toast.LENGTH_SHORT).show();
        }
    }


    private void resetForm() {
        etNamaLengkap.setText("");
        etNIM.setText("");
        etTanggalLahir.setText("");
        etNomorTelepon.setText("");
        etEmail.setText("");
        etAlamat.setText("");
        etProgramStudi.setText("");
        etAngkatan.setText("");
        etIPK.setText("");
        rgJenisKelamin.clearCheck();
        etNIM.setEnabled(true);
        tilNIM.setEnabled(true);
    }
}