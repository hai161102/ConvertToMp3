package com.haiprj.converttomp3.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMp3FilesBinding;
import com.haiprj.converttomp3.models.MusicManager;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.activity.PlayMusicActivity;
import com.haiprj.converttomp3.ui.adapter.MusicAdapter;
import com.haiprj.converttomp3.ui.dialog.RenameDialog;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Mp3Fragment extends BaseFragment<FragmentMp3FilesBinding> implements ViewResult {

    private boolean isEmpty = true;
    public static final String TAG = "Mp3Fragment";
    private final List<MusicManager> list = new ArrayList<>();
    private final AppDataPresenter dataPresenter;

    private MusicAdapter musicAdapter;

    public Mp3Fragment() {
        dataPresenter = new AppDataPresenter(this);
    }

    @Override
    protected void initView() {
        musicAdapter = new MusicAdapter(requireContext(), new MusicAdapter.MusicItemListener() {
            @Override
            public void callback(String key, Object... objects) {
                if (Objects.equals(key, MusicAdapter.CLICK_MORE)) {
                    showMore(objects);
                }
                if (Objects.equals(key, MusicAdapter.CLICK)) {
                    PlayMusicActivity.start(requireContext(), (MusicManager) objects[0]);
                }
            }
        });

        binding.rcvFileConvert.setAdapter(musicAdapter);
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
    @SuppressLint("NonConstantResourceId")
    private void showMore(Object[] objects) {
        MusicManager fileModel = (MusicManager) objects[0];
        PopupMenu popup = new PopupMenu(requireContext(), (View) objects[1]);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.more_popup, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.share:
                    AppUtils.shareFile(requireContext(), fileModel.getUri());
                    break;
                case R.id.rename:
                    RenameDialog.getInstance(requireContext(), requireActivity(), (keys, objectsF) -> {
                        if (Objects.equals(keys, "rename")) {
                            boolean isSuccess = (boolean) objectsF[0];
                            if (isSuccess){
                                loadData();
                            }
                            else {
                                Toast.makeText(requireActivity(), "Error Rename", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setFilePath(fileModel.getFileName());
                    RenameDialog.showUI();
                    break;
                case R.id.details:
                    viewDetails(fileModel.getUri());
                    break;
                case R.id.delete:
                    AppUtils.deleteFile(fileModel.getUri());
                    break;

            }

            return true;
        });

        popup.show();
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
    @SuppressWarnings("unchecked")
    @Override
    public void onViewAvailable(String key, Object... objects) {
        requireActivity().runOnUiThread(() -> {

            if (Objects.equals(key, "loadFile")) {
                list.clear();
                list.addAll((Collection<? extends MusicManager>) objects[0]);
                onFileAvailable();
            }
        });
    }

    private void onFileAvailable() {
        isEmpty = list.isEmpty();
        if (isEmpty) {
            showViewEmpty();
        }
        else hideViewEmpty();
        musicAdapter.update(list);
    }

    @Override
    public void onViewNotAvailable(String mess) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
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
