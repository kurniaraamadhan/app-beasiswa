package com.millenialzdev.logindanregistervolleymysql;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder> {

    private List<Notifikasi> notifikasiList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onNotifikasiClick(Notifikasi notifikasi);
    }

    public NotifikasiAdapter(List<Notifikasi> notifikasiList, OnItemClickListener listener) {
        this.notifikasiList = notifikasiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotifikasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifikasi, parent, false);
        return new NotifikasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiViewHolder holder, int position) {
        Notifikasi notifikasi = notifikasiList.get(position);

        holder.tvJudul.setText(notifikasi.getJudul());
        holder.tvDeskripsi.setText(notifikasi.getDeskripsi());
        holder.ivIcon.setImageResource(notifikasi.getIkon());

        // Atur warna berdasarkan prioritas (misal: "red", "orange", "green")
        int tintColor = Color.parseColor("#009688"); // Default teal
        switch (notifikasi.getWarnaPrioritas().toLowerCase()) {
            case "red":
                tintColor = Color.parseColor("#D32F2F"); // Merah untuk error/penting
                break;
            case "orange":
                tintColor = Color.parseColor("#FF9800"); // Oranye untuk peringatan
                break;
            case "green":
                tintColor = Color.parseColor("#4CAF50"); // Hijau untuk sukses/info
                break;
            case "blue":
                tintColor = Color.parseColor("#2196F3"); // Biru untuk info umum
                break;
            default:
                tintColor = Color.parseColor("#009688"); // Default teal
                break;
        }
        holder.ivIcon.setColorFilter(tintColor);
        holder.tvJudul.setTextColor(tintColor); // Judul juga bisa pakai warna prioritas

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotifikasiClick(notifikasi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifikasiList.size();
    }

    public static class NotifikasiViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvJudul, tvDeskripsi;

        public NotifikasiViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_notifikasi_icon);
            tvJudul = itemView.findViewById(R.id.tv_notifikasi_judul);
            tvDeskripsi = itemView.findViewById(R.id.tv_notifikasi_deskripsi);
        }
    }

    public void updateList(List<Notifikasi> newList) {
        this.notifikasiList = newList;
        notifyDataSetChanged();
    }
}