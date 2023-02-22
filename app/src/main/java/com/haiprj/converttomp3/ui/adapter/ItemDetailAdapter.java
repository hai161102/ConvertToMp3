package com.haiprj.converttomp3.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ItemDetailsBinding;
import com.haiprj.converttomp3.models.DetailsModel;
import com.haiprj.converttomp3.ui.dialog.DetailsDialog;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {

    private Context context;
    private List<DetailsModel> list = new ArrayList<>();

    public ItemDetailAdapter(Context context, List<DetailsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_details, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.load(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemDetailsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void load(DetailsModel model) {
            binding.header.setText(model.getHeader());
            binding.body.setText(model.getBody());
        }
    }
}
