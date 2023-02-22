package com.haiprj.converttomp3.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentTransaction;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
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
import com.haiprj.converttomp3.ui.dialog.RenameDialog;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MainMp3Fragment extends BaseFragment<FragmentMainMp3FilesBinding> implements ViewResult {

    private List<FileModel> listMp3 = new ArrayList<>();
    private AppDataPresenter dataPresenter;
    private Mp3Fragment mp3Fragment;
    @Override
    protected void initView() {
        dataPresenter = new AppDataPresenter(this);
        loadData();
        mp3Fragment = new Mp3Fragment(listMp3);
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
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.mp3View, mp3Fragment).commit();
    }

    @Override
    protected void addEvent() {

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
        });
    }

    @Override
    public void onViewNotAvailable(String mess) {
        requireActivity().runOnUiThread(() -> {
            if (Objects.equals(mess, Const.PERMISSION_NOT_GRANTED)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    if (!PermissionUtil.isPermissionGranted(requireContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
                        PermissionUtil.requestPermission(requireActivity(), Const.REQUEST_MANAGE_STORAGE_ID, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
                    }
                }
                else {
                    if (!PermissionUtil.isPermissionGranted(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                        PermissionUtil.requestPermission(requireActivity(), Const.REQUEST_MANAGE_STORAGE_ID, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    else if (!PermissionUtil.isPermissionGranted(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        PermissionUtil.requestPermission(requireActivity(), Const.REQUEST_MANAGE_STORAGE_ID, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void showMore(Object[] objects) {
        FileModel fileModel = (FileModel) objects[0];
        PopupMenu popup = new PopupMenu(requireContext(), (View) objects[1]);
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
                    RenameDialog.getInstance(requireContext(), requireActivity(), (keys, objectsF) -> {
                        if (Objects.equals(keys, "rename")) {
                            boolean isSuccess = (boolean) objectsF[0];
                            if (isSuccess){
                                loadData();
                            }
                            else {
                                Toast.makeText(requireContext(), "Error Rename", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setFilePath(fileModel.getDisplayName());
                    RenameDialog.showUI();
                    break;
                case R.id.addFavourite:
                    App.getAppRoomDatabase(requireContext()).favouriteDao().insert(fileModel);
                    break;
                case R.id.details:
                    viewDetails(fileModel);
                    break;
                case R.id.delete:
                    AppUtils.deleteFile(fileModel.getFileUri());
                    break;

            }

            return true;
        });

        popup.show();
    }

    private void viewDetails(FileModel fileModel) {

    }

    private void loadData() {
        dataPresenter.loadFile(requireContext(), Const.LOAD_MP3);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Const.REQUEST_MANAGE_STORAGE_ID) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
