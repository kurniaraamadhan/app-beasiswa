package com.millenialzdev.logindanregistervolleymysql;
public class Notifikasi {
    private String judul;
    private String deskripsi;
    private int ikon;
    private String warnaPrioritas;

    public Notifikasi(String judul, String deskripsi, int ikon, String warnaPrioritas) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.ikon = ikon;
        this.warnaPrioritas = warnaPrioritas;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getIkon() {
        return ikon;
    }

    public String getWarnaPrioritas() {
        return warnaPrioritas;
    }
}