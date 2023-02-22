package com.haiprj.converttomp3.ui.fragment;

import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.AppCallback;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentListMusicBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.ui.adapter.MusicAdapter;
import com.haiprj.converttomp3.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListMusicFragment extends BaseFragment<FragmentListMusicBinding>{

    private Mp3Fragment fragmentList;
    private Mp3Fragment fragmentFavourite;

    private List<FileModel> currentList = new ArrayList<>();
    public ListMusicFragment(List<FileModel> list) {
        this.currentList = list;
    }

    private AppCallback callback;
    private final ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            binding.allMusics.setTextColor(requireContext().getColor(R.color.white));
            binding.favouriteMusic.setTextColor(requireContext().getColor(R.color.white));
            if (position == 0) {
                setupViewOnPageChange(binding.allMusics);
            }
            if (position == 1) {
                setupViewOnPageChange(binding.favouriteMusic);
            }

        }
    };
    private AppCallback appCallback = new AppCallback() {
        @Override
        public void action(String action, Object... objects) {
            callback.action(action, objects);

        }
    };

    public void setCallback(AppCallback callback) {
        this.callback = callback;
    }

    private void setupViewOnPageChange(TextView textView) {
        textView.setTextColor(requireContext().getColor(R.color.app_color));
    }

    @Override
    protected void initView() {

        this.fragmentList  = new Mp3Fragment(currentList);
        this.fragmentFavourite  = new Mp3Fragment(App.getAppRoomDatabase(requireContext()).favouriteDao().getAllFavourite());
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(requireActivity());
        fragmentList.setCallback(appCallback);
        fragmentFavourite.setCallback(appCallback);
        viewPagerAdapter.add(fragmentList);
        viewPagerAdapter.add(fragmentFavourite);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.setUserInputEnabled(true);
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback);
    }

    @Override
    protected void addEvent() {
        binding.allMusics.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(0);
        });

        binding.favouriteMusic.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback);
    }

    public List<FileModel> getList() {
        return fragmentList.getList();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_music;
    }
}
