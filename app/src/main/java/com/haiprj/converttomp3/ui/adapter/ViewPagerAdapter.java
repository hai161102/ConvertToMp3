package com.haiprj.converttomp3.ui.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> list = new ArrayList<>();


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }




    public void add(Fragment fragment) {
        list.add(fragment);
    }

    public void remove(Fragment fragment) {
        list.remove(fragment);
    }


    @NonNull
    @Override
    public androidx.fragment.app.Fragment createFragment(int position) {
        return list.get(position);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}
