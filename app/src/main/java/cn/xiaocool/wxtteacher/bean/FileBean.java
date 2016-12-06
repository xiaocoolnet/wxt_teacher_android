package cn.xiaocool.wxtteacher.bean;


import java.util.List;

import cn.xiaocool.wxtteacher.bean.bean.TreeNodeId;
import cn.xiaocool.wxtteacher.bean.bean.TreeNodeLabel;
import cn.xiaocool.wxtteacher.bean.bean.TreeNodePid;

public class FileBean
{
	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	private long length;
	private String avatar;
	private String telephone;
	private Boolean checked;
	private List<String> childIDs;
	public FileBean(int _id, int parentId, String name,String avatar,String telephone)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
		this.avatar = avatar;
		this.telephone = telephone;
		this.checked = false;
	}

	public FileBean(int _id, int parentId, String name,String avatar,String telephone,boolean isChecked)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
		this.avatar = avatar;
		this.telephone = telephone;
		this.checked = isChecked;
	}
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public List<String> getChildIDs() {
		return childIDs;
	}

	public void setChildIDs(List<String> childIDs) {
		this.childIDs = childIDs;
	}


	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}
