package cn.xiaocool.wxtteacher.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;

import cn.xiaocool.wxtteacher.BaseActivity;
import cn.xiaocool.wxtteacher.R;

import cn.xiaocool.wxtteacher.adapter.SimpleTreeAdapter;
import cn.xiaocool.wxtteacher.bean.FileBean;
import cn.xiaocool.wxtteacher.bean.ParentAdressBean;
import cn.xiaocool.wxtteacher.bean.bean.Node;
import cn.xiaocool.wxtteacher.bean.bean.TreeListViewAdapter;
import cn.xiaocool.wxtteacher.dao.CommunalInterfaces;
import cn.xiaocool.wxtteacher.net.NewsRequest;

import cn.xiaocool.wxtteacher.ui.ProgressViewUtil;


public class SpaceClickGrowActivity extends BaseActivity {

    private ListView address_class;
    private List<ParentAdressBean> parentAdressBeanList;
    private TreeListViewAdapter mAdapter;
    private List<FileBean> fileBeanList;
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
                                    fileBeanList.add(new FileBean(parentAdressBeanList.get(i).getNid(),parentAdressBeanList.get(i).getNparentId(),parentAdressBeanList.get(i).getClassname(),"0","0"));
                                    for (int j=0;j<parentAdressBeanList.get(i).getStudent_list().size();j++){
                                        parentAdressBeanList.get(i).getStudent_list().get(j).setNparentId(Integer.parseInt(parentAdressBeanList.get(i).getClassid()));
                                        parentAdressBeanList.get(i).getStudent_list().get(j).setNid(Integer.parseInt(parentAdressBeanList.get(i).getStudent_list().get(j).getId()));
                                        fileBeanList.add(new FileBean(parentAdressBeanList.get(i).getStudent_list().get(j).getNid(),parentAdressBeanList.get(i).getStudent_list().get(j).getNparentId(),parentAdressBeanList.get(i).getStudent_list().get(j).getName(),parentAdressBeanList.get(i).getStudent_list().get(j).getPhoto(),"0"));

                                    }
                                }
                                try
                                {
                                    mAdapter = new SimpleTreeAdapter<FileBean>(address_class, SpaceClickGrowActivity.this, fileBeanList,fileBeanList, 10);
                                    mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
                                    {
                                        @Override
                                        public void onClick(Node node, int position)
                                        {
                                            if (node.isLeaf())
                                            {
                                                Toast.makeText(SpaceClickGrowActivity.this, node.getName(),
                                                        Toast.LENGTH_SHORT).show();
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
//                            Log.e("============", classAddresses.get(0).get("group"));
                        }

                    }
                    break;
            }
        }
    };


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_click_grow);

        initView();
    }

    private void initView() {
       findViewById(R.id.up_jiantou).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
        address_class = (ListView) findViewById(R.id.address_class);
        parentAdressBeanList = new ArrayList<>();
        fileBeanList = new ArrayList<>();
    }

    private void news() {
        new NewsRequest(SpaceClickGrowActivity.this, handler).addressParent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        news();
    }
}
