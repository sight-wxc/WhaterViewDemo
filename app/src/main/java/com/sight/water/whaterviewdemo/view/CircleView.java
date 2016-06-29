package com.sight.water.whaterviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sight.water.whaterviewdemo.R;

/**
 * Created by Sight on 2016/4/28.
 */
public class CircleView extends View
{


    private int circleColor;

    private int arcColor;

    private int textColor;


    private float textSize;

    private String text;

    private int startAngle;

    private int sweepAngle;

    private int mCircleXY;
    private float mRadius;
    private Paint mCirclePaint;
    private Paint mArcPaint;
    private Paint mTextPaint;

    private RectF mRectF;


    private int mScreenWidth;

    private int mScreenHeight;

    public CircleView(Context context)
    {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.circleView);
        if (ta != null) {
            circleColor = ta.getColor(R.styleable.circleView_circleColor, 0);
            arcColor = ta.getColor(R.styleable.circleView_arcColor, 0);
            textColor = ta.getColor(R.styleable.circleView_textColor, 0);
            textSize = ta.getDimension(R.styleable.circleView_textSize, 50);
            text = ta.getString(R.styleable.circleView_text);
            startAngle = ta.getInt(R.styleable.circleView_startAngle, 0);
            sweepAngle = ta.getInt(R.styleable.circleView_sweepAngle, 90);
            ta.recycle();

        }
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);


        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }


    @Override
    public void onMeasure(int width, int height)
    {
        super.onMeasure(width, height);

        mScreenWidth = this.getMeasuredWidth();
        mScreenHeight = this.getMeasuredHeight();
        init(mScreenWidth, mScreenHeight);
    }

    private void init(int width, int height)
    {
        int length = Math.min(width, height);
        mCircleXY = length / 2;
        mRadius = length * 0.5f / 2;
        Log.e("width", width + "height" + height + "xy" + mCircleXY);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleColor);

        mRectF = new RectF(length * 0.1f, length * 0.1f, length * 0.9f, length * 0.9f);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(arcColor);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(width * 0.1f);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


    }


    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        drawSth(canvas);
        Log.e("TAG", "draw");
    }

    private void drawSth(Canvas canvas)
    {

        canvas.drawCircle(mCircleXY, mCircleXY, mRadius, mCirclePaint);
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mArcPaint);
        canvas.drawText(text, 0, text.length(), mCircleXY, mCircleXY + textSize / 4, mTextPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
    }
}
