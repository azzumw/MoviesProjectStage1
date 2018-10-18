package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }



    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return context.getString(R.string.trailerPageTitle);
            case 1: return context.getString(R.string.reviewPageTitle);
            default: return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FragmentTrailer();
            case 1: return  new FragmentReviews();
            default: return null;
        }
    }
}
