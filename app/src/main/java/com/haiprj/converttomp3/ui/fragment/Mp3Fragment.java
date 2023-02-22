package com.haiprj.converttomp3.ui.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.AppCallback;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMp3FilesBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.adapter.MusicAdapter;
import com.haiprj.converttomp3.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Mp3Fragment extends BaseFragment<FragmentMp3FilesBinding> {

    private boolean isEmpty = true;
    public static final String TAG = "Mp3Fragment";

    private final List<FileModel> list = new ArrayList<>();
    private MusicAdapter musicAdapter;

    private AppCallback callback;

    public Mp3Fragment(List<FileModel> list) {
        this.list.clear();
        this.list.addAll(list);
    }


    public void setCallback(AppCallback callback) {
        this.callback = callback;
    }

    private final MusicAdapter.MusicItemListener musicItemListener = new MusicAdapter.MusicItemListener() {
        @Override
        public void callback(String key, Object... objects) {
            callback.action(key, objects);
        }
    };
    @Override
    protected void initView() {
        musicAdapter = new MusicAdapter(requireContext(), musicItemListener);

        binding.rcvFileConvert.setAdapter(musicAdapter);
        if (isEmpty) {
            showViewEmpty();
        }

        setupFile(list);
    }


    private void showViewEmpty() {
        binding.emptyView.setVisibility(View.VISIBLE);

    }
    @Override
    protected void addEvent() {

    }


    private void viewDetails(Uri uri) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mp3_files;
    }

    private void hideViewEmpty() {
        binding.emptyView.setVisibility(View.GONE);
    }

    private void setupFile(List<FileModel> fileModelList) {
        isEmpty = fileModelList.isEmpty();
        musicAdapter.update(fileModelList);
        try {
            if (isEmpty) {
                showViewEmpty();
            }
            else hideViewEmpty();
        }catch (Exception ignored) {

        }

    }


    @Override
    public void onResume() {
        super.onResume();

    }


    public List<FileModel> getList() {
        return list;
    }

    public void updateList(List<FileModel> listMp3) {
        this.list.clear();
        this.list.addAll(listMp3);
        setupFile(this.list);
    }
}
