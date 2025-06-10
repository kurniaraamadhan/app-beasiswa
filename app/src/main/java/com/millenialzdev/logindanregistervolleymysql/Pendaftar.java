package com.millenialzdev.logindanregistervolleymysql;

public class Pendaftar {
    private String nama;
    private String nim;
    private String status; // Misal: "Menunggu Verifikasi", "Diverifikasi", "Ditolak"

    public Pendaftar(String nama, String nim, String status) {
        this.nama = nama;
        this.nim = nim;
        this.status = status;
    }

    // Buat Getter methods
    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getStatus() {
        return status;
    }
}