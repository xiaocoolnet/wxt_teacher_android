package cn.xiaocool.wxtteacher.main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtteacher.BaseActivity;
import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.adapter.ChooseTreeAdapter;
import cn.xiaocool.wxtteacher.bean.FileBean;
import cn.xiaocool.wxtteacher.bean.ParentAdressBean;
import cn.xiaocool.wxtteacher.bean.UserInfo;
import cn.xiaocool.wxtteacher.bean.bean.Node;
import cn.xiaocool.wxtteacher.bean.bean.TreeListViewAdapter;
import cn.xiaocool.wxtteacher.dao.CommunalInterfaces;
import cn.xiaocool.wxtteacher.net.NewsRequest;
import cn.xiaocool.wxtteacher.ui.ProgressViewUtil;
import cn.xiaocool.wxtteacher.utils.JsonParser;
import cn.xiaocool.wxtteacher.utils.ToastUtils;
import cn.xiaocool.wxtteacher.utils.VolleyUtil;

public class CreatePGroupActivity extends BaseActivity {

    private Context context;
    private ListView address_class;
    private List<ParentAdressBean> parentAdressBeanList;
    private TreeListViewAdapter mAdapter;
    private List<FileBean> fileBeanList;
    private ArrayList<String> chooseIDs;
    private TextView down_selected_num;
    private EditText edt_chat_name;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.MESSAGEADDRESS:
                    if (msg.obj != null) {
                        ProgressViewUtil.dismiss();
                        JSONObject obj = (JSONObject) msg.obj;
                        try {
                            String state = obj.getString("status");
                            if (state.equals(CommunalInterfaces._STATE)) {
                                //修改三级列表后重新解析
                                parentAdressBeanList.clear();
                                parentAdressBeanList.addAll(getBeanFromJson(obj));
                                for (int i =0;i<parentAdressBeanList.size();i++){
                                    parentAdressBeanList.get(i).setNparentId(0);
                                    parentAdressBeanList.get(i).setNid(Integer.parseInt(parentAdressBeanList.get(i).getClassid()));
                                    FileBean classBean = new FileBean(parentAdressBeanList.get(i).getNid(),parentAdressBeanList.get(i).getNparentId(),parentAdressBeanList.get(i).getClassname(),"0","0");
                                    List<String> classids = new ArrayList<>();
                                    classBean.setChildIDs(classids);
                                    fileBeanList.add(classBean);
                                    for (int j=0;j<parentAdressBeanList.get(i).getStudent_list().size();j++){
                                        parentAdressBeanList.get(i).getStudent_list().get(j).setNparentId(Integer.parseInt(parentAdressBeanList.get(i).getClassid()));
                                        parentAdressBeanList.get(i).getStudent_list().get(j).setNid(Integer.parseInt(parentAdressBeanList.get(i).getStudent_list().get(j).getId()));
                                        FileBean studentBean = new FileBean(parentAdressBeanList.get(i).getStudent_list().get(j).getNid(),parentAdressBeanList.get(i).getStudent_list().get(j).getNparentId(),parentAdressBeanList.get(i).getStudent_list().get(j).getName(),parentAdressBeanList.get(i).getStudent_list().get(j).getPhoto(),"0");
                                        classids.add(String.valueOf(studentBean.get_id()));
                                        List<String> studentids = new ArrayList<>();
                                        studentBean.setChildIDs(studentids);
                                        fileBeanList.add(studentBean);
                                        for (int k=0;k<parentAdressBeanList.get(i).getStudent_list().get(j).getParent_list().size();k++){
                                            ParentAdressBean.StudentListBean studentListBean =  parentAdressBeanList.get(i).getStudent_list().get(j);
                                            studentListBean.getParent_list().get(k).setNparentId(Integer.parseInt(studentListBean.getId()));
                                            studentListBean.getParent_list().get(k).setNid(Integer.parseInt(studentListBean.getParent_list().get(k).getId()));
                                            FileBean parentBean = new FileBean(studentListBean.getParent_list().get(k).getNid(), studentListBean.getParent_list().get(k).getNparentId(), studentListBean.getParent_list().get(k).getName()+"("+ studentListBean.getParent_list().get(k).getAppellation()+")",studentListBean.getParent_list().get(k).getPhoto(),studentListBean.getParent_list().get(k).getPhone());
                                            studentids.add(String.valueOf(parentBean.get_id()));
                                            List<String> parentids = new ArrayList<>();
                                            parentBean.setChildIDs(parentids);
                                            fileBeanList.add(parentBean);
                                        }
                                    }
                                }
                                try
                                {
                                    mAdapter = new ChooseTreeAdapter(address_class, context, fileBeanList, fileBeanList, new ChooseTreeAdapter.isChecked() {
                                        @Override
                                        public void isChecked() {
                                            chooseIDs.clear();
                                            for (int i = 0; i < fileBeanList.size(); i++) {
                                                if (fileBeanList.get(i).getChecked()){
                                                    chooseIDs.add(String.valueOf(fileBeanList.get(i).get_id()));
                                                }
                                            }
                                            Log.e("count",""+chooseIDs.size());
                                            down_selected_num.setText("已选择"+chooseIDs.size()+"人");
                                        }
                                    }, 10);
                                    mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
                                    {
                                        @Override
                                        public void onClick(Node node, int position)
                                        {
                                            if (node.isLeaf())
                                            {

                                            }
                                        }

                                    });

                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                    Log.e("AddressParentFragment",e.toString());
                                }
                                address_class.setAdapter(mAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void inviderPerson(String chatid) {
        String inviderid =null;
        String str = null;
        for (int i = 0; i < chooseIDs.size(); i++) {
            inviderid = inviderid + "," + chooseIDs.get(i);
            str = str + "," + "0";
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


    private List<ParentAdressBean> getBeanFromJson(JSONObject result) {
        String data = "";
        try {
//            JSONObject json = new JSONObject(result);
            data = result.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ParentAdressBean>>() {
        }.getType());
    }







    private void news() {
        new NewsRequest(context, handler).addressParent();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pgroup);
        getSupportActionBar().hide();
        init();
        news();
    }

    private void init() {
        ProgressViewUtil.show(this);
        context = this;
        parentAdressBeanList = new ArrayList<>();
        fileBeanList = new ArrayList<>();
        chooseIDs = new ArrayList<>();
        address_class = (ListView) findViewById(R.id.address_class);
        edt_chat_name = (EditText) findViewById(R.id.edt_chat_name);
        down_selected_num = (TextView) findViewById(R.id.down_selected_num);
        findViewById(R.id.up_jiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commepleted();
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
//        if (chooseIDs.size()<1){
//            ToastUtils.ToastShort(context,"请选择家长！");
//            return;
//        }
//        Intent intent = new Intent();
//        intent.putExtra("sss", "ssssss");
//        intent.putStringArrayListExtra("ids", chooseIDs);
//        intent.putStringArrayListExtra("names", chooseIDs);
//        setResult(RESULT_OK, intent);
//        finish();
    }
}
