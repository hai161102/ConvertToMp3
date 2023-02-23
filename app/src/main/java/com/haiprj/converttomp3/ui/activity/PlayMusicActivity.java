package com.haiprj.converttomp3.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


import androidx.viewpager2.widget.ViewPager2;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.android_app_lib.ui.BaseDialog;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.AppCallback;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivityPlayMusicBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.adapter.MusicAdapter;
import com.haiprj.converttomp3.ui.adapter.ViewPagerAdapter;
import com.haiprj.converttomp3.ui.dialog.DetailsDialog;
import com.haiprj.converttomp3.ui.fragment.FragmentLyric;
import com.haiprj.converttomp3.ui.fragment.ListMusicFragment;
import com.haiprj.converttomp3.ui.fragment.MediaControlFragment;
import com.haiprj.converttomp3.ui.fragment.Mp3Fragment;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.FilePath;
import com.haiprj.converttomp3.utils.FileUtils;
import com.haiprj.converttomp3.utils.ReplayState;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PlayMusicActivity extends BaseActivity<ActivityPlayMusicBinding> implements ViewResult {

    private FileModel fileModel;

    private MediaControlFragment mediaControlFragment;
    private ListMusicFragment listMusicFragment;
    private final List<FileModel> currentListFile = new ArrayList<>();
    private final FragmentLyric fragmentLyric = new FragmentLyric();

    public List<FileModel> randomList = new ArrayList<>();

    private boolean isRandom = false;
    private AppDataPresenter dataPresenter;

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
        dataPresenter = new AppDataPresenter(this);
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


    private void randomMusic(final List<FileModel> list, final List<FileModel> randomLists) {
        int index = random.nextInt(list.size());
        if (randomLists.contains(list.get(index))) {
            randomMusic(list, randomLists);
        } else {
            randomLists.add(list.get(index));
        }
    }

    private void setupViewPager() {
        mediaControlFragment = new MediaControlFragment(getCurrentFileModel());
        listMusicFragment = new ListMusicFragment(isRandom ? randomList : currentListFile);
        mediaControlFragment.setCallback((action, objects) -> {

            if (Objects.equals(action, "next")) {
                FileModel model = (FileModel) objects[0];

                mediaControlFragment.loadData(getNextMusic(model));
                setCurrentFileModel(getNextMusic(model));
            }

            else if (Objects.equals(action, MediaControlFragment.NEXT_AND_NONE)) {
                FileModel model = (FileModel) objects[0];
                if (listMusicFragment.getList().indexOf(model) >= listMusicFragment.getList().size() - 1) {
                    return;
                }
                mediaControlFragment.loadData(getNextMusic(model));
                setCurrentFileModel(getNextMusic(model));
            }

            else if (Objects.equals(action, MediaControlFragment.NEXT_AND_REPLAY_ALL)) {
                FileModel model = (FileModel) objects[0];

                mediaControlFragment.loadData(getNextMusic(model));
                setCurrentFileModel(getNextMusic(model));
            }

            else if (Objects.equals(action, MediaControlFragment.NEXT_AND_REPLAY_ONE)) {
                FileModel model = (FileModel) objects[0];
                mediaControlFragment.loadData(model);
                setCurrentFileModel(model);
            }

            else if (Objects.equals(action, "previous")) {
                FileModel model = (FileModel) objects[0];

                mediaControlFragment.loadData(getPreviousMusic(model));
                setCurrentFileModel(getPreviousMusic(model));
            }
            else if (Objects.equals(action, "finish")) {
                PlayMusicActivity.this.finish();
            }
            else if (Objects.equals(action, "random")) {
                isRandom = !isRandom;
                if (isRandom) {
                    setupRandomList();
                } else {
                    randomList.clear();
                    randomList.addAll(currentListFile);
                }
                listMusicFragment.updateList(randomList);
            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        listMusicFragment.setCallback(new AppCallback() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void action(String action, Object... objects) {
                if (Objects.equals(action, MusicAdapter.CLICK)) {
                    mediaControlFragment.loadData((FileModel) objects[0]);
                    binding.viewPager.setCurrentItem(1);
                    setCurrentFileModel((FileModel) objects[0]);
                }

                if (Objects.equals(action, MusicAdapter.CLICK_MORE)) {

                    FileModel model = (FileModel) objects[0];
                    AppUtils.showPopupMenu((Context) PlayMusicActivity.this, R.menu.more_popup, (View) objects[1], menuItem -> {
                        switch (menuItem.getItemId()) {
                            case R.id.share:
                                AppUtils.shareFile(PlayMusicActivity.this, model.getFileUri());
                                break;
                            case R.id.rename:
                                File file = FileUtils.getFileFromUri(PlayMusicActivity.this, model.getFileUri());
                                AppUtils.rename(PlayMusicActivity.this, PlayMusicActivity.this, file.getPath(), dataPresenter);
                                break;
                            case R.id.addFavourite:
                                App.getAppRoomDatabase(PlayMusicActivity.this).favouriteDao().insert(model);
                                break;
                            case R.id.details:
                                viewDetails(model);
                                break;
                            case R.id.delete:
                                AppUtils.delete(PlayMusicActivity.this, PlayMusicActivity.this, FilePath.getPath(PlayMusicActivity.this, model.getFileUri()), dataPresenter);
//                                AppUtils.deleteFile(fileModel.getFileUri());
                                break;

                        }
                    });
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

    private FileModel getNextMusic(FileModel current){
        if (listMusicFragment.getList().indexOf(current) < listMusicFragment.getList().size() - 1)
            return listMusicFragment.getList().get(listMusicFragment.getList().indexOf(current) + 1);
        else
            return listMusicFragment.getList().get(0);
    }

    private FileModel getPreviousMusic(FileModel current) {
        if (listMusicFragment.getList().indexOf(current) > 0)
            return listMusicFragment.getList().get(listMusicFragment.getList().indexOf(current) - 1);
        else
            return listMusicFragment.getList().get(listMusicFragment.getList().size() - 1);
    }

    private void viewDetails(FileModel fileModel) {
        DetailsDialog detailsDialog = new DetailsDialog(this, this, new BaseDialog.OnActionDialogCallback() {
            @Override
            public void callback(String key, Object... objects) {

            }
        }, fileModel);
        detailsDialog.show();
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

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAvailable(String key, Object... objects) {
        if (Objects.equals(key, "loadFile")) {
            currentListFile.clear();
            currentListFile.addAll((Collection<? extends FileModel>) objects[0]);
            if (isRandom) {
                setupRandomList();
            } else {
                randomList.clear();
                randomList.addAll(currentListFile);
            }
            listMusicFragment.updateList(randomList);
        }
        if (Objects.equals(key, Const.RENAME)) {
            dataPresenter.loadFile(this, Const.LOAD_MP3);
        }
        if (Objects.equals(key, Const.DELETE)) {
            dataPresenter.loadFile(this, Const.LOAD_MP3);
        }
    }

    @Override
    public void onViewNotAvailable(String mess) {

    }
}
