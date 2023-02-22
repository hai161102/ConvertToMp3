package com.haiprj.converttomp3.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.android_app_lib.ui.BaseDialog;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMp4FilesBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.activity.WatchVideoActivity;
import com.haiprj.converttomp3.ui.adapter.FileModelAdapter;
import com.haiprj.converttomp3.ui.dialog.DetailsDialog;
import com.haiprj.converttomp3.ui.dialog.RenameDialog;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.FilePath;
import com.haiprj.converttomp3.utils.FileUtils;
import com.haiprj.converttomp3.utils.PermissionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Mp4Fragment extends BaseFragment<FragmentMp4FilesBinding> implements ViewResult {

    private boolean isEmpty = true;

    private boolean isPermissionGranted;

    public void setPermissionGranted(boolean permissionGranted) {
        isPermissionGranted = permissionGranted;
        if (getContext() != null) {
            loadData();
        }
    }

    public static final String TAG = "Mp4Fragment";
    private final List<FileModel> list = new ArrayList<>();
    private final AppDataPresenter dataPresenter;

    private FileModelAdapter fileModelAdapter;


    public Mp4Fragment() {
        dataPresenter = new AppDataPresenter(this);
    }

    @Override
    protected void initView() {
        binding.progress.setProgressTintList(ColorStateList.valueOf(requireContext().getColor(R.color.app_color)));
        fileModelAdapter = new FileModelAdapter(requireContext());
        fileModelAdapter.setListener(new FileModelAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                WatchVideoActivity.start(requireContext(), list.get(position).getFileUri().toString());
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onMore(int position, Object object, View view) {
                FileModel fileModel = (FileModel) object;
                PopupMenu popup = new PopupMenu(requireContext(), view);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.more_popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.share:
                            AppUtils.shareFile(requireContext(), fileModel.getFileUri());
                            break;
                        case R.id.rename:
                            RenameDialog.getInstance(requireContext(), requireActivity(), (key, objects) -> {
                                if (Objects.equals(key, "rename")) {
                                    AppUtils.renameFile(requireContext(), fileModel.getFileUri());
                                }
                            }).setFilePath(fileModel.getDisplayName());
                            RenameDialog.showUI();
                            break;
                        case R.id.addFavourite:
                            break;
                        case R.id.details:
                            viewDetails(fileModel);
                            break;
                        case R.id.delete:
                            AppUtils.deleteFile(fileModel.getFileUri());
                            break;
                        case R.id.convert:
                            convertFile(fileModel.getFileUri());
                            popup.dismiss();
                            break;

                    }
                    return true;
                });

                popup.show();
            }
        });
        binding.rcvFileConvert.setAdapter(fileModelAdapter);
        if (isEmpty) {
            showViewEmpty();
        }
        loadData();
    }

    private void viewDetails(FileModel fileModel) {
        DetailsDialog detailsDialog = new DetailsDialog(requireContext(), requireActivity(), new BaseDialog.OnActionDialogCallback() {
            @Override
            public void callback(String key, Object... objects) {

            }
        }, fileModel);
        detailsDialog.show();
    }

    private void convertFile(Uri fileUri) {
//        MediaPlayer mp = MediaPlayer.create(requireContext(), fileUri);
//        int duration = mp.getDuration();
//        mp.release();
        //@SuppressWarnings("ConstantConditions") File file = new File(FilePath.getPath(requireContext(), fileUri));
        binding.frameProgress.setVisibility(View.VISIBLE);
//        dataPresenter.convertMp4ToMp3(file.getPath(),
//                App.getAppPath(requireContext()) + File.separator + file.getName().split(".mp4")[0] + ".mp3",
//                0,
//                duration,
//                true,
//                true);
        try {
            dataPresenter.convertMp4ToMp3(requireContext(), FileUtils.getFileFromUri(requireContext(), fileUri).getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Objects.equals(key, "loadFile")) {
                    list.clear();
                    list.addAll((Collection<? extends FileModel>) objects[0]);
                    onFileAvailable();
                }
                if (Objects.equals(key, "ConvertSuccess")){
                    Toast.makeText(requireContext(), key, Toast.LENGTH_LONG).show();
                    if (listener != null)
                        listener.onConvertDone(objects[0]);
                    binding.frameProgress.setVisibility(View.GONE);

                }
                if (Objects.equals(key, Const.MVP_CONVERT)) {
                    if (listener != null)
                        listener.onConvertDone(objects[0]);
                    binding.frameProgress.setVisibility(View.GONE);
                }

            }
        });

    }

    private void onFileAvailable() {
        isEmpty = list.isEmpty();
        if (isEmpty) {
            showViewEmpty();
        }
        else hideViewEmpty();
        fileModelAdapter.update(list);
//        Log.d(TAG, "onFileAvailable: " + list.size());
    }

    private void hideViewEmpty() {
        binding.emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onViewNotAvailable(String mess) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Objects.equals(mess, Const.PERMISSION_NOT_GRANTED)) {

                }
                if (listener != null)
                    listener.onConvertFailed(mess);
                Toast.makeText(requireContext(), mess, Toast.LENGTH_LONG).show();
                binding.frameProgress.setVisibility(View.GONE);
            }
        });

    }


    public void loadData() {
        if (isPermissionGranted)
            dataPresenter.loadFile(requireContext(),Const.LOAD_MP4);
    }

}
