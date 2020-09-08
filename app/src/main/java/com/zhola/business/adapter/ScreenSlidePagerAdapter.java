package com.zhola.business.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zhola.business.fragment.BasicInfoFragment;
import com.zhola.business.fragment.DocumentFragment;
import com.zhola.business.fragment.EssentialFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BasicInfoFragment();
            case 1:
                return new EssentialFragment();
            case 2:
                return new DocumentFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}