package com.millenialzdev.logindanregistervolleymysql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import java.util.List;

public class BerkasDitolakAdapter extends RecyclerView.Adapter<BerkasDitolakAdapter.BerkasDitolakViewHolder> {

    private List<Berkas> berkasList;

    public BerkasDitolakAdapter(List<Berkas> berkasList) {
        this.berkasList = berkasList;
    }

    @NonNull
    @Override
    public BerkasDitolakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Meng-inflate item_berkas_ditolak.xml sebagai layout untuk setiap item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berkas_ditolak, parent, false);
        return new BerkasDitolakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BerkasDitolakViewHolder holder, int position) {
        Berkas berkas = berkasList.get(position);

        holder.tvNimDitolak.setText("NIM: " + berkas.getNim());
        holder.tvJenisDitolak.setText("Jenis Berkas: " + berkas.getJenisBerkas());
        holder.tvAlasanDitolak.setText("Alasan Ditolak: " + berkas.getAlasanDitolak());

        holder.btnLihatDetail.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Lihat Detail Berkas NIM: " + berkas.getNim(), Toast.LENGTH_SHORT).show();
            // TODO: Implementasi logika untuk membuka detail berkas
        });
    }

    public void updateList(List<Berkas> newList) {
        berkasList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return berkasList.size();
    }

    public static class BerkasDitolakViewHolder extends RecyclerView.ViewHolder {
        TextView tvNimDitolak, tvJenisDitolak, tvAlasanDitolak;
        Button btnLihatDetail;

        public BerkasDitolakViewHolder(@NonNull View itemView) {
            super(itemView);
            // ID-ID ini berasal dari item_berkas_ditolak.xml
            tvNimDitolak = itemView.findViewById(R.id.tv_berkas_nim_ditolak);
            tvJenisDitolak = itemView.findViewById(R.id.tv_berkas_jenis_ditolak);
            tvAlasanDitolak = itemView.findViewById(R.id.tv_berkas_alasan_ditolak);
            btnLihatDetail = itemView.findViewById(R.id.btn_lihat_detail_ditolak);
        }
    }
}