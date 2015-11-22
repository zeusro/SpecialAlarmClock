package com.gdtel.eshore.androidframework.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdtel.eshore.androidframework.common.entity.MoreItemEntity;
import com.gdtel.eshore.anroidframework.R;

public class MoreItemAdapter extends BaseAdapter {
	private Context context;
	private List<MoreItemEntity> items;
	
	public MoreItemAdapter(Context context, List<MoreItemEntity> items) {
		super();
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {

		return items.size();
	}

	@Override
	public Object getItem(int location) {

		return items.get(location);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@SuppressWarnings("deprecation")
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
			holder.item_name = (TextView)convertView.findViewById(R.id.item_name);
			holder.item_icon = (ImageView)convertView.findViewById(R.id.item_icon);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		MoreItemEntity item = items.get(position);
		
		if (!TextUtils.isEmpty(item.getItemName())) {
			holder.item_name.setText(item.getItemName());
		}
		if (item.getDrawable()!=null) {
			holder.item_icon.setBackgroundDrawable(item.getDrawable());
		}
		return convertView;
	}
	class ViewHolder {
		ImageView item_icon;
		TextView item_name;
	}
}
