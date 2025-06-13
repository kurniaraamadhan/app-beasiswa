package com.millenialzdev.logindanregistervolleymysql;

import android.os.Parcel;
import android.os.Parcelable;

public class Pendaftar implements Parcelable {
    private String nama;
    private String nim;
    private String status;
    private String tanggalLahir;
    private String jenisKelamin;
    private String nomorTelepon;
    private String email;
    private String alamat;
    private String programStudi;
    private String angkatan;
    private String ipk;
    private String tanggalDaftar;

    // Konstruktor dengan semua field yang relevan
    public Pendaftar(String nama, String nim, String status, String tanggalLahir, String jenisKelamin,
                     String nomorTelepon, String email, String alamat,
                     String programStudi, String angkatan, String ipk, String tanggalDaftar) {
        this.nama = nama;
        this.nim = nim;
        this.status = status;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.alamat = alamat;
        this.programStudi = programStudi;
        this.angkatan = angkatan;
        this.ipk = ipk;
        this.tanggalDaftar = tanggalDaftar;
    }

    // --- Getter methods ---
    public String getNama() { return nama; }
    public String getNim() { return nim; }
    public String getStatus() { return status; }
    public String getTanggalLahir() { return tanggalLahir; }
    public String getJenisKelamin() { return jenisKelamin; }
    public String getNomorTelepon() { return nomorTelepon; }
    public String getEmail() { return email; }
    public String getAlamat() { return alamat; }
    public String getProgramStudi() { return programStudi; }
    public String getAngkatan() { return angkatan; }
    public String getIpk() { return ipk; }
    public String getTanggalDaftar() { return tanggalDaftar; }


    // --- Implementasi Parcelable ---
    protected Pendaftar(Parcel in) {
        nama = in.readString();
        nim = in.readString();
        status = in.readString();
        tanggalLahir = in.readString();
        jenisKelamin = in.readString();
        nomorTelepon = in.readString();
        email = in.readString();
        alamat = in.readString();
        programStudi = in.readString();
        angkatan = in.readString();
        ipk = in.readString();
        tanggalDaftar = in.readString();
    }

    public static final Creator<Pendaftar> CREATOR = new Creator<Pendaftar>() {
        @Override
        public Pendaftar createFromParcel(Parcel in) {
            return new Pendaftar(in);
        }

        @Override
        public Pendaftar[] newArray(int size) {
            return new Pendaftar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(nim);
        dest.writeString(status);
        dest.writeString(tanggalLahir);
        dest.writeString(jenisKelamin);
        dest.writeString(nomorTelepon);
        dest.writeString(email);
        dest.writeString(alamat);
        dest.writeString(programStudi);
        dest.writeString(angkatan);
        dest.writeString(ipk);
        dest.writeString(tanggalDaftar);
    }
}