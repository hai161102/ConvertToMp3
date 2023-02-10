package com.haiprj.converttomp3.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.haiprj.android_app_lib.ui.BaseAdapter;
import com.haiprj.android_app_lib.ui.BaseViewHolder;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ItemFileBinding;
import com.haiprj.converttomp3.models.ItemFiles;

import java.util.List;

public class ItemFileAdapter extends BaseAdapter<ItemFileBinding, ItemFiles> {

    public ItemFileAdapter(Context context, List<ItemFiles> list) {
        super(context, list);
    }

    public ItemFileAdapter() {
    }

    @Override
    protected BaseViewHolder<ItemFileBinding> getViewHolder() {
        ItemFileBinding itemFilesBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_file, parent, false);
        return new ViewHolder(itemFilesBinding);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class ViewHolder extends BaseViewHolder<ItemFileBinding>{

        public ViewHolder(@NonNull ItemFileBinding binding) {
            super(binding);
        }

        @Override
        public void load(Object object) {
            ItemFiles itemFiles = (ItemFiles) object;

        }
    }
}
