package cn.xiaocool.wxtteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.bean.FileBean;
import cn.xiaocool.wxtteacher.bean.bean.Node;
import cn.xiaocool.wxtteacher.bean.bean.TreeListViewAdapter;
import cn.xiaocool.wxtteacher.main.TeacherCommunicationActivity;
import cn.xiaocool.wxtteacher.ui.RoundImageView;

import static cn.xiaocool.wxtteacher.R.id.footer;
import static cn.xiaocool.wxtteacher.R.id.p_box;

public class ChooseTreeAdapter<T> extends TreeListViewAdapter<T>
{
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions displayImageOptions;
	private List<FileBean> fileBeanList;
	private isChecked check;
	public ChooseTreeAdapter(ListView mTree, Context context, List<T> datas, List<FileBean> fileBeanList,isChecked checked,
							 int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
		this.fileBeanList = fileBeanList;
		this.check = checked;
		displayImageOptions = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
				.cacheInMemory(true).cacheOnDisc(true).build();
	}

	@Override
	public View getConvertView(final Node node , int position, View convertView, ViewGroup parent)
	{
	 	FileBean model = null;
		for (int i = 0;i<fileBeanList.size();i++){
			FileBean fileBean = fileBeanList.get(i);
			if (node.getId() == fileBean.get_id()){
				model = fileBeanList.get(i);
				break;
			}
		}
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.chatp_choose_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView
				.findViewById(R.id.id_treenode_icon);
			viewHolder.avatar = (RoundImageView) convertView
					.findViewById(R.id.id_treenode_avatar);
			viewHolder.label = (TextView) convertView
					.findViewById(R.id.id_treenode_label);
			viewHolder.tree_divider = (LinearLayout) convertView
					.findViewById(R.id.tree_divider);
			viewHolder.tree_divider1 = (LinearLayout) convertView
					.findViewById(R.id.tree_divider1);
			viewHolder.p_box = (CheckBox) convertView.findViewById(p_box);
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1)
		{
			viewHolder.icon.setVisibility(View.INVISIBLE);
		} else
		{
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
		}

		if (node.getpId()==0){
			viewHolder.avatar.setVisibility(View.GONE);
			viewHolder.tree_divider.setVisibility(View.GONE);
			viewHolder.tree_divider1.setVisibility(View.GONE);
		}else {
			viewHolder.avatar.setVisibility(View.VISIBLE);
		}
		if (node.getLevel()==2){
			viewHolder.tree_divider.setVisibility(View.VISIBLE);
			viewHolder.tree_divider1.setVisibility(View.VISIBLE);
			viewHolder.avatar.setVisibility(View.GONE);
		}else if(node.getLevel()==1){
			viewHolder.tree_divider.setVisibility(View.GONE);
			viewHolder.tree_divider1.setVisibility(View.GONE);
			viewHolder.avatar.setVisibility(View.VISIBLE);
		}
		if (node.getLevel()==1){
			viewHolder.tree_divider1.setVisibility(View.VISIBLE);
		}


		if (node.getLevel()==2){
			viewHolder.p_box.setVisibility(View.VISIBLE);
			viewHolder.p_box.setChecked(model.getChecked());
			final FileBean finalModel = model;
			final ViewHolder finalViewHolder = viewHolder;
			viewHolder.p_box.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					finalModel.setChecked(finalViewHolder.p_box.isChecked());
					check.isChecked();
				}
			});
		}else {

			viewHolder.p_box.setVisibility(View.GONE);

		}
		viewHolder.label.setText(node.getName());
		imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + model.getAvatar(), viewHolder.avatar, displayImageOptions);






		return convertView;
	}

	private void refreshPeopleWithUI(FileBean finalModel, Node node) {
		if (finalModel.getChecked()){
			finalModel.setChecked(false);
		}else {
			finalModel.setChecked(true);
		}
		if (node.getLevel()==0){
			if (finalModel.getChecked()){
				for (int i = 0; i < fileBeanList.size(); i++) {
					fileBeanList.get(i).setChecked(false);
				}
			}else {
				for (int i = 0; i < fileBeanList.size(); i++) {
					fileBeanList.get(i).setChecked(true);
				}
			}
		}else if (node.getLevel()==1){
			if (finalModel.getChecked()){
				for (int i = 0; i < finalModel.getChildIDs().size(); i++) {
					String id = finalModel.getChildIDs().get(i);
					for (int j = 0; j < fileBeanList.size(); j++) {
						if (id.equals(String.valueOf(fileBeanList.get(j).get_id()))){
							fileBeanList.get(j).setChecked(false);
						}
					}
				}
			}else {
				for (int i = 0; i < finalModel.getChildIDs().size(); i++) {
					String id = finalModel.getChildIDs().get(i);
					for (int j = 0; j < fileBeanList.size(); j++) {
						if (id.equals(String.valueOf(fileBeanList.get(j).get_id()))){
							fileBeanList.get(j).setChecked(true);
						}
					}
				}
			}
		}else {
			if (finalModel.getChecked()){
				finalModel.setChecked(false);
			}else {
				finalModel.setChecked(true);
			}
		}
		checkSelected(finalModel);

		this.notifyDataSetChanged();
	}

	private void checkSelected(FileBean finalModel) {
		FileBean parentModel = null;
		for (int i = 0; i < fileBeanList.size(); i++) {
			if (fileBeanList.get(i).get_id()==(finalModel.getParentId())){
				parentModel = fileBeanList.get(i);
				break;
			}
		}
		if (parentModel==null)return;
		Boolean flag = false;
		for (int i = 0; i < parentModel.getChildIDs().size(); i++) {
			for (int j = 0; j < fileBeanList.size(); j++) {
				if (String.valueOf(fileBeanList.get(j).get_id()).equals(parentModel.getChildIDs().get(i))){
					if (fileBeanList.get(j).getChecked()){
						flag = true;
					}
				}
			}
		}

		parentModel.setChecked(flag);
	}

	private final class ViewHolder {
		ImageView icon,avatar;
		TextView label;
		LinearLayout tree_divider,tree_divider1;
		CheckBox p_box;
	}

	public interface isChecked{
		void isChecked();
	}
}
