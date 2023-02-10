package com.haiprj.converttomp3.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<T> extends Fragment {

    protected T binding;

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (T) DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View view = ((ViewDataBinding) binding).getRoot();
        initView(view);
        addEvent(view);
        return view;
    }
    protected abstract void initView(View view);
    protected abstract void addEvent(View view);
    protected abstract int getLayoutId();

}
