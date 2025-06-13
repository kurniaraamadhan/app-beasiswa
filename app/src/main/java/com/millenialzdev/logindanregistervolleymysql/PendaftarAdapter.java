package com.millenialzdev.logindanregistervolleymysql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class PendaftarAdapter extends RecyclerView.Adapter<PendaftarAdapter.PendaftarViewHolder> {

    private List<Pendaftar> pendaftarList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDetailClick(Pendaftar pendaftar);
    }

    public PendaftarAdapter(List<Pendaftar> pendaftarList, OnItemClickListener listener) {
        this.pendaftarList = pendaftarList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PendaftarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pendaftar_baru, parent, false);
        return new PendaftarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendaftarViewHolder holder, int position) {
        Pendaftar pendaftar = pendaftarList.get(position);
        holder.tvName.setText(pendaftar.getNama());
        holder.tvNIM.setText("NIM: " + pendaftar.getNim());

        // BARU: Tampilkan Prodi dan Angkatan
        holder.tvProdiAngkatan.setText("Prodi: " + pendaftar.getProgramStudi() + " | Angkatan: " + pendaftar.getAngkatan());
        // BARU: Tampilkan Tanggal Daftar (asumsi ada field tanggalDaftar di Pendaftar.java)
        // Untuk contoh ini, saya akan tambahkan field dummy "tanggalDaftar" di Pendaftar.java
        // holder.tvTanggalDaftar.setText("Tanggal Daftar: " + pendaftar.getTanggalDaftar());

        // Jika kamu belum menambahkan tanggalDaftar di Pendaftar.java, bisa hardcode atau pakai tanggal sekarang
        holder.tvTanggalDaftar.setText("Tanggal Daftar: 10 Juni 2025"); // Contoh dummy

        holder.tvStatus.setText("Status: " + pendaftar.getStatus());

        // Atur warna status
        int statusColor;
        switch (pendaftar.getStatus()) {
            case "Menunggu Verifikasi Pemprov":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case "Diverifikasi Pemprov":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                break;
            case "Ditolak Pemprov":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark);
                break;
            default:
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray);
                break;
        }
        holder.tvStatus.setTextColor(statusColor);

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetailClick(pendaftar);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendaftarList.size();
    }

    public static class PendaftarViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNIM, tvProdiAngkatan, tvTanggalDaftar, tvStatus; // tvProdiAngkatan, tvTanggalDaftar BARU
        Button btnDetail;

        public PendaftarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_pendaftar_name);
            tvNIM = itemView.findViewById(R.id.tv_pendaftar_nim);
            tvProdiAngkatan = itemView.findViewById(R.id.tv_pendaftar_prodi_angkatan); // Inisialisasi BARU
            tvTanggalDaftar = itemView.findViewById(R.id.tv_pendaftar_tanggal_daftar); // Inisialisasi BARU
            tvStatus = itemView.findViewById(R.id.tv_pendaftar_status);
            btnDetail = itemView.findViewById(R.id.btn_view_detail);
        }
    }

    public void updateList(List<Pendaftar> newList) {
        this.pendaftarList = newList;
        notifyDataSetChanged();
    }
}