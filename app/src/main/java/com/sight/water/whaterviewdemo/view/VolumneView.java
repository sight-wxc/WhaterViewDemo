package com.sight.water.whaterviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sight.water.whaterviewdemo.R;

/**
 * Created by Sight on 2016/4/28.
 */
public class VolumneView extends View
{
    private Paint mPaint;
    private int mCount=6;
    private int mWidth;
    private int mRectHeight;
    private int mRectWidth;
    private LinearGradient mLinearGradient;
    private double mRandom;
    private float mcurrentHeight;

    public static final int OFFSET = 5;

    public VolumneView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public VolumneView(Context context)
    {
        this(context, null);
    }


    public VolumneView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.circleView);
        if (ta != null) {
            mCount = ta.getInt(R.styleable.circleView_count, 6);
            ta.recycle();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mRectHeight = getMeasuredHeight();
        mRectWidth = (int) (mWidth * 0.8 / mCount);
        mLinearGradient = new LinearGradient(0, 0, mRectWidth, mRectHeight, Color.BLUE, Color.GREEN, Shader.TileMode.CLAMP);

        mPaint.setShader(mLinearGradient);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        for (int i = 0; i < mCount; i++) {
            mRandom = Math.random();
            mcurrentHeight = (float) (mRectHeight * mRandom);
            float width = (float) (mWidth * 0.4 / 2 + OFFSET);
            canvas.drawRect(width + i * mRectWidth, mcurrentHeight, width + (i + 1) * mRectWidth, mRectHeight, mPaint);
        }
        postInvalidateDelayed(300);


    }

}
