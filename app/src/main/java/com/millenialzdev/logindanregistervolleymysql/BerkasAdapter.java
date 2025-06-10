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

public class BerkasAdapter extends RecyclerView.Adapter<BerkasAdapter.BerkasViewHolder> {

    private List<Berkas> berkasList;

    public BerkasAdapter(List<Berkas> berkasList) {
        this.berkasList = berkasList;
    }

    @NonNull
    @Override
    public BerkasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berkas_mahasiswa, parent, false);
        return new BerkasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BerkasViewHolder holder, int position) {
        Berkas berkas = berkasList.get(position);
        holder.tvNIM.setText("NIM: " + berkas.getNim());
        holder.tvJenisBerkas.setText("Jenis: " + berkas.getJenisBerkas());
        holder.tvStatus.setText("Status: " + berkas.getStatus());

        // Atur warna status
        int statusColor;
        switch (berkas.getStatus()) {
            case "Pending":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case "Disetujui":
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

        holder.btnTolak.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Berkas " + berkas.getJenisBerkas() + " dari NIM " + berkas.getNim() + " ditolak.", Toast.LENGTH_SHORT).show();
            // TODO: Implementasi logika tolak berkas
        });

        holder.btnSetujui.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Berkas " + berkas.getJenisBerkas() + " dari NIM " + berkas.getNim() + " disetujui.", Toast.LENGTH_SHORT).show();
            // TODO: Implementasi logika setujui berkas
        });
    }

    @Override
    public int getItemCount() {
        return berkasList.size();
    }

    public static class BerkasViewHolder extends RecyclerView.ViewHolder {
        TextView tvNIM, tvJenisBerkas, tvStatus;
        Button btnTolak, btnSetujui;

        public BerkasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNIM = itemView.findViewById(R.id.tv_berkas_nim);
            tvJenisBerkas = itemView.findViewById(R.id.tv_berkas_jenis);
            tvStatus = itemView.findViewById(R.id.tv_berkas_status);
            btnTolak = itemView.findViewById(R.id.btn_tolak_berkas);
            btnSetujui = itemView.findViewById(R.id.btn_setujui_berkas);
        }
    }

    public void updateList(List<Berkas> newList) {
        berkasList = newList;
        notifyDataSetChanged();
    }
}