package com.haiprj.converttomp3.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;


import androidx.viewpager2.widget.ViewPager2;

import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.AppCallback;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivityPlayMusicBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.ui.adapter.MusicAdapter;
import com.haiprj.converttomp3.ui.adapter.ViewPagerAdapter;
import com.haiprj.converttomp3.ui.fragment.FragmentLyric;
import com.haiprj.converttomp3.ui.fragment.ListMusicFragment;
import com.haiprj.converttomp3.ui.fragment.MediaControlFragment;
import com.haiprj.converttomp3.ui.fragment.Mp3Fragment;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.ReplayState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PlayMusicActivity extends BaseActivity<ActivityPlayMusicBinding> {

    private FileModel fileModel;

    private MediaControlFragment mediaControlFragment;
    private ListMusicFragment listMusicFragment;
    private final List<FileModel> currentListFile = new ArrayList<>();
    private final FragmentLyric fragmentLyric = new FragmentLyric();

    public List<FileModel> randomList = new ArrayList<>();

    private boolean isRandom = false;

    public boolean isRandom() {
        return isRandom;
    }

    private String json = "";
    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };
    private Random random;

    public static void start(Context context, String jsonObject, List<String> listJson, boolean isRandom) {
        Intent starter = new Intent(context, PlayMusicActivity.class);
        starter.putExtra("random", isRandom);
        starter.putExtra("object", jsonObject);
        starter.putStringArrayListExtra("list", (ArrayList<String>) listJson);
        context.startActivity(starter);
    }
    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        random = new Random();
        isRandom = getIntent().getBooleanExtra("random", false);
        json = getIntent().getStringExtra("object");
        fileModel = AppUtils.convertFromJson(json, FileModel.class);
        List<String> listJson = getIntent().getStringArrayListExtra("list");
        listJson.forEach(j -> {
            currentListFile.add(AppUtils.convertFromJson(j, FileModel.class));
        });

        setupRandomList();
        setupViewPager();

    }

    private void setupRandomList() {

        randomList.clear();
        randomList.add(0, fileModel);
        while (randomList.size() < currentListFile.size()) {
            randomMusic(currentListFile, randomList);
        }
    }


    private void randomMusic(final List<FileModel> list, final List<FileModel> randomLists){
        int index = random.nextInt(list.size());
        if (randomLists.contains(list.get(index))){
            randomMusic(list, randomLists);
        }
        else {
            randomLists.add(list.get(index));
        }
    }
    private void setupViewPager() {
        mediaControlFragment = new MediaControlFragment(getCurrentFileModel());
        listMusicFragment = new ListMusicFragment(isRandom ? randomList : currentListFile);
        mediaControlFragment.setCallback((action, objects) -> {
            if (Objects.equals(action, "next")){
                if (objects.length == 1) {
                    FileModel fileModel = (FileModel) objects[0];
                    int i = listMusicFragment.getList().indexOf(fileModel);
                    if (i < listMusicFragment.getList().size() - 1) {
                        mediaControlFragment.loadData(listMusicFragment.getList().get(i + 1));

                    }
                    else {
                        mediaControlFragment.loadData(listMusicFragment.getList().get(0));
                    }
                    setCurrentFileModel(fileModel);
                }
                if (objects.length == 2) {
                    FileModel fileModel = (FileModel) objects[0];
                    ReplayState replayState = (ReplayState) objects[1];
                    int i = listMusicFragment.getList().indexOf(fileModel);
                    switch (replayState) {
                        case NONE:
                            if (i == listMusicFragment.getList().size() - 1)
                                break;
                        case REPLAY_ALL:
                            if (i < listMusicFragment.getList().size() - 1) {
                                mediaControlFragment.loadData(listMusicFragment.getList().get(i + 1));

                            }
                            else {
                                mediaControlFragment.loadData(listMusicFragment.getList().get(0));
                            }
                            break;
                    }
                    setCurrentFileModel(fileModel);
                }
            }

            if (Objects.equals(action, "previous")) {
                FileModel fileModel = (FileModel) objects[0];
                int i = listMusicFragment.getList().indexOf(fileModel);
                if (i > 0) {
                    mediaControlFragment.loadData(listMusicFragment.getList().get(i - 1));
                }
                else
                {
                    mediaControlFragment.loadData(listMusicFragment.getList().get(listMusicFragment.getList().size() - 1));
                }
                setCurrentFileModel(fileModel);
            }
            if (Objects.equals(action, "finish")){
                PlayMusicActivity.this.finish();
            }
            if (Objects.equals(action, "random")){
                isRandom = !isRandom;
                if (isRandom) {
                    setupRandomList();
                }
                else {
                    randomList.clear();
                    randomList.addAll(currentListFile);
                }
                listMusicFragment.updateList(randomList);
            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        listMusicFragment.setCallback(new AppCallback() {
            @Override
            public void action(String action, Object... objects) {
                if (Objects.equals(action, MusicAdapter.CLICK)) {
                    mediaControlFragment.loadData((FileModel) objects[0]);
                    binding.viewPager.setCurrentItem(1);
                    setCurrentFileModel((FileModel) objects[0]);
                }

                if (Objects.equals(action, MusicAdapter.CLICK_MORE)) {

                }


            }
        });
        viewPagerAdapter.add(listMusicFragment);
        viewPagerAdapter.add(mediaControlFragment);
        viewPagerAdapter.add(fragmentLyric);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback);
        binding.viewPager.setUserInputEnabled(true);
        binding.viewPager.setCurrentItem(1);
    }

    private FileModel getCurrentFileModel() {
        return fileModel;
    }

    private void setCurrentFileModel(FileModel model) {
        fileModel = model;
    }
    @Override
    protected void addEvent() {




    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileModel = null;
        randomList.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_music;
    }
}
