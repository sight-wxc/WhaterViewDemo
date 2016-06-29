package com.sight.water.whaterviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * Created by Sight on 2016/4/28.
 */
public class WaterListView extends ListView
{


    private int mMaxScrollY = 50;

    private Context context;


    public WaterListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }


    public WaterListView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public WaterListView(Context context)
    {
        this(context, null);
    }


    private void initView()
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int denisty = (int) metrics.density;
        this.mMaxScrollY = denisty * mMaxScrollY;
  }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return  super.overScrollBy(deltaX,deltaY,scrollX,scrollY,scrollRangeX,scrollRangeY,maxOverScrollX,mMaxScrollY,isTouchEvent);
    }

}
