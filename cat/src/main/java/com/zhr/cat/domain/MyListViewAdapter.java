package com.zhr.cat.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.zhr.cat.R;
import com.zhr.cat.tools.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyListViewAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private List<TextInfo> textInfos;

    class ViewHolder {/**/
        CircleImageView catIcon, clientIcon;
        TextView catContent, clientContent, time;
        ViewGroup catContainer, clientContainer;
//        String lastTime=null;
    }

    public MyListViewAdapter(Context contex, List<TextInfo> textInfos) {
        this.context = contex;
        this.textInfos = textInfos;
    }

    public void setList(List<TextInfo> textInfos) {
        this.textInfos = textInfos;
    }

    @Override
    public int getCount() {
        return textInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return textInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_lv_item, null);
            holder.catIcon = convertView.findViewById(R.id.iv_cat_head);
            holder.clientIcon = convertView.findViewById(R.id.iv_client_head);
            holder.catContainer = convertView.findViewById(R.id.cat_container);
            holder.clientContainer = convertView.findViewById(R.id.client_container);
            holder.catContent = convertView.findViewById(R.id.cat_content);
            holder.clientContent = convertView.findViewById(R.id.client_content);
            holder.time = convertView.findViewById(R.id.chat_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA)
                .format(textInfos.get(position).getTime());
        int type = textInfos.get(position).getType();
        String content = textInfos.get(position).getContent();
        if (type == 1) {// 消息来自Client
            holder.catContainer.setVisibility(View.GONE);
            holder.clientContainer.setVisibility(View.VISIBLE);
            holder.clientContent.setText(content);
        } else {
            holder.catContainer.setVisibility(View.VISIBLE);
            holder.clientContainer.setVisibility(View.GONE);
            holder.catContent.setText(content);
        }
//        if (holder.lastTime==null){
//            holder.lastTime=time;
//            holder.time.setText(time);
//        }else{
//            int endIndex=time.length()-3;
//            System.out.println("******************");
//            System.out.println("time = " + time);
//            System.out.println("lastTime = " + holder.lastTime);
//            if (!time.substring(0,endIndex).equals(holder.lastTime.substring(0,endIndex))){
//
//            }else {
//                holder.time.setText("");
//            }
//        }
        holder.time.setText(time);
        return convertView;
    }
}
