package cn.xiaocool.wxtteacher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.bean.TeacherInfo;
import cn.xiaocool.wxtteacher.net.request.constant.NetBaseConstant;

/**
 * Created by Administrator on 2016/6/19.
 */
public class WebRecruitListAdapter extends BaseAdapter{

    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;
    private ArrayList<TeacherInfo> list;
    private LayoutInflater inflater;

    public WebRecruitListAdapter(ArrayList<TeacherInfo> list, Context context) {
        this.mContext = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
//        displayImage = new DisplayImageOptions.Builder()
//                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
//                .cacheInMemory(true).cacheOnDisc(true).build();
        displayImage = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.katong)          // image在加载过程中，显示的图片
                .showImageForEmptyUri(R.drawable.katong)  // empty URI时显示的图片
                .showImageOnFail(R.drawable.katong)       // 不是图片文件 显示图片
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(false)           // default 不缓存至内存
                .cacheOnDisc(false)             // default 不缓存至手机SDCard
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// default
                .bitmapConfig(Bitmap.Config.ARGB_8888)              // default
                .displayer(new SimpleBitmapDisplayer()).build();
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
            convertView = inflater.inflate(R.layout.item_teacher_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        imageLoader.displayImage(NetBaseConstant.NET_BASE_HOST + list.get(position).getThumb(), holder.teacher_img, displayImage);

        holder.post_title.setText(list.get(position).getPost_title());
        holder.post_content.setText(list.get(position).getPost_date());
        return convertView;
    }

    public class ViewHolder {
        TextView post_title, post_content;
        ImageView teacher_img;

        public ViewHolder(View convertView) {
            post_title = (TextView) convertView.findViewById(R.id.post_title);
            post_content = (TextView) convertView.findViewById(R.id.post_content);
            teacher_img = (ImageView) convertView.findViewById(R.id.teacher_img);
        }
    }
}
