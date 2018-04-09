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

	class ViewHodler {/**/
		CircleImageView catIcon, clientIcon;
		TextView catContent, clientContent, time;
		ViewGroup catContainer, clientContainer;
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
		ViewHodler hodler;
		if (convertView == null) {
			hodler = new ViewHodler();
			convertView = LayoutInflater.from(context).inflate(R.layout.chat_lv_item, null);
			hodler.catIcon=convertView.findViewById(R.id.iv_cat_head);
			hodler.clientIcon=convertView.findViewById(R.id.iv_client_head);
			hodler.catContainer = (ViewGroup) convertView.findViewById(R.id.cat_container);
			hodler.clientContainer = (ViewGroup) convertView.findViewById(R.id.client_container);
			hodler.catContent = (TextView) convertView.findViewById(R.id.cat_content);
			hodler.clientContent = (TextView) convertView.findViewById(R.id.chatto_content);
			hodler.time = (TextView) convertView.findViewById(R.id.chat_time);
			convertView.setTag(hodler);
		} else {
			hodler = (ViewHodler) convertView.getTag();
		}
		String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA)
				.format(textInfos.get(position).getTime());
		int type = textInfos.get(position).getType();
		String content = textInfos.get(position).getContent();
		if (type == 1) {// 消息来自Client
			hodler.catContainer.setVisibility(View.GONE);
			hodler.clientContainer.setVisibility(View.VISIBLE);
			hodler.clientContent.setText(content);
			hodler.time.setText(time);
		} else {
			hodler.catContainer.setVisibility(View.VISIBLE);
			hodler.clientContainer.setVisibility(View.GONE);
			hodler.catContent.setText(content);
			hodler.time.setText(time);
		}
		return convertView;
	}

}
