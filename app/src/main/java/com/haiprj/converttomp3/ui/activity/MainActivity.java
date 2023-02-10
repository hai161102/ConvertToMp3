package com.haiprj.converttomp3.ui.activity;

import android.Manifest;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivityMainBinding;
import com.haiprj.converttomp3.ui.adapter.ViewPagerAdapter;
import com.haiprj.converttomp3.ui.fragment.Mp3Fragment;
import com.haiprj.converttomp3.ui.fragment.Mp4Fragment;
import com.haiprj.converttomp3.utils.PermissionUtil;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private final Mp4Fragment mp4Fragment = new Mp4Fragment();
    private final Mp3Fragment mp3Fragment = new Mp3Fragment();


    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
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
    }

    private void setViewMp4() {
        binding.mp4Files.setBackgroundResource(R.drawable.shape_search);
        binding.mp4Files.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
    }

    @Override
    protected void initView() {
        //requestPermission();
        setupViewPager();
    }

    private void requestPermission() {
        if (PermissionUtil.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtil.requestPermission(this, 1, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.add(mp4Fragment);
        viewPagerAdapter.add(mp3Fragment);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback);
        binding.viewPager.setUserInputEnabled(true);
    }

    @Override
    protected void addEvent() {
        binding.mp4Files.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(0);
        });
        binding.mp3Files.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
        });
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