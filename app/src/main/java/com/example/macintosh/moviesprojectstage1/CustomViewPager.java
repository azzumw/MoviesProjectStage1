package com.example.macintosh.moviesprojectstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CustomViewPager extends ViewPager {
    private View mCurrentView;
    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int currentPagePosition = 0;
//            View child = getChildAt(currentPagePosition);
//            mCurrentView = getChildAt(currentPagePosition);
            if(mCurrentView!= null){
                mCurrentView.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
                int h = mCurrentView.getMeasuredHeight();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(h,MeasureSpec.EXACTLY);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }
}
