package com.haiprj.converttomp3.ui.fragment;

import android.view.View;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMp3FilesBinding;
import com.haiprj.converttomp3.models.ItemFiles;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.adapter.ItemFileAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Mp3Fragment extends BaseFragment<FragmentMp3FilesBinding> implements ViewResult {

    private boolean isEmpty = false;
    private final String TAG = "Mp3Fragment";
    private final List<ItemFiles> list = new ArrayList<>();
    private final AppDataPresenter dataPresenter;

    private ItemFileAdapter itemFileAdapter;

    public Mp3Fragment() {
        dataPresenter = new AppDataPresenter(this);
    }

    @Override
    protected void initView(View view) {
        itemFileAdapter = new ItemFileAdapter(requireContext(), list);
        dataPresenter.loadFile(TAG);
        if (isEmpty) {
            showViewEmpty();
        }
    }

    private void showViewEmpty() {
        binding.emptyView.setVisibility(View.VISIBLE);
    }
    @Override
    protected void addEvent(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mp3_files;
    }

    private void hideViewEmpty() {
        binding.emptyView.setVisibility(View.GONE);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void onViewAvailable(String key, Object... objects) {
        if (Objects.equals(key, "loadFile")) {
            list.clear();
            list.addAll((Collection<? extends ItemFiles>) objects[0]);
            onFileAvailable();
        }
    }

    private void onFileAvailable() {
        isEmpty = list.isEmpty();
        if (isEmpty) {
            showViewEmpty();
        }
        else hideViewEmpty();
        itemFileAdapter.update(list);
    }

    @Override
    public void onViewNotAvailable(String mess) {

    }
}