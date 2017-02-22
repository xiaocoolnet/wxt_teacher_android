package cn.xiaocool.wxtteacher.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.ByteArrayOutputStream;

import cn.xiaocool.wxtteacher.BaseActivity;
import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.ui.SharePopupWindow;
import cn.xiaocool.wxtteacher.utils.ToastUtils;
import cn.xiaocool.wxtteacher.view.WxtApplication;
import okhttp3.internal.Util;

public class ShareAppActivity extends BaseActivity {

    private ImageView erweima_img;
    SharePopupWindow takePhotoPopWin;
    private   int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_share_app);
        erweima_img = (ImageView) findViewById(R.id.erweima_img);
        findViewById(R.id.up_jiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom(v);
            }
        });
    }

    public void showPopFormBottom(View view) {
        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        //SharePopupWindow takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.root_layout), Gravity.TOP, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.haoyou:
                    ToastUtils.ToastShort(ShareAppActivity.this, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.ToastShort(ShareAppActivity.this, "微信朋友圈");
                    history();
                    break;
            }
        }
    };


    /**
     * 分享到微信好友
     */
    private void setting() {
        //ToastUtils.ToastShort(this, "分享到微信好友");
        flag = 0;
        shareWX();
        takePhotoPopWin.dismiss();

    }

    /**
     * 分享到微信朋友圈
     */
    private void history() {
        // ToastUtils.ToastShort(this, "分享到微信朋友圈");
        flag = 1;
        shareWX();
        takePhotoPopWin.dismiss();
    }

    private static int THUMB_SIZE = 100;
    /**
     * 微信分享网页
     * */
    private void shareWX() {

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.teacher_erweima);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);  // ��������ͼ

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        WxtApplication wxtApplication = WxtApplication.getmInstance();
        wxtApplication.api.sendReq(req);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
