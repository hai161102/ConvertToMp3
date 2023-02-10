package com.haiprj.converttomp3.ui.fragment;

import android.Manifest;
import android.view.View;

import androidx.annotation.NonNull;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMp3FilesBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.adapter.FileModelAdapter;
import com.haiprj.converttomp3.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Mp3Fragment extends BaseFragment<FragmentMp3FilesBinding> implements ViewResult {

    private boolean isEmpty = true;
    public static final String TAG = "Mp3Fragment";
    private final List<FileModel> list = new ArrayList<>();
    private final AppDataPresenter dataPresenter;

    private FileModelAdapter fileModelAdapter;

    public Mp3Fragment() {
        dataPresenter = new AppDataPresenter(this);
    }

    @Override
    protected void initView() {
        fileModelAdapter = new FileModelAdapter(requireContext());
        fileModelAdapter.setListener(new FileModelAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

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

    private void showViewEmpty() {
        binding.emptyView.setVisibility(View.VISIBLE);
    }
    @Override
    protected void addEvent() {

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
        fileModelAdapter.update(list);
    }

    @Override
    public void onViewNotAvailable(String mess) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PermissionUtil.isPermissionGranted(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            PermissionUtil.requestPermission(requireActivity(), 1, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
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

    public void loadData() {
        dataPresenter.loadFile(requireContext(),TAG);
    }
}
