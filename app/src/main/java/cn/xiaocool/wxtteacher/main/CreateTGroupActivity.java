package cn.xiaocool.wxtteacher.main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtteacher.BaseActivity;
import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.adapter.ChooseStudentExpandbleListViewAdapter;
import cn.xiaocool.wxtteacher.adapter.EListAdapter;
import cn.xiaocool.wxtteacher.bean.Child;
import cn.xiaocool.wxtteacher.bean.ClassList;
import cn.xiaocool.wxtteacher.bean.Group;
import cn.xiaocool.wxtteacher.bean.UserInfo;
import cn.xiaocool.wxtteacher.dao.CommunalInterfaces;
import cn.xiaocool.wxtteacher.net.NewsRequest;
import cn.xiaocool.wxtteacher.utils.JsonParser;
import cn.xiaocool.wxtteacher.utils.ToastUtils;
import cn.xiaocool.wxtteacher.utils.VolleyUtil;

public class CreateTGroupActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<Group> groups;
    private ExpandableListView listView;
    private EListAdapter adapter;
    private EditText edt_chat_name;
    private ImageView btn_exit;
    private ExpandableListView class_list;
    private String classID, className, type;
    private ChooseStudentExpandbleListViewAdapter classListAdapter;
    private List<ClassList.ClassListData> classListDataList;
    private List<List<ClassList.ClassStudentData>> studentDataList;
    private List<String> classesID;
    private CheckBox quan_check;
    private int size;
    private String[] allMembers;
    private TextView down_selected_num;
    private RelativeLayout up_jiantou, btn_add;
    private ArrayList<Child> selectedUsers;
    private ArrayList<String> selectedIds, selectedNames;
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.TEACHER:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            groups = new ArrayList<Group>();
                            JSONArray dataArray = obj.optJSONArray("data");
                            JSONObject itemObject;
                            Group group = new Group("teacher", "老师");
                            for (int i = 0; i < dataArray.length(); i++) {
                                itemObject = dataArray.optJSONObject(i);
                                Child child = new Child(itemObject.optString("id"), itemObject.optString("name"),
                                        itemObject.optString("name"));
                                group.addChildrenItem(child);

                            }
                            groups.add(group);
                            adapter = new EListAdapter(CreateTGroupActivity.this, groups, quan_check, down_selected_num);
                            listView.setAdapter(adapter);
                            listView.setOnChildClickListener(adapter);
                        }
                    }


                    break;

                case 111:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(context,msg.obj.toString())){
                            JSONObject obj = (JSONObject) msg.obj;
                            try {
                                String chatid = obj.getJSONObject("data").getString("chatid");
                                inviderPerson(chatid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            ToastUtils.ToastShort(context,"创建失败");
                        }

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tgroup);
        context = this;
        initView();
    }


    private void initView() {
        classesID = new ArrayList<>();
        studentDataList = new ArrayList<>();
        classListDataList = new ArrayList<>();
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        btn_add = (RelativeLayout) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        edt_chat_name = (EditText) findViewById(R.id.edt_chat_name);
        quan_check = (CheckBox) findViewById(R.id.quan_check);
        quan_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll();
            }
        });
        listView = (ExpandableListView) findViewById(R.id.listView);
        listView.setGroupIndicator(null);
        down_selected_num = (TextView) findViewById(R.id.down_selected_num);
        type = getIntent().getStringExtra("type");



        new NewsRequest(CreateTGroupActivity.this, handler).getTeacher();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.btn_add:
                getAllMenbers();
                commepleted();
                break;
        }
    }

    private void inviderPerson(String chatid) {
        String inviderid =null;
        String str = null;
        for (int i = 0; i < selectedIds.size(); i++) {
            inviderid = inviderid + "," + selectedIds.get(i);
            str = str + "," + "1";
        }

        inviderid = inviderid.substring(5, inviderid.length());
        str = str.substring(5, str.length());
        String url = "http://wxt.xiaocool.net/index.php?g=apps&m=message&a=xcAddChatLinkMan&invitertype=1&inviterid="+new UserInfo(this).getUserId()+"&userid="+inviderid
                +"&chatid="+chatid+"&usertype="+str;
        Log.e("initData: ", url);
        VolleyUtil.VolleyGetRequest(this, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonParser.JSONparser(context,result)) {
                    ToastUtils.ToastShort(context,"创建成功！");
                    finish();
                }else {
                    ToastUtils.ToastShort(context,"创建失败！");
                }
            }

            @Override
            public void onError() {

            }
        });
    }
    private void commepleted() {

        if (edt_chat_name.getText()==null||edt_chat_name.getText().equals("")){
            ToastUtils.ToastShort(context,"请输入群名称!");
            return;
        }
        String url = "http://wxt.xiaocool.net/index.php?g=apps&m=message&a=CreateGroupChat";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("send_uid",new UserInfo(context).getUserId()));
        params.add(new BasicNameValuePair("title",edt_chat_name.getText().toString()));
        params.add(new BasicNameValuePair("send_type","1"));
        new NewsRequest(context,handler).post(url,params,111);
    }
    private void checkAll() {
        if (quan_check.isChecked()) {
            size = 0;
            for (int i = 0; i < groups.size(); i++) {
                groups.get(i).setChecked(true);
                size += groups.get(i).getChildrenCount();
                for (int j = 0; j < groups.get(i).getChildrenCount(); j++) {
                    groups.get(i).getChildItem(j).setChecked(true);
                }
            }
            adapter.notifyDataSetChanged();
            listView.setOnChildClickListener(adapter);
            for (int i = 0; i < groups.size(); i++) {
                listView.expandGroup(i);
            }
            down_selected_num.setText("已选择" + size + "人");

        } else {
            for (int i = 0; i < groups.size(); i++) {
                groups.get(i).setChecked(false);
                for (int j = 0; j < groups.get(i).getChildrenCount(); j++) {
                    groups.get(i).getChildItem(j).setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
            listView.setOnChildClickListener(adapter);
            for (int i = 0; i < groups.size(); i++) {
                listView.expandGroup(i);
            }
            down_selected_num.setText("已选择0人");
        }
    }

    /**
     * 获取群组初始人员
     */
    private void getAllMenbers() {
        ArrayList<String> newG = new ArrayList<>();
        selectedUsers = new ArrayList<>();
        selectedIds = new ArrayList<>();
        selectedNames = new ArrayList<>();
        for (int i = 0; i < adapter.getterGroups().size(); i++) {
            for (int j = 0; j < adapter.getterGroups().get(i).getChildrenCount(); j++) {
                if (adapter.getterGroups().get(i).getChildItem(j).getChecked()) {

                    Child child = new Child(adapter.getterGroups().get(i).getChildItem(j).getUserid(), adapter.getterGroups().get(i).getChildItem(j).getFullname(), adapter.getterGroups().get(i).getChildItem(j).getUsername());
                    selectedUsers.add(child);
                    selectedIds.add(adapter.getterGroups().get(i).getChildItem(j).getUserid());
                    selectedNames.add(adapter.getterGroups().get(i).getChildItem(j).getFullname());
                } else {
                    Log.e("checked", String.valueOf(adapter.getterGroups().get(i).getChildItem(j).getChecked()));

                }

            }
        }

    }
}
