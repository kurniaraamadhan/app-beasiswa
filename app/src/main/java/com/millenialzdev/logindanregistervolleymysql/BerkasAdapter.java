package com.millenialzdev.logindanregistervolleymysql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

public class BerkasAdapter extends RecyclerView.Adapter<BerkasAdapter.ViewHolder> {

    private final List<String> berkasList;

    public BerkasAdapter(List<String> berkasList) {
        this.berkasList = berkasList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false); // Simple layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String berkas = berkasList.get(position);
        holder.textView.setText(berkas);

        // Animasi transisi Fade In
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in);
        holder.itemView.startAnimation(animation);

        // Klik item -> buka DetailBerkasActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailBerkasActivity.class);
            intent.putExtra("nama_berkas", berkas);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return berkasList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
