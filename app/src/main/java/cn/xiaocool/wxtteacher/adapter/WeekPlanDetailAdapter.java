package cn.xiaocool.wxtteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtteacher.R;

/**
 * Created by Administrator on 2016/7/16.
 */
public class WeekPlanDetailAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> list;
    private String[] titles;
    public WeekPlanDetailAdapter(Context context, String[] titles,List<String> list){
        this.titles = titles;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_week_plan_detail,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.item_title.setText(titles[position]);
        holder.item_content.setText(list.get(position));

        return convertView;
    }

    public class ViewHolder {

        TextView item_title,item_content;

        public ViewHolder(View convertView) {
            item_title = (TextView) convertView.findViewById(R.id.item_title);
            item_content = (TextView) convertView.findViewById(R.id.item_content);
        }
    }
}
