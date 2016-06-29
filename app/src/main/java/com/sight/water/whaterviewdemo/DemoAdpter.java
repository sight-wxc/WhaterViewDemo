package com.sight.water.whaterviewdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sight on 2016/4/20.
 */
public class DemoAdpter extends BaseAdapter

{
    List<BaseMsg> msgs;

    public DemoAdpter(List<BaseMsg> msgs)
    {
        this.msgs = msgs;
    }

    @Override
    public int getCount()
    {
        return msgs.size();
    }

    @Override
    public BaseMsg getItem(int position)
    {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        viewHolder vh = null;
        if (convertView == null) {
            vh =new viewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            vh.setPostion(convertView);
            convertView.setTag(vh);
        }else{
            vh = (viewHolder) convertView.getTag();
        }
        BaseMsg msg = msgs.get(position);

        if (msg!=null){
            vh.mTvEndTime.setText(msg.getTimeE());
            vh.mtvTime.setText(msg.getTimeS());
            vh.TvTitle.setText(msg.getTitle());

        }


        return convertView;
    }

    class viewHolder
    {
        private TextView mtvTime;
        private TextView mTvEndTime;
        private TextView TvTitle;


        public void setPostion(View itemView)
        {

            mtvTime = (TextView) itemView.findViewById(R.id.tv_start);
            mTvEndTime = (TextView) itemView.findViewById(R.id.tv_end);
            TvTitle = (TextView) itemView.findViewById(R.id.tv_title);


        }

    }

}
