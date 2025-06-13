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

public class UploadedBerkasAdapter extends RecyclerView.Adapter<UploadedBerkasAdapter.UploadedBerkasViewHolder> {

    private List<Berkas> uploadedBerkasList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onLihatBerkasClick(Berkas berkas);
    }

    public UploadedBerkasAdapter(List<Berkas> uploadedBerkasList, OnItemClickListener listener) {
        this.uploadedBerkasList = uploadedBerkasList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UploadedBerkasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berkas_uploaded, parent, false);
        return new UploadedBerkasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadedBerkasViewHolder holder, int position) {
        Berkas berkas = uploadedBerkasList.get(position);

        holder.tvJenisBerkas.setText(berkas.getJenisBerkas());
        holder.tvStatus.setText("Status: " + berkas.getStatus());

        // Atur warna status
        int statusColor;
        switch (berkas.getStatus()) {
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
        holder.ivBerkasIcon.setColorFilter(statusColor);


        holder.btnLihatBerkas.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLihatBerkasClick(berkas);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploadedBerkasList.size();
    }

    public static class UploadedBerkasViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBerkasIcon;
        TextView tvJenisBerkas, tvStatus;
        Button btnLihatBerkas;

        public UploadedBerkasViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBerkasIcon = itemView.findViewById(R.id.iv_berkas_icon);
            tvJenisBerkas = itemView.findViewById(R.id.tv_uploaded_berkas_jenis);
            tvStatus = itemView.findViewById(R.id.tv_uploaded_berkas_status);
            btnLihatBerkas = itemView.findViewById(R.id.btn_lihat_uploaded_berkas);
        }
    }

    public void updateList(List<Berkas> newList) {
        this.uploadedBerkasList = newList;
        notifyDataSetChanged();
    }
}