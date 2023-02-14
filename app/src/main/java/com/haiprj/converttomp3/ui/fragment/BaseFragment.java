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

import com.haiprj.converttomp3.interfaces.FragmentListener;

public abstract class BaseFragment<T> extends Fragment {

    protected T binding;
    protected FragmentListener listener;
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (T) DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View view = ((ViewDataBinding) binding).getRoot();
        initView();
        addEvent();
        return view;
    }
    protected abstract void initView();
    protected abstract void addEvent();
    protected abstract int getLayoutId();

    public void setListener(FragmentListener listener) {
        this.listener = listener;
    }
}
