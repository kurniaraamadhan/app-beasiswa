package com.millenialzdev.logindanregistervolleymysql;

import android.app.DatePickerDialog;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataDiriFragment extends Fragment {

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

    public DataDiriFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_diri, container, false);

        // Inisialisasi TextInputLayout
        tilNamaLengkap = view.findViewById(R.id.til_nama_lengkap);
        tilNIM = view.findViewById(R.id.til_nim);
        tilTanggalLahir = view.findViewById(R.id.til_tanggal_lahir);
        tilNomorTelepon = view.findViewById(R.id.til_nomor_telepon);
        tilEmail = view.findViewById(R.id.til_email);
        tilAlamat = view.findViewById(R.id.til_alamat);
        tilProgramStudi = view.findViewById(R.id.til_program_studi);
        tilAngkatan = view.findViewById(R.id.til_angkatan);
        tilIPK = view.findViewById(R.id.til_ipk);

        // Inisialisasi TextInputEditText
        etNamaLengkap = view.findViewById(R.id.et_nama_lengkap);
        etNIM = view.findViewById(R.id.et_nim);
        etTanggalLahir = view.findViewById(R.id.et_tanggal_lahir);
        etNomorTelepon = view.findViewById(R.id.et_nomor_telepon);
        etEmail = view.findViewById(R.id.et_email);
        etAlamat = view.findViewById(R.id.et_alamat);
        etProgramStudi = view.findViewById(R.id.et_program_studi);
        etAngkatan = view.findViewById(R.id.et_angkatan);
        etIPK = view.findViewById(R.id.et_ipk);

        // Inisialisasi RadioGroup dan RadioButton
        rgJenisKelamin = view.findViewById(R.id.rg_jenis_kelamin);
        rbLakiLaki = view.findViewById(R.id.rb_laki_laki);
        rbPerempuan = view.findViewById(R.id.rb_perempuan);

        // Inisialisasi Button
        btnSimpanData = view.findViewById(R.id.btn_simpan_data_mahasiswa);

        calendar = Calendar.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Listener untuk Tanggal Lahir (DatePicker)
        etTanggalLahir.setOnClickListener(v -> showDatePicker());
        tilTanggalLahir.setEndIconOnClickListener(v -> showDatePicker()); // Juga ketika ikon kalender diklik

        // Listener untuk tombol Simpan Data
        btnSimpanData.setOnClickListener(v -> simpanDataMahasiswa());
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
        String myFormat = "dd/MM/yyyy"; // Format tanggal yang diinginkan
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        etTanggalLahir.setText(sdf.format(calendar.getTime()));
    }

    private void simpanDataMahasiswa() {
        // Reset error state
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
        String jenisKelamin = "";
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = getView().findViewById(selectedGenderId);
            jenisKelamin = selectedRadioButton.getText().toString();
        }

        boolean isValid = true;

        if (namaLengkap.isEmpty()) {
            tilNamaLengkap.setError("Nama Lengkap tidak boleh kosong");
            isValid = false;
        }
        if (nim.isEmpty()) {
            tilNIM.setError("NIM tidak boleh kosong");
            isValid = false;
        } else if (nim.length() != 10) { // Contoh validasi panjang NIM
            tilNIM.setError("NIM harus 10 digit");
            isValid = false;
        }
        if (tanggalLahir.isEmpty()) {
            tilTanggalLahir.setError("Tanggal Lahir tidak boleh kosong");
            isValid = false;
        }
        if (nomorTelepon.isEmpty()) {
            tilNomorTelepon.setError("Nomor Telepon tidak boleh kosong");
            isValid = false;
        }
        if (email.isEmpty()) {
            tilEmail.setError("Email tidak boleh kosong");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Format email tidak valid");
            isValid = false;
        }
        if (alamat.isEmpty()) {
            tilAlamat.setError("Alamat tidak boleh kosong");
            isValid = false;
        }
        if (programStudi.isEmpty()) {
            tilProgramStudi.setError("Program Studi tidak boleh kosong");
            isValid = false;
        }
        if (angkatan.isEmpty()) {
            tilAngkatan.setError("Angkatan tidak boleh kosong");
            isValid = false;
        } else {
            try {
                int year = Integer.parseInt(angkatan);
                if (year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR)) { // Contoh validasi tahun
                    tilAngkatan.setError("Tahun angkatan tidak valid");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilAngkatan.setError("Angkatan harus berupa angka");
                isValid = false;
            }
        }
        if (ipk.isEmpty()) {
            tilIPK.setError("IPK tidak boleh kosong");
            isValid = false;
        } else {
            try {
                double ipkValue = Double.parseDouble(ipk);
                if (ipkValue < 0.0 || ipkValue > 4.0) { // IPK validasi 0.0 - 4.0
                    tilIPK.setError("IPK tidak valid (0.0 - 4.0)");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilIPK.setError("IPK harus berupa angka desimal");
                isValid = false;
            }
        }
        if (selectedGenderId == -1) {
            Toast.makeText(getContext(), "Jenis Kelamin harus dipilih", Toast.LENGTH_SHORT).show();
            isValid = false;
        }


        if (isValid) {
            // Jika semua validasi sukses, lakukan sesuatu dengan data
            // Contoh: Tampilkan data di Toast
            String data = "Nama: " + namaLengkap + "\n" +
                    "NIM: " + nim + "\n" +
                    "Tanggal Lahir: " + tanggalLahir + "\n" +
                    "Jenis Kelamin: " + jenisKelamin + "\n" +
                    "Telepon: " + nomorTelepon + "\n" +
                    "Email: " + email + "\n" +
                    "Alamat: " + alamat + "\n" +
                    "Prodi: " + programStudi + "\n" +
                    "Angkatan: " + angkatan + "\n" +
                    "IPK: " + ipk;
            Toast.makeText(getContext(), "Data Mahasiswa Disimpan:\n" + data, Toast.LENGTH_LONG).show();

            // TODO: Di sini adalah tempat kamu menyimpan data ke database (SQLite/Room)
            //       atau mengirimnya ke server melalui API.
            //       Setelah berhasil disimpan, mungkin kamu bisa reset form atau kembali ke HomeFragment.

            // Contoh: Mereset form setelah simpan
            resetForm();
        } else {
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
    }
}