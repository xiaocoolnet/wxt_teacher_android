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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtteacher.BaseActivity;
import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.bean.UserInfo;
import cn.xiaocool.wxtteacher.net.NewsRequest;
import cn.xiaocool.wxtteacher.utils.JsonParser;
import cn.xiaocool.wxtteacher.utils.ToastUtils;

public class ChangeChatNameActivity extends BaseActivity {

    private EditText edt_chat_name;
    private Context context;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    if (JsonParser.JSONparser(context,msg.obj.toString())){
                        ToastUtils.ToastShort(context,"修改成功");
                        Intent intent = new Intent();
                        intent.putExtra("chat_name", edt_chat_name.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else {
                        ToastUtils.ToastShort(context,"修改失败");
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_chat_name);
        getSupportActionBar().hide();
        context = this;
        edt_chat_name = (EditText) findViewById(R.id.edt_chat_name);
        edt_chat_name.setText(getIntent().getStringExtra("chat_name"));
        findViewById(R.id.up_jiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChatName();
            }
        });
    }

    private void saveChatName() {
        if (edt_chat_name.getText()==null||edt_chat_name.getText().equals("")){
            ToastUtils.ToastShort(context,"请输入名称");
        }else if (edt_chat_name.getText().equals(getIntent().getStringExtra("chat_name"))){
            ToastUtils.ToastShort(context,"群名称未修改！");
        }else {
            String url = "http://wxt.xiaocool.net/index.php?g=apps&m=message&a=UpdateGroupTitle";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uid",new UserInfo(context).getUserId()));
            params.add(new BasicNameValuePair("groupid",getIntent().getStringExtra("chatid")));
            params.add(new BasicNameValuePair("title",edt_chat_name.getText().toString()));
            new NewsRequest(context,handler).post(url,params,101);
        }

    }
}
