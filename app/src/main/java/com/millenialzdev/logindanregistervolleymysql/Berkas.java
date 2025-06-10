package com.millenialzdev.logindanregistervolleymysql;

public class Berkas {
    private String nim;
    private String jenisBerkas;
    private String status;
    private String alasanDitolak;

    public Berkas(String nim, String jenisBerkas, String status, String s) {
        this.nim = nim;
        this.jenisBerkas = jenisBerkas;
        this.status = status;
        this.alasanDitolak = alasanDitolak;
    }

    public String getNim() { return nim; }
    public String getJenisBerkas() { return jenisBerkas; }
    public String getStatus() { return status; }
    public String getAlasanDitolak() { return alasanDitolak; }
}