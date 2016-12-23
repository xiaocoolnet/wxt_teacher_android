package cn.xiaocool.wxtteacher;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import com.bugtags.library.Bugtags;

import cn.jpush.android.api.JPushInterface;
import cn.xiaocool.wxtteacher.app.ExitApplication;
import cn.xiaocool.wxtteacher.bean.UserInfo;
import cn.xiaocool.wxtteacher.main.LoginActivity;
import cn.xiaocool.wxtteacher.main.MainActivity;
import cn.xiaocool.wxtteacher.utils.IntentUtils;
import cn.xiaocool.wxtteacher.utils.LogUtils;
import cn.xiaocool.wxtteacher.utils.SPUtils;
import cn.xiaocool.wxtteacher.view.NiceDialog;
import cn.xiaocool.wxtteacher.view.WxtApplication;

public class BaseActivity extends AppCompatActivity implements ReceiverInterface {
    private IntentFilter myIntentFilter;
    GestureDetector mGestureDetector;
    private boolean mNeedBackGesture = false;
    private BaseReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        receiver = new BaseReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }



    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//        destroyRadio();
        WxtApplication.getInstance().removeActivity(this);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void destroyRadio() {
        getApplicationContext().unregisterReceiver(IntentReceicer);
    }

    @Override
    public void dealWithRadio(Intent intent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void regiserRadio(String[] actions) {
        myIntentFilter = new IntentFilter();
        for (int i = 0; i < actions.length; i++) {
            myIntentFilter.addAction(actions[i]);
        }
        getApplicationContext().registerReceiver(IntentReceicer, myIntentFilter);
    }

    private BroadcastReceiver IntentReceicer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dealWithRadio(intent);
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Bugtags.onDispatchTouchEvent(this, ev);
        if (mNeedBackGesture) {
            return mGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }



    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    public class BaseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.i("BaseReceiver", "接收到:");
            String loginFromOther = intent.getStringExtra("loginOther");
            if (loginFromOther.equals("loginFromOther")){
                final NiceDialog  mDialog = new NiceDialog(BaseActivity.this);
                mDialog.setTitle("提示");
                mDialog.setContent("您的账号在另一地点登录！");
                mDialog.setOKButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new UserInfo(context).clearDataExceptPhone(context);
                        JPushInterface.stopPush(context);
                        SPUtils.remove(context, "newsGroupRecive");
                        SPUtils.remove(context,"receiveParentWarn");
                        SPUtils.remove(context,"noticeRecive");
                        SPUtils.remove(context,"backlogData");
                        SPUtils.remove(context,"teacherCommunication");
                        SPUtils.remove(context,"homeWork");

                        MainActivity.mInstace.finish();
                        IntentUtils.getIntents(context, LoginActivity.class);
                        ExitApplication.getInstance().exit();
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
            }
        }

    }
}