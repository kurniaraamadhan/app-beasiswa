package com.millenialzdev.logindanregistervolleymysql; // Ganti dengan package-mu

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectedFilesAdapter extends RecyclerView.Adapter<SelectedFilesAdapter.SelectedFileViewHolder> {

    private List<SelectedFile> selectedFilesList;
    private OnItemRemoveListener removeListener; // Listener untuk menghapus item

    public interface OnItemRemoveListener {
        void onRemoveClick(int position);
    }

    public SelectedFilesAdapter(List<SelectedFile> selectedFilesList, OnItemRemoveListener removeListener) {
        this.selectedFilesList = selectedFilesList;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public SelectedFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_file, parent, false);
        return new SelectedFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedFileViewHolder holder, int position) {
        SelectedFile file = selectedFilesList.get(position);
        holder.tvFileName.setText(file.getFileName());

        holder.ivRemoveFile.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemoveClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedFilesList.size();
    }

    public static class SelectedFileViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        ImageView ivRemoveFile;

        public SelectedFileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tv_selected_file_name);
            ivRemoveFile = itemView.findViewById(R.id.iv_remove_selected_file);
        }
    }

    public void updateList(List<SelectedFile> newList) {
        this.selectedFilesList = newList;
        notifyDataSetChanged();
    }
}