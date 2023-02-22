package com.haiprj.converttomp3.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ItemFileBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class FileModelAdapter extends RecyclerView.Adapter<FileModelAdapter.ViewHolder> {

    private Context context;
    private final List<FileModel> list = new ArrayList<>();

    public FileModelAdapter(Context context) {
        this.context = context;
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.load(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(List<FileModel> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemFileBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFileBinding.bind(itemView);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void load(FileModel fileModel){
            float size = fileModel.getSize() / (1024f * 1024f);
            binding.fileName.setText(fileModel.getDisplayName());

            binding.fileSize.setText(AppUtils.stringForTime(fileModel.getDuration()));
            if (fileModel.getDisplayName().endsWith(".mp4")) {
                Glide.with(context).load(fileModel.getFileUri()).into(binding.imgItem);
            }
            else {
                binding.imgItem.setImageResource(R.drawable.music_note);
            }

            binding.itemLayout.setOnClickListener(v -> {
                listener.onClick(getPosition());
            });

            binding.more.setOnClickListener(v -> {
                listener.onMore(getPosition(), fileModel, binding.more);
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
        void onMore(int position, Object object, View view);
    }
}
