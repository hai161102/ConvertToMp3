package com.haiprj.converttomp3.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentTransaction;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.android_app_lib.ui.BaseDialog;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.AppCallback;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMainMp3FilesBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.activity.MainActivity;
import com.haiprj.converttomp3.ui.activity.PlayMusicActivity;
import com.haiprj.converttomp3.ui.adapter.MusicAdapter;
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
import java.util.Random;

public class MainMp3Fragment extends BaseFragment<FragmentMainMp3FilesBinding> implements ViewResult {

    private final List<FileModel> listMp3 = new ArrayList<>();
    private AppDataPresenter dataPresenter;
    private Mp3Fragment mp3Fragment;

    @Override
    protected void initView() {
        dataPresenter = new AppDataPresenter(this);
        mp3Fragment = new Mp3Fragment(listMp3);
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mp3View, mp3Fragment).commit();
        loadData();
        mp3Fragment.setCallback((action, objects) -> {
            if (Objects.equals(action, MusicAdapter.CLICK_MORE)) {
                showMore(objects);
            }
            if (Objects.equals(action, MusicAdapter.CLICK)) {
                List<String> listJson = new ArrayList<>();
                listMp3.forEach(fileModel -> {
                    listJson.add(AppUtils.convertToJson(fileModel));
                });
                FileModel fileModel = (FileModel) objects[0];
                PlayMusicActivity.start(requireContext(), AppUtils.convertToJson(fileModel), listJson, false);
//                    PlayMusicActivity.start(MainActivity.this, fileModel, new AppCallback() {
//                        @Override
//                        public void action(String action, Object... objectsf) {
//                        }
//                    });
            }
        });

    }

    @Override
    protected void addEvent() {
        binding.random.setOnClickListener(v -> {
            List<String> listJson = new ArrayList<>();
            Random random = new Random();
            int index = random.nextInt(listMp3.size());
            listMp3.forEach(fileModel -> {
                listJson.add(AppUtils.convertToJson(fileModel));
            });
            PlayMusicActivity.start(requireContext(), AppUtils.convertToJson(listMp3.get(index)), listJson, true);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_mp3_files;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAvailable(String key, Object... objects) {
        requireActivity().runOnUiThread(() -> {
            if (Objects.equals(key, "loadFile")){
                listMp3.clear();
                listMp3.addAll((Collection<? extends FileModel>) objects[0]);
                mp3Fragment.updateList(listMp3);
            }
            if (Objects.equals(key, Const.RENAME)) {
                loadData();
            }
            if (Objects.equals(key, Const.DELETE)) {
                loadData();
            }
        });
    }

    @Override
    public void onViewNotAvailable(String mess) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireContext(), mess, Toast.LENGTH_LONG).show();
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void showMore(Object[] objects) {
        FileModel fileModel = (FileModel) objects[0];

        AppUtils.showPopupMenu(requireContext(), R.menu.more_popup, (View) objects[1], menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.share:
                    AppUtils.shareFile(requireContext(), fileModel.getFileUri());
                    break;
                case R.id.rename:
                    File file = FileUtils.getFileFromUri(requireContext(), fileModel.getFileUri());
                    AppUtils.rename(requireContext(), requireActivity(), file.getPath(), dataPresenter);
                    break;
                case R.id.addFavourite:
                    App.getAppRoomDatabase(requireContext()).favouriteDao().insert(fileModel);
                    break;
                case R.id.details:
                    viewDetails(fileModel);
                    break;
                case R.id.delete:
                    AppUtils.delete(requireContext(), requireActivity(), FilePath.getPath(requireContext(), fileModel.getFileUri()), dataPresenter);
//                    AppUtils.deleteFile(fileModel.getFileUri());
                    break;

            }
        });

    }

    private void viewDetails(FileModel fileModel) {
        DetailsDialog detailsDialog = new DetailsDialog(requireContext(), requireActivity(), (key, objects) -> {

        }, fileModel);
        detailsDialog.show();
    }

    public void loadData() {
        if (isPermissionGranted)
            dataPresenter.loadFile(requireContext(), Const.LOAD_MP3);
    }


    private boolean isPermissionGranted;

    public void setPermissionGranted(boolean permissionGranted) {
        isPermissionGranted = permissionGranted;
    }
}
