package cn.xiaocool.wxtteacher.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.adapter.ChatListAdapter;
import cn.xiaocool.wxtteacher.bean.CommunicateListModel;
import cn.xiaocool.wxtteacher.bean.UserInfo;
import cn.xiaocool.wxtteacher.ui.ProgressViewUtil;
import cn.xiaocool.wxtteacher.utils.JsonParser;
import cn.xiaocool.wxtteacher.utils.VolleyUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdressGroupFragment extends Fragment {

    private Context context;
    private List<CommunicateListModel> communicateListModelList;
    private ChatListAdapter chatListAdapter;
    private ListView group_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adress_group, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        communicateListModelList = new ArrayList<>();
        initView();
    }

    private void initView() {
        group_list = (ListView) getView().findViewById(R.id.group_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        getChatList();
    }

    private void getChatList() {
        ProgressViewUtil.show(context);
        String url = "http://wxt.xiaocool.net/index.php?g=apps&m=message&a=xcGetChatListData&uid="+new UserInfo(getActivity()).getUserId();
        VolleyUtil.VolleyGetRequest(getActivity(), url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonParser.JSONparser(getActivity(), result)) {
                    ProgressViewUtil.dismiss();
                    communicateListModelList.clear();
                    for (int i = 0; i <JsonParser.getBeanFromJsonCommunicateListModel(result).size() ; i++) {
                        if (JsonParser.getBeanFromJsonCommunicateListModel(result).get(i).getType().equals("1")){
                            communicateListModelList.add(JsonParser.getBeanFromJsonCommunicateListModel(result).get(i));
                        }
                    }
                    setAdapter();
                }
            }

            @Override
            public void onError() {
                ProgressViewUtil.dismiss();
            }
        });
    }

    private void setAdapter() {
        if (chatListAdapter==null){
            chatListAdapter = new ChatListAdapter(getActivity(),communicateListModelList);
            group_list.setAdapter(chatListAdapter);
        }else {
            chatListAdapter.notifyDataSetChanged();
        }

    }
}
