package com.sight.water.whaterviewdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sight.water.whaterviewdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sight-WXC on 2016/6/29 0029.
 * 自定义的点赞的View
 * 还要多写几种模式，
 *
 */
public class HeartView extends View {


    public final String TAG = HeartView.class.getSimpleName();

    /**
     * 存放的图片的随机数
     */
    public Bitmap[] drawable;
    /**
     * 随机数
     */
    public Random random = new Random();
    /**
     *
     */
    public List<Heart> mList = new ArrayList<>();

    public final int Code = 0x0000002;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Code
                    ) {
                invalidate();
            }
        }
    };

    private int witdth;

    private int height;

    private int bitWidth;

    private int bitHeight;

    /**
     * 持续时间
     */
    private long stepTime = 500;


    /**
     * 测量路径的坐标位置
     */
    private PathMeasure pathMeasure = null;

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {

        drawable = new Bitmap[3];
        drawable[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.pl_blue);
        drawable[1] = BitmapFactory.decodeResource(getResources(), R.mipmap.pl_red);
        drawable[2] = BitmapFactory.decodeResource(getResources(), R.mipmap.pl_yellow);
        bitWidth = drawable[0].getWidth();
        bitHeight = drawable[0].getHeight();
        pathMeasure = new PathMeasure();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        witdth = getMeasuredWidth();
        height = getMeasuredHeight();
        Log.e(TAG, "witdth:" + witdth + "heigt:" + height);
        setMeasuredDimension(witdth, height);
    }

    /**
     * 添加爱心
     */
    public void addHeart() {

        Heart art = new Heart();
        art.setX((witdth - bitWidth) / 2);
        art.setY(height - bitHeight);
        art.setTime(stepTime * 30);
        art.setBitmap(drawable[random.nextInt(3)]);
        mList.add(art);
        handler.removeMessages(Code);//清理之前的message
        handler.sendEmptyMessageDelayed(Code, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                Heart heart = mList.get(i);
                long remian = heart.getTime() - stepTime;
                if (remian > 0) {
                    Bitmap bitmp = heart.getBitmap();
                    canvas.drawBitmap(bitmp, heart.getX(), heart.getY(), new Paint());
                    Path path = new Path();
                    heart.setTime(remian);
                    int x = heart.getX();
                    int y = heart.getY();

                    if (x > 0 && x < witdth - bitWidth) {
                        x = x + random.nextInt(20) * (random.nextBoolean() ? 1 : -1);
                    }
                    if (y > 0 && y <= height - bitHeight) {
                        y -= 50 - random.nextInt(50);
                    }

                    heart.setX(x);
                    heart.setY(y);
                } else {
                    mList.remove(heart);
                }

            }
            handler.sendEmptyMessageDelayed(Code, 150);
        }


    }

    public class Heart {
        public int x;
        public int y;
        public long time;
        public Bitmap bitmap;

        public Path path;


        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

}
