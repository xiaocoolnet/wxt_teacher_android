package cn.xiaocool.wxtteacher.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.bean.ChatInfoBean;
import cn.xiaocool.wxtteacher.net.request.constant.NetBaseConstant;


/**
 * Created by Administrator on 2016/5/27.
 */
public class ChatInfoGridAdapter extends BaseAdapter{

    private int i;
    private LayoutInflater mLayoutInflater;
    private List<ChatInfoBean> mWorkImgs;
    private Context mContext;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ChatInfoGridAdapter(List<ChatInfoBean> workImgs, Context context) {
        this.mContext = context;
        this.mWorkImgs = workImgs;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.i = mWorkImgs.size();
        options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square).cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    public int getCount() {
        return  mWorkImgs.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyGridViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyGridViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.chat_img_item, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_user_img);
            viewHolder.delete_img = (ImageView)convertView.findViewById(R.id.delete_img);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }
        if (position+1<=mWorkImgs.size()){
            imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + mWorkImgs.get(position).getMy_face(), viewHolder.imageView, options);
            viewHolder.tv_name.setText(mWorkImgs.get(position).getMy_nickname());
        }else {
            DisplayImageOptions  option = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.chat_add).showImageOnFail(R.drawable.chat_add).cacheInMemory(false).cacheOnDisc(false).build();
            imageLoader.displayImage("error",viewHolder.imageView,option);
            viewHolder.imageView.setImageResource(R.drawable.chat_add);
            viewHolder.tv_name.setText("");
        }


        return convertView;
    }

    private static class MyGridViewHolder {
        ImageView imageView;
        ImageView delete_img;
        TextView tv_name;
    }
}
