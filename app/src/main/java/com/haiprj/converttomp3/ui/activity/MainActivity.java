package com.haiprj.converttomp3.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager2.widget.ViewPager2;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.android_app_lib.ui.BaseDialog;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.AppCallback;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivityMainBinding;
import com.haiprj.converttomp3.interfaces.FragmentListener;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.adapter.MusicAdapter;
import com.haiprj.converttomp3.ui.adapter.ViewPagerAdapter;
import com.haiprj.converttomp3.ui.dialog.DetailsDialog;
import com.haiprj.converttomp3.ui.dialog.RenameDialog;
import com.haiprj.converttomp3.ui.fragment.MainMp3Fragment;
import com.haiprj.converttomp3.ui.fragment.Mp3Fragment;
import com.haiprj.converttomp3.ui.fragment.Mp4Fragment;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.PermissionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    private final Mp4Fragment mp4Fragment = new Mp4Fragment();
    private final MainMp3Fragment mp3Fragment = new MainMp3Fragment();
    private boolean isLoadAudio = false;


    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            requestPermission();
            setViewAt(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    private void setViewAt(int position) {
        binding.mp4Files.setBackgroundColor(Color.TRANSPARENT);
        binding.mp3Files.setBackgroundColor(Color.TRANSPARENT);
        switch (position) {
            case 0:
                setViewMp4();
                break;
            case 1:
                setViewMp3();
        }
    }

    private void setViewMp3() {
        binding.mp3Files.setBackgroundResource(R.drawable.shape_search);
        binding.mp3Files.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
        if (isLoadAudio) {
            mp3Fragment.loadData();
            isLoadAudio = false;
        }
    }

    private void setViewMp4() {
        binding.mp4Files.setBackgroundResource(R.drawable.shape_search);
        binding.mp4Files.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
    }

    @Override
    protected void initView() {
        //requestPermission();
        requestPermission();
        setupViewPager();
    }

    private void requestPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mp4Fragment.setPermissionGranted(true);
            mp3Fragment.setPermissionGranted(true);
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, Const.REQUEST_MANAGE_STORAGE_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Const.REQUEST_MANAGE_STORAGE_ID) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mp4Fragment.setPermissionGranted(true);
                    mp3Fragment.setPermissionGranted(true);
                } else {
                    requestPermission();
//                    if () {
//                        Toast.makeText(this, permissions[0], Toast.LENGTH_LONG).show();
//                    }
//                    if () {
//                        Toast.makeText(this, permissions[1], Toast.LENGTH_LONG).show();
//                    }
//                    for (int i : grantResults) {
//                        if (i != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(MainActivity.this, i + " is not granted", Toast.LENGTH_LONG).show();
//                        }
//                    }
                }
            }
        }
    }

    private void setupViewPager() {
        mp4Fragment.setListener(new FragmentListener() {
            @Override
            public void onConvertDone(Object... object) {
                binding.viewPager.setCurrentItem(1);
                isLoadAudio = true;

            }

            @Override
            public void onConvertFailed(String mess) {

            }
        });
//        mp3Fragment.setCallback(new AppCallback() {
//            }
//        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.add(mp4Fragment);
        viewPagerAdapter.add(mp3Fragment);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback);
        binding.viewPager.setUserInputEnabled(false);
    }

    @Override
    protected void addEvent() {
        binding.mp4Files.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(0);
        });
        binding.mp3Files.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
            isLoadAudio = true;
        });
    }

//    @SuppressLint("NonConstantResourceId")
//    private void showMore(Object[] objects) {
//        FileModel fileModel = (FileModel) objects[0];
//        PopupMenu popup = new PopupMenu(this, (View) objects[1]);
//        //Inflating the Popup using xml file
//        popup.getMenuInflater()
//                .inflate(R.menu.more_popup, popup.getMenu());
//
//        //registering popup with OnMenuItemClickListener
//        popup.setOnMenuItemClickListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.share:
//                    AppUtils.shareFile(this, fileModel.getFileUri());
//                    break;
//                case R.id.rename:
//                    RenameDialog.getInstance(MainActivity.this, MainActivity.this, (keys, objectsF) -> {
//                        if (Objects.equals(keys, "rename")) {
//                            boolean isSuccess = (boolean) objectsF[0];
//                            if (isSuccess){
//                                loadData();
//                            }
//                            else {
//                                Toast.makeText(MainActivity.this, "Error Rename", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }).setFilePath(fileModel.getDisplayName());
//                    RenameDialog.showUI();
//                    break;
//                case R.id.addFavourite:
//                    App.getAppRoomDatabase(MainActivity.this).favouriteDao().insert(fileModel);
//                    break;
//                case R.id.details:
//                    viewDetails(fileModel);
//                    break;
//                case R.id.delete:
//                    AppUtils.deleteFile(fileModel.getFileUri());
//                    break;
//
//            }
//
//            return true;
//        });
//
//        popup.show();
//    }

    private void viewDetails(FileModel fileModel) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
    }
}