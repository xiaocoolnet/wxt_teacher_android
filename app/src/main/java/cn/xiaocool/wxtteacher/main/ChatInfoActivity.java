package cn.xiaocool.wxtteacher.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.adapter.ChatInfoGridAdapter;
import cn.xiaocool.wxtteacher.bean.ChatInfoBean;
import cn.xiaocool.wxtteacher.bean.UserInfo;
import cn.xiaocool.wxtteacher.ui.NoScrollGridView;
import cn.xiaocool.wxtteacher.utils.JsonParser;
import cn.xiaocool.wxtteacher.utils.ToastUtils;
import cn.xiaocool.wxtteacher.utils.VolleyUtil;

public class ChatInfoActivity extends AppCompatActivity {

    private Context context;
    private List<ChatInfoBean> pList,tList;
    private List<String> nowPlist;
    private ChatInfoGridAdapter pAdapter,tAdapter;
    private NoScrollGridView pGridView,tGridView;
    private String inviderid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_information);
        getSupportActionBar().hide();
        context = this;
        pList = new ArrayList<>();
        tList = new ArrayList<>();
        initView();
        initData();
    }

    private void initData() {
        String chatid = getIntent().getStringExtra("chatid");
        String url = "http://wxt.xiaocool.net/index.php?g=apps&m=message&a=xcGetChatLinkManList&userid="+new UserInfo(this).getUserId()+"&chatid="+chatid;
        Log.e("initData: ", url);
        VolleyUtil.VolleyGetRequest(this, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonParser.JSONparser(context,result)) {
                    List<ChatInfoBean> list = getBeanFromJsonReceive(result);
                    divideList(list);
                    setAdapter();
                }else {
                    ToastUtils.ToastShort(context,"暂无群联系人！");
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void divideList(List<ChatInfoBean> list) {
        nowPlist = new ArrayList<>();
        pList.clear();
        tList.clear();
        for (int i = 0; i < list.size(); i++) {
            ChatInfoBean chatInfoBean = list.get(i);
            nowPlist.add(chatInfoBean.getUserid());
            if (chatInfoBean.getUser_type().equals("0")){
                pList.add(chatInfoBean);
            }else {
                tList.add(chatInfoBean);
            }
        }
    }

    private void setAdapter() {
        if (pAdapter==null){
            pAdapter = new ChatInfoGridAdapter(pList,context);
        }else {
            pAdapter.notifyDataSetChanged();
        }
        if (tAdapter==null){
            tAdapter = new ChatInfoGridAdapter(tList,context);
        }else {
            tAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        findViewById(R.id.up_jiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pGridView = (NoScrollGridView) findViewById(R.id.gv_pList);
        tGridView = (NoScrollGridView) findViewById(R.id.gv_tList);
        pAdapter = new ChatInfoGridAdapter(pList,context);
        tAdapter = new ChatInfoGridAdapter(tList,context);
        pGridView.setAdapter(pAdapter);
        tGridView.setAdapter(tAdapter);
        pGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==pList.size()){
                    Intent intent = new Intent(context, ChatpChooseActivity.class);
                    intent.putExtra("type", "student");
                    startActivityForResult(intent, 101);
                }
            }
        });
        tGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==tList.size()){
                    Intent intent = new Intent(context, MyClassListActivity.class);
                    intent.putExtra("type", "teacher");
                    startActivityForResult(intent, 102);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 101:
                    if (data != null) {
                        ArrayList<String> ids = data.getStringArrayListExtra("ids");
                        ArrayList<String> names = data.getStringArrayListExtra("names");
                        String haschoose = "";
                        for (int i = 0; i < names.size(); i++) {
                            if (i < 3) {
                                if (names.get(i) != null || names.get(i) != "null") {
                                    haschoose = haschoose + names.get(i) + "、";
                                }
                            } else if (i == 4) {
                                haschoose = haschoose.substring(0, haschoose.length() - 1);
                                haschoose = haschoose + "等...";
                            }

                        }

                        for (int i = 0; i < ids.size(); i++) {
                            for (int j = 0; j < nowPlist.size(); j++) {
                                if (nowPlist.get(j).equals(ids.get(i))){
                                    ids.remove(ids.get(i));
                                }
                            }
                        }
                        inviderid =null;
                        for (int i = 0; i < ids.size(); i++) {
                            inviderid = inviderid + "," + ids.get(i);
                        }
                        inviderid = inviderid.substring(5, inviderid.length());
                        invidePersons("0");
                    }

                    break;
                case 102:
                    if (data != null) {
                        ArrayList<String> ids = data.getStringArrayListExtra("ids");
                        ArrayList<String> names = data.getStringArrayListExtra("names");
                        String haschoose = "";
                        for (int i = 0; i < names.size(); i++) {
                            if (i < 3) {
                                if (names.get(i) != null || names.get(i) != "null") {
                                    haschoose = haschoose + names.get(i) + "、";
                                }
                            } else if (i == 4) {
                                haschoose = haschoose.substring(0, haschoose.length() - 1);
                                haschoose = haschoose + "等...";
                            }

                        }
                        for (int i = 0; i < ids.size(); i++) {
                            for (int j = 0; j < nowPlist.size(); j++) {
                                if (nowPlist.get(j).equals(ids.get(i))){
                                    ids.remove(ids.get(i));
                                }
                            }
                        }
                        inviderid =null;
                        for (int i = 0; i < ids.size(); i++) {
                            inviderid = inviderid + "," + ids.get(i);
                        }
                        inviderid = inviderid.substring(5, inviderid.length());
                        invidePersons("1");
                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void invidePersons(String type) {
        String chatid = getIntent().getStringExtra("chatid");
        String url = "http://wxt.xiaocool.net/index.php?g=apps&m=message&a=xcAddChatLinkMan&invitertype=1&inviterid="+new UserInfo(this).getUserId()+"&userid="+inviderid
                +"&chatid="+chatid+"&usertype="+type;
        Log.e("initData: ", url);
        VolleyUtil.VolleyGetRequest(this, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonParser.JSONparser(context,result)) {
                   ToastUtils.ToastShort(context,"邀请成功！");
                    initData();
                }else {
                    ToastUtils.ToastShort(context,"邀请失败！");
                }
            }

            @Override
            public void onError() {

            }
        });
    }


    /**
     * 字符串转模型（接收）
     * @param result
     * @return
     */
    private List<ChatInfoBean> getBeanFromJsonReceive(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<ChatInfoBean>>() {
        }.getType());
    }
}