package com.millenialzdev.logindanregistervolleymysql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BerkasDitolakAdapter extends RecyclerView.Adapter<BerkasDitolakAdapter.BerkasDitolakViewHolder> {

    private List<Berkas> berkasList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDetailClick(Berkas berkas);
    }

    public BerkasDitolakAdapter(List<Berkas> berkasList, OnItemClickListener listener) {
        this.berkasList = berkasList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BerkasDitolakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berkas_ditolak, parent, false);
        return new BerkasDitolakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BerkasDitolakViewHolder holder, int position) {
        Berkas berkas = berkasList.get(position);
        holder.tvNIM.setText("NIM: " + berkas.getNim());
        holder.tvJenisBerkas.setText("Jenis Berkas: " + berkas.getJenisBerkas());
        // Tampilkan alasan penolakan
        if (berkas.getAlasanDitolak() != null && !berkas.getAlasanDitolak().isEmpty()) {
            holder.tvAlasanDitolak.setText("Alasan Ditolak: " + berkas.getAlasanDitolak());
            holder.tvAlasanDitolak.setVisibility(View.VISIBLE);
        } else {
            holder.tvAlasanDitolak.setVisibility(View.GONE);
        }

        // Ubah Listener di sini
        holder.btnLihatDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetailClick(berkas);
            }
        });
    }

    @Override
    public int getItemCount() {
        return berkasList.size();
    }

    public static class BerkasDitolakViewHolder extends RecyclerView.ViewHolder {
        TextView tvNIM, tvJenisBerkas, tvAlasanDitolak;
        Button btnLihatDetail;

        public BerkasDitolakViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNIM = itemView.findViewById(R.id.tv_berkas_nim_ditolak);
            tvJenisBerkas = itemView.findViewById(R.id.tv_berkas_jenis_ditolak);
            tvAlasanDitolak = itemView.findViewById(R.id.tv_berkas_alasan_ditolak);
            btnLihatDetail = itemView.findViewById(R.id.btn_lihat_detail_ditolak);
        }
    }

    public void updateList(List<Berkas> newList) {
        this.berkasList = newList;
        notifyDataSetChanged();
    }
}