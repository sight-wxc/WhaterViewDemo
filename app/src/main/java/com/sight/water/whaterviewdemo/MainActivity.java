package com.sight.water.whaterviewdemo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sight.water.whaterviewdemo.util.ResourcesCompat;
import com.sight.water.whaterviewdemo.view.WaterLinearLayout;
import com.sight.water.whaterviewdemo.view.WaterListView;
import com.sight.water.whaterviewdemo.view.WaterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{


    Button btnAdd;
    Button btnDelete;
    /**
     * 自定义View
     * 在项目经常看到这样的item
     */
    WaterView waterView;
    /**
     * 自定义View
     * 时间轴 ~~改写了下
     */
    WaterLinearLayout waterLayout;

    DemoAdpter demoAdpter;

    List<BaseMsg> message = new ArrayList<>();

    WaterListView waterListView;

    private int firstY;
    private int currentY;

    private boolean isDown;
    private boolean isShow;

    private ObjectAnimator mAnimator;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDelete = (Button) findViewById(R.id.btn_detele);
        waterView = (WaterView) findViewById(R.id.vi_water);
        waterLayout = (WaterLinearLayout) findViewById(R.id.vi_layout);
        waterListView = (WaterListView) findViewById(R.id.vi_lv);




        btnDelete.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        //这里设置是另一种风格Style  目前只弄了Ver ~~
        waterLayout.setAnthorStyle(true, R.mipmap.icon_blue_unselected_grey, R.mipmap.icon_blue_seleted, 1, ResourcesCompat.getColor(this, R.color.colorPrimary));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                startActivity(new Intent(getApplicationContext(), HeartActivity.class));
                Toast.makeText(MainActivity.this, "我还没开发完呢，提啥需求", Toast.LENGTH_LONG).show();
            }
        });


        for (int i = 0; i < 30; i++) {
            message.add(new BaseMsg("时间", "地点", "391335693"));
        }
        demoAdpter = new DemoAdpter(message);
        waterListView.setAdapter(demoAdpter);


        waterListView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        firstY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentY = (int) event.getY();
                        if (firstY - currentY > 0) {
                            isDown = true;
                        } else {
                            isDown = false;
                        }
                        if (isDown) {
                            if (isShow) {
                                showHideAnimation(false);
                                isShow = !isShow;
                            }
                        } else {
                            if (!isShow) {
                                showHideAnimation(true);
                                isShow = true;
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }


                return false;
            }
        });
    }


    //显示隐藏ListView的方法
    private void showHideAnimation(boolean flag)
    {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (flag) {
            mAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), 0);
        } else {
            mAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), -toolbar.getHeight());
        }
        mAnimator.start();
        ;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addSubItem(String time, String co)
    {
        View refeundView = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.list_layout_item, waterLayout, false);
        TextView tvTime = (TextView) refeundView.findViewById(R.id.tv_first);
        TextView tvContext = (TextView) refeundView.findViewById(R.id.tv_second);
        tvTime.setText(time);
        tvTime.setTextColor(ResourcesCompat.getColor(this.getApplicationContext(), R.color.colorPrimary));
        tvContext.setText(co);
        waterLayout.addView(refeundView);

    }


    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btn_add) {
            //这里设置左边的ICON
            waterView.setLeftIcon(R.mipmap.ic_icon_server);
            //这里设置右边的左方位的图片
            waterView.setRightIcon("这是右边的标题", R.mipmap.ic_icon_server, Color.parseColor("#FF4081"), new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(MainActivity.this, "我还没开发完呢，提啥需求", Toast.LENGTH_LONG).show();
                }
            }, 0);

            addSubItem("2016.6.22", "你很帅！！QQ:391335693 ");


        } else {
            //这里设置 左边的为空
            waterView.setLeftIcon(0);
            waterView.setRightIconNull();
            int child = waterLayout.getChildCount();
            if (child > 0) {
                waterLayout.removeViewAt(child - 1);
            }


        }

    }
}
