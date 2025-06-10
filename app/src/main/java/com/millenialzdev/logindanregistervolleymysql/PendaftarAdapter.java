package com.millenialzdev.logindanregistervolleymysql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendaftarAdapter extends RecyclerView.Adapter<PendaftarAdapter.PendaftarViewHolder> {

    private List<Pendaftar> pendaftarList;

    public PendaftarAdapter(List<Pendaftar> pendaftarList) {
        this.pendaftarList = pendaftarList;
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
        holder.tvStatus.setText("Status: " + pendaftar.getStatus());

        // Atur warna status
        int statusColor;
        switch (pendaftar.getStatus()) {
            case "Menunggu Verifikasi":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case "Diverifikasi":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                break;
            case "Ditolak":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark);
                break;
            default:
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray);
                break;
        }
        holder.tvStatus.setTextColor(statusColor);


        holder.btnDetail.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Lihat Detail " + pendaftar.getNama(), Toast.LENGTH_SHORT).show();
            // TODO: Implementasi logika untuk membuka detail pendaftar (misal: new Activity/Fragment)
        });
    }
    public void updateList(List<Pendaftar> newList) {
        pendaftarList = newList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return pendaftarList.size();
    }

    public static class PendaftarViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNIM, tvStatus;
        Button btnDetail;

        public PendaftarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_pendaftar_name);
            tvNIM = itemView.findViewById(R.id.tv_pendaftar_nim);
            tvStatus = itemView.findViewById(R.id.tv_pendaftar_status);
            btnDetail = itemView.findViewById(R.id.btn_view_detail);
        }
    }
}