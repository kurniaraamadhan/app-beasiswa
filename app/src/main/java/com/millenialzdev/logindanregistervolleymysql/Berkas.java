package com.millenialzdev.logindanregistervolleymysql;

import android.os.Parcel;
import android.os.Parcelable;

public class Berkas implements Parcelable {
    private String nim;
    private String jenisBerkas;
    private String status; // Misal: "Pending", "Disetujui", "Ditolak"
    private String alasanDitolak; // Untuk berkas yang ditolak
    private String tanggalUpload;
    private String urlBerkas;
    private String namaMahasiswa; // Field untuk nama mahasiswa

    public Berkas(String nim, String jenisBerkas, String status, String alasanDitolak, String tanggalUpload, String urlBerkas, String namaMahasiswa) {
        this.nim = nim;
        this.jenisBerkas = jenisBerkas;
        this.status = status;
        this.alasanDitolak = alasanDitolak;
        this.tanggalUpload = tanggalUpload;
        this.urlBerkas = urlBerkas;
        this.namaMahasiswa = namaMahasiswa;
    }

    // --- Getter methods ---
    public String getNim() { return nim; }
    public String getJenisBerkas() { return jenisBerkas; }
    public String getStatus() { return status; }
    public String getAlasanDitolak() { return alasanDitolak; }
    public String getTanggalUpload() { return tanggalUpload; }
    public String getUrlBerkas() { return urlBerkas; }
    public String getNamaMahasiswa() { return namaMahasiswa; }

    // --- Implementasi Parcelable ---
    protected Berkas(Parcel in) {
        nim = in.readString();
        jenisBerkas = in.readString();
        status = in.readString();
        alasanDitolak = in.readString();
        tanggalUpload = in.readString();
        urlBerkas = in.readString();
        namaMahasiswa = in.readString();
    }

    public static final Creator<Berkas> CREATOR = new Creator<Berkas>() {
        @Override
        public Berkas createFromParcel(Parcel in) {
            return new Berkas(in);
        }

        @Override
        public Berkas[] newArray(int size) {
            return new Berkas[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nim);
        dest.writeString(jenisBerkas);
        dest.writeString(status);
        dest.writeString(alasanDitolak);
        dest.writeString(tanggalUpload);
        dest.writeString(urlBerkas);
        dest.writeString(namaMahasiswa);
    }
}