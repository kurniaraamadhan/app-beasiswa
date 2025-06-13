package com.millenialzdev.logindanregistervolleymysql;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class RiwayatUploadAdapter extends RecyclerView.Adapter<RiwayatUploadAdapter.RiwayatUploadViewHolder> {

    private List<Berkas> riwayatBerkasList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onLihatBerkasClick(Berkas berkas);
        void onLihatDetailPendaftarClick(Berkas berkas);
    }

    public RiwayatUploadAdapter(List<Berkas> riwayatBerkasList, OnItemClickListener listener) {
        this.riwayatBerkasList = riwayatBerkasList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RiwayatUploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_upload, parent, false);
        return new RiwayatUploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatUploadViewHolder holder, int position) {
        Berkas berkas = riwayatBerkasList.get(position);

        holder.tvNIM.setText("NIM: " + berkas.getNim());
        holder.tvTanggalUpload.setText(berkas.getTanggalUpload());
        holder.tvNamaMahasiswa.setText("Nama: " + berkas.getNamaMahasiswa()); // <--- BARU: Set nama mahasiswa
        holder.tvJenisBerkas.setText("Jenis Berkas: " + berkas.getJenisBerkas());
        holder.tvStatus.setText("Status: " + berkas.getStatus());

        // Atur warna status
        int statusColor;
        String pesanAlasanDitolak = "";
        switch (berkas.getStatus()) {
            case "Menunggu Verifikasi Pemprov":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case "Diverifikasi Pemprov":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                break;
            case "Ditolak Pemprov":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark);
                pesanAlasanDitolak = "Alasan: " + berkas.getAlasanDitolak();
                break;
            default:
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray);
                break;
        }
        holder.tvStatus.setTextColor(statusColor);

        // Tampilkan/sembunyikan alasan ditolak
        if (berkas.getStatus().equals("Ditolak Pemprov") && berkas.getAlasanDitolak() != null && !berkas.getAlasanDitolak().isEmpty()) {
            holder.tvAlasanDitolak.setText(pesanAlasanDitolak);
            holder.tvAlasanDitolak.setVisibility(View.VISIBLE);
        } else {
            holder.tvAlasanDitolak.setVisibility(View.GONE);
        }

        holder.btnLihatBerkas.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLihatBerkasClick(berkas);
            }
        });

        // Opsional: Klik pada item secara keseluruhan untuk melihat detail pendaftar
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLihatDetailPendaftarClick(berkas);
            }
        });
    }

    @Override
    public int getItemCount() {
        return riwayatBerkasList.size();
    }

    public static class RiwayatUploadViewHolder extends RecyclerView.ViewHolder {
        TextView tvNIM, tvTanggalUpload, tvNamaMahasiswa, tvJenisBerkas, tvStatus, tvAlasanDitolak; // <--- tvNamaMahasiswa BARU
        Button btnLihatBerkas;

        public RiwayatUploadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNIM = itemView.findViewById(R.id.tv_riwayat_nim);
            tvTanggalUpload = itemView.findViewById(R.id.tv_riwayat_tanggal_upload);
            tvNamaMahasiswa = itemView.findViewById(R.id.tv_riwayat_nama_mahasiswa); // <--- Inisialisasi BARU
            tvJenisBerkas = itemView.findViewById(R.id.tv_riwayat_jenis_berkas);
            tvStatus = itemView.findViewById(R.id.tv_riwayat_status);
            tvAlasanDitolak = itemView.findViewById(R.id.tv_riwayat_alasan_ditolak);
            btnLihatBerkas = itemView.findViewById(R.id.btn_lihat_berkas_riwayat);
        }
    }

    public void updateList(List<Berkas> newList) {
        this.riwayatBerkasList = newList;
        notifyDataSetChanged();
    }
}