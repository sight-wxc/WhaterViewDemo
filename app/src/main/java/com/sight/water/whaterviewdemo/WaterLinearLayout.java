package com.sight.water.whaterviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;


/**
 * 本项目是参考 --Created by 大灯泡 on 2016/1/21. 时间轴
 * 在我实际项目中改写了下 谢谢原作者
 * 并不是我的原创 在基础上改写了下 项目需要
 * 如果有侵犯权利,请与我联系,主要是给开发者一个好的项目
 * 仅供参考
 */
public class WaterLinearLayout extends LinearLayout
{
    //=============================================================line gravity常量定义
    public static final int GRAVITY_LEFT = 2;
    public static final int GRAVITY_RIGHT = 4;
    public static final int GRAVITY_MIDDLE = 0;
    public static final int GRAVITY_TOP = 1;
    public static final int GRAVITY_BOTTOM = 3;
    //=============================================================元素定义
    private Bitmap mIcon;
    //line location
    private int lineMarginSide;
    private int lineDynamicDimen;
    //line property
    private int lineStrokeWidth;
    private int lineColor;
    //point property
    private int pointSize;
    private int pointColor;

    //=============================================================paint
    private Paint linePaint;
    private Paint pointPaint;
    //=============================================================其他辅助参数
    //第一个点的位置
    private int firstX;
    private int firstY;
    //最后一个图的位置
    private int lastX;
    private int lastY;
    //默认垂直
    private int curOrientation = VERTICAL;

    //line gravity(默认垂直的左边)
    private int lineGravity = GRAVITY_LEFT;

    private Context mContext;

    //开关
    private boolean drawLine = true;

    private int rootLeft;
    private int rootMiddle;
    private int rootRight;
    private int rootTop;
    private int rootBottom;
    //参照点
    private int sideRelative;
    //最后一个点画圆
    private boolean isLastCircle;
    //是否是另一种风格
    private boolean isAntherStyle;
    //进度状态是为selectIcon;
    private Bitmap SelectIcon;
    //进度 值 为int
    private int status;
    //另一种状态颜色值
    private int anThorColor;


    public WaterLinearLayout(Context context)
    {
        this(context, null);
    }

    public WaterLinearLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public WaterLinearLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.WaterLinearLayout);
        lineMarginSide = attr.getDimensionPixelOffset(R.styleable.WaterLinearLayout_line_margin_side, 10);
        lineDynamicDimen = attr.getDimensionPixelOffset(R.styleable.WaterLinearLayout_line_dynamic_dimen, 0);
        lineStrokeWidth = attr.getDimensionPixelOffset(R.styleable.WaterLinearLayout_line_stroke_width, 2);
        lineColor = attr.getColor(R.styleable.WaterLinearLayout_line_color, 0xff3dd1a5);
        pointSize = attr.getDimensionPixelSize(R.styleable.WaterLinearLayout_point_size, 8);
        pointColor = attr.getColor(R.styleable.WaterLinearLayout_point_color, 0xff3dd1a5);
        lineGravity = attr.getInt(R.styleable.WaterLinearLayout_line_gravity, GRAVITY_LEFT);

        int iconRes = attr.getResourceId(R.styleable.WaterLinearLayout_icon_src, R.mipmap.ic_ok);
        BitmapDrawable temp = (BitmapDrawable) context.getResources().getDrawable(iconRes);
        if (temp != null) mIcon = temp.getBitmap();

        curOrientation = getOrientation();
        attr.recycle();
        setWillNotDraw(false);
        initView(context);
    }

    private void initView(Context context)
    {
        this.mContext = context;

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineStrokeWidth);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setDither(true);
        pointPaint.setColor(pointColor);
        pointPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        calculateSideRelative();
        if (drawLine) {
            Log.e("draw_line", "line");
            drawTimeLine(canvas);
        }
    }

    private void calculateSideRelative()
    {
        rootLeft = getLeft();
        rootTop = getTop();
        rootRight = getRight();
        rootBottom = getBottom();
        if (curOrientation == VERTICAL) rootMiddle = (rootLeft + rootRight) >> 1;
        if (curOrientation == HORIZONTAL) rootMiddle = (rootTop + rootBottom) >> 1;

        boolean isCorrect = (lineGravity == GRAVITY_MIDDLE || (lineGravity + curOrientation) % 2 != 0);
        if (isCorrect) {
            switch (lineGravity) {
                case GRAVITY_TOP:
                    sideRelative = rootTop;
                    break;
                case GRAVITY_BOTTOM:
                    sideRelative = rootBottom;
                    break;
                case GRAVITY_LEFT:
                    sideRelative = rootLeft;
                    break;
                case GRAVITY_RIGHT:
                    sideRelative = rootRight;
                    break;
                case GRAVITY_MIDDLE:
                    sideRelative = rootMiddle;
                    break;
            }
        } else {
            sideRelative = 0;
        }
    }

    private void drawTimeLine(Canvas canvas)
    {
        int childCount = getChildCount();

        if (childCount > 0) {
            //大于1，证明至少有2个，也就是第一个和第二个之间连成线，第一个和最后一个分别有点/icon
            if (childCount > 1) {
                switch (curOrientation) {
                    case VERTICAL:
                        drawFirstChildViewVertical(canvas);
                        drawLastChildViewVertical(canvas);
                        drawBetweenLineVertical(canvas);
                        break;
                    case HORIZONTAL:
                        drawFirstChildViewHorizontal(canvas);
                        drawLastChildViewHorizontal(canvas);
                        drawBetweenLineHorizontal(canvas);
                        break;
                    default:
                        break;
                }
            } else if (childCount == 1) {
                switch (curOrientation) {
                    case VERTICAL:
                        drawFirstChildViewVertical(canvas);
                        if (isAntherStyle) {
                            drawBetweenLineVertical(canvas);
                        } else {
                            int boom = getChildAt(0).getBottom();
                            //画剩下的
                            canvas.drawLine(firstX, firstY + pointSize, firstX, boom, linePaint);
                        }
                        break;
                    case HORIZONTAL:
                        drawFirstChildViewHorizontal(canvas);
                        break;
                    default:
                        break;

                }
            }
        }
    }

    //=============================================================Vertical Draw
    private void drawFirstChildViewVertical(Canvas canvas)
    {
        if (getChildAt(0) != null) {
            int top = getChildAt(0).getTop();
            //记录值
            firstX = sideRelative >= rootMiddle ? (sideRelative - lineMarginSide) : (sideRelative + lineMarginSide);
            firstY = top + getChildAt(0).getPaddingTop() + lineDynamicDimen;

            if (isAntherStyle) {
                firstX = (sideRelative >= rootMiddle ? (sideRelative - lineMarginSide) : (sideRelative + lineMarginSide)) - (SelectIcon
                        .getWidth() >> 1);
                canvas.drawBitmap(SelectIcon, firstX, firstY, null);
            } else {
                //画一个圆
                canvas.drawCircle(firstX, firstY, pointSize, pointPaint);
            }

        }
    }

    private void drawLastChildViewVertical(Canvas canvas)
    {
        if (getChildAt(getChildCount() - 1) != null) {
            int top = getChildAt(getChildCount() - 1).getTop();
            int boom = getChildAt(getChildCount() - 1).getBottom();
            //记录值
            lastX = (sideRelative >= rootMiddle ? (sideRelative - lineMarginSide) : (sideRelative + lineMarginSide)) - (mIcon
                    .getWidth() >> 1);
            lastY = top + getChildAt(getChildCount() - 1).getPaddingTop() + lineDynamicDimen;


            if (isAntherStyle) {
                boolean isAttach = status >= getChildCount() - 1;
                Bitmap iconDraw = isAttach ? SelectIcon : mIcon;
                //记录值
                lastX = (sideRelative >= rootMiddle ? (sideRelative - lineMarginSide) : (sideRelative + lineMarginSide)) - (iconDraw
                        .getWidth() >> 1);
                //画一个图
                canvas.drawBitmap(iconDraw, lastX, lastY, null);


            } else {
                if (isLastCircle) {
                    //画一个圆
                    lastX = sideRelative >= rootMiddle ? (sideRelative - lineMarginSide) : (sideRelative + lineMarginSide);
                    canvas.drawCircle(lastX, lastY, pointSize, pointPaint);
                } else {
                    //画一个图
                    canvas.drawBitmap(mIcon, lastX, lastY, null);
                }
                //画剩下的
                canvas.drawLine(lastX, lastY + pointSize, lastX, boom, linePaint);
            }

        }
    }

    private void drawBetweenLineVertical(Canvas canvas)
    {


        if (isAntherStyle) {
            int oldy = firstY + mIcon.getHeight();

            Bitmap draw = mIcon;
            for (int i = 0; i < getChildCount() - 1; i++) {
                //画一个图
                int top = getChildAt(i).getTop();
                int Y = top + getChildAt(i).getPaddingTop() + lineDynamicDimen;
                int x = firstX + mIcon.getWidth() / 2;
                int iconY = Y + draw.getHeight();
                int Boom = getChildAt(i).getBottom() + getChildAt(i).getPaddingBottom() + lineDynamicDimen + draw.getHeight() / 2;
                int height = (Boom - iconY) / 2;
                if (i == status) {
                    draw = SelectIcon;
                    linePaint.setColor(anThorColor);

                    canvas.drawLine(x, iconY, x, iconY + height, linePaint);
                    linePaint.setColor(lineColor);
                    canvas.drawLine(x, iconY + height * 2, x, iconY + height, linePaint);

                } else if (i < status) {
                    draw = SelectIcon;
                    linePaint.setColor(anThorColor);
                    canvas.drawLine(x, iconY, x, iconY + height * 2, linePaint);


                } else {
                    draw = mIcon;
                    linePaint.setColor(lineColor);
                    canvas.drawLine(x, iconY, x, iconY + height * 2, linePaint);


                }
                oldy = Y;
                if (i != 0) {
                    //画一个图
                    canvas.drawBitmap(draw, firstX, Y, null);
                }


            }


        } else {
            //画剩下的
            canvas.drawLine(firstX, firstY + pointSize, firstX, lastY - pointSize, linePaint);

            for (int i = 0; i < getChildCount() - 1; i++) {
                //画了线，就画圆
                if (getChildAt(i) != null && i != 0) {
                    int top = getChildAt(i).getTop();
                    //记录值
                    int Y = top + getChildAt(i).getPaddingTop() + lineDynamicDimen;
                    canvas.drawCircle(firstX, Y, pointSize, pointPaint);
                }
            }
        }


    }

    //=============================================================Horizontal Draw
    private void drawFirstChildViewHorizontal(Canvas canvas)
    {
        if (getChildAt(0) != null) {
            int left = getChildAt(0).getLeft();
            //记录值
            firstX = left + getChildAt(0).getPaddingLeft() + lineDynamicDimen;
            firstY = sideRelative >= rootMiddle ? (sideRelative - lineMarginSide) : (sideRelative + lineMarginSide);
            //画一个圆
            canvas.drawCircle(firstX, firstY, pointSize, pointPaint);
        }
    }

    private void drawLastChildViewHorizontal(Canvas canvas)
    {
        if (getChildAt(getChildCount() - 1) != null) {
            int left = getChildAt(getChildCount() - 1).getLeft();
            //记录值
            lastX = left + getChildAt(getChildCount() - 1).getPaddingLeft() + lineDynamicDimen;
            lastY = (sideRelative >= rootMiddle ? (sideRelative - lineMarginSide) : (sideRelative + lineMarginSide)) - (mIcon
                    .getWidth() >> 1);
            if (isLastCircle) {
                //画一个圆
                canvas.drawCircle(lastX, lastY, pointSize, pointPaint);
            } else {
                //画一个图
                canvas.drawBitmap(mIcon, lastX, lastY, null);
            }
        }
    }

    private void drawBetweenLineHorizontal(Canvas canvas)
    {
        //画剩下的线
        canvas.drawLine(firstX, firstY, lastX, firstY, linePaint);
        for (int i = 0; i < getChildCount() - 1; i++) {
            //画了线，就画圆
            if (getChildAt(i) != null && i != 0) {
                int left = getChildAt(i).getLeft();
                //记录值
                int x = left + getChildAt(i).getPaddingLeft() + lineDynamicDimen;
                canvas.drawCircle(x, firstY, pointSize, pointPaint);
            }
        }
    }

    //=============================================================Getter/Setter

    @Override
    public void setOrientation(int orientation)
    {
        super.setOrientation(orientation);
        this.curOrientation = orientation;
        invalidate();
    }

    public int getLineStrokeWidth()
    {
        return lineStrokeWidth;
    }

    public void setLineStrokeWidth(int lineStrokeWidth)
    {
        this.lineStrokeWidth = lineStrokeWidth;
        invalidate();
    }

    public boolean isDrawLine()
    {
        return drawLine;
    }

    public void setDrawLine(boolean drawLine)
    {
        this.drawLine = drawLine;
        invalidate();
    }

    public Paint getLinePaint()
    {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint)
    {
        this.linePaint = linePaint;
        invalidate();
    }

    public int getPointSize()
    {
        return pointSize;
    }

    public void setPointSize(int pointSize)
    {
        this.pointSize = pointSize;
        invalidate();
    }

    public int getPointColor()
    {
        return pointColor;
    }

    public void setPointColor(int pointColor)
    {
        this.pointColor = pointColor;
        invalidate();
    }

    public Paint getPointPaint()
    {
        return pointPaint;
    }

    public void setPointPaint(Paint pointPaint)
    {
        this.pointPaint = pointPaint;
        invalidate();
    }

    public int getLineColor()
    {
        return lineColor;
    }

    public void setLineColor(int lineColor)
    {
        this.lineColor = lineColor;
        invalidate();
    }

    public int getLineMarginSide()
    {
        return lineMarginSide;
    }

    public void setLineMarginSide(int lineMarginSide)
    {
        this.lineMarginSide = lineMarginSide;
        invalidate();
    }

    public int getLineDynamicDimen()
    {
        return lineDynamicDimen;
    }

    public void setLineDynamicDimen(int lineDynamicDimen)
    {
        this.lineDynamicDimen = lineDynamicDimen;
        invalidate();
    }

    public Bitmap getIcon()
    {
        return mIcon;
    }

    public void setIcon(Bitmap icon)
    {
        mIcon = icon;
    }

    public void setIcon(int resId)
    {
        if (resId == 0) return;
        BitmapDrawable temp = (BitmapDrawable) mContext.getResources().getDrawable(resId);
        if (temp != null) mIcon = temp.getBitmap();
        invalidate();
    }

    public int getLineGravity()
    {
        return lineGravity;
    }

    public void setLineGravity(int lineGravity)
    {
        this.lineGravity = lineGravity;
        invalidate();
    }

    public void setLastCircle(boolean isLastCircle)
    {
        this.isLastCircle = isLastCircle;
    }

    /**
     * 设置另一种风格Style
     *
     * @param resid
     * @param isAntherStyle
     * @param status
     */
    public void setAnthorStyle(boolean isAntherStyle, int resid, int anresid, int status, int color)
    {

        this.isAntherStyle = isAntherStyle;
        BitmapDrawable amp = (BitmapDrawable) ResourcesCompat.getDrawable(getContext(), resid);
        if (amp != null)
            this.mIcon = amp.getBitmap();
        BitmapDrawable temp = (BitmapDrawable) ResourcesCompat.getDrawable(getContext(), anresid);
        if (temp != null)
            this.SelectIcon = temp.getBitmap();
        this.status = status;
        this.anThorColor = color;

    }


}
