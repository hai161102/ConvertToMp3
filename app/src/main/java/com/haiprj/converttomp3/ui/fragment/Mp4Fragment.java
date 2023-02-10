package com.haiprj.converttomp3.ui.fragment;

import android.Manifest;
import android.util.Log;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMp4FilesBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.activity.WatchVideoActivity;
import com.haiprj.converttomp3.ui.adapter.FileModelAdapter;
import com.haiprj.converttomp3.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Mp4Fragment extends BaseFragment<FragmentMp4FilesBinding> implements ViewResult {

    private boolean isEmpty = true;
    public static final String TAG = "Mp4Fragment";
    private final List<FileModel> list = new ArrayList<>();
    private final AppDataPresenter dataPresenter;

    private FileModelAdapter fileModelAdapter;


    public Mp4Fragment() {
        dataPresenter = new AppDataPresenter(this);
    }

    @Override
    protected void initView() {
        fileModelAdapter = new FileModelAdapter(requireContext());
        fileModelAdapter.setListener(new FileModelAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                WatchVideoActivity.start(requireContext(), list.get(position).getFileUri().toString());
            }
        });
        binding.rcvFileConvert.setAdapter(fileModelAdapter);
        if (isEmpty) {
            showViewEmpty();
        }
        if (PermissionUtil.isPermissionGranted(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            loadData();
        }
        else {
            PermissionUtil.requestPermission(requireActivity(), 1, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PermissionUtil.isPermissionGranted(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            PermissionUtil.requestPermission(requireActivity(), 1, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
    }

    private void showViewEmpty() {
        binding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void addEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mp4_files;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAvailable(String key, Object... objects) {
        if (Objects.equals(key, "loadFile")) {
            list.clear();
            list.addAll((Collection<? extends FileModel>) objects[0]);
            onFileAvailable();
        }
    }

    private void onFileAvailable() {
        isEmpty = list.isEmpty();
        if (isEmpty) {
            showViewEmpty();
        }
        else hideViewEmpty();
        Log.d(Mp4Fragment.TAG, "onFileAvailable Fragment: " + list.size());
        fileModelAdapter.update(list);
//        Log.d(TAG, "onFileAvailable: " + list.size());
    }

    private void hideViewEmpty() {
        binding.emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onViewNotAvailable(String mess) {

    }

    public void loadData() {
        dataPresenter.loadFile(requireContext(),TAG);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (PermissionUtil.isPermissionGranted(requireContext(), permissions)){
                loadData();
            }
            else PermissionUtil.requestPermission(requireActivity(), 1, permissions);
        }
    }
}
