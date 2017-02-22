package cn.xiaocool.wxtteacher.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.wxtteacher.BaseActivity;
import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.adapter.LocalImgGridAdapter;
import cn.xiaocool.wxtteacher.net.NewsRequest;
import cn.xiaocool.wxtteacher.net.request.constant.NetUtil;
import cn.xiaocool.wxtteacher.utils.StringUtils;
import cn.xiaocool.wxtteacher.utils.ToastUtils;
import cn.xiaocool.wxtteacher.utils.pushimage.PushImage;
import cn.xiaocool.wxtteacher.utils.pushimage.PushImageUtil;
import cn.xiaocool.wxtteacher.view.PicassoImageLoader;
import cn.xiaocool.wxtteacher.view.PicassoPauseOnScrollListener;

public class SuggestBackActivity extends BaseActivity {

    private EditText apply_content;
    private RelativeLayout send_btn,up_jiantou;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<String> mPhototNames;
    private String pushImgName;
    private KProgressHUD hud;
    private FunctionConfig functionConfig;
    private GridView homework_pic_grid;
    private LocalImgGridAdapter localImgGridAdapter;
    private Context mContext;
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1001:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    hud.dismiss();
                    try {
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            ToastUtils.ToastShort(SuggestBackActivity.this,"发送成功！");
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.ToastShort(SuggestBackActivity.this,"请求异常！");
                    }
                    break;
                case 5231:
                    mPhotoList.remove((int)msg.obj);
                    localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext,handler);
                    homework_pic_grid.setAdapter(localImgGridAdapter);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_back);
        mContext = this;
        initview();
    }

    private void initview() {
        apply_content = (EditText) findViewById(R.id.apply_content);
        send_btn = (RelativeLayout) findViewById(R.id.send_btn);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        homework_pic_grid = (GridView) findViewById(R.id.homework_pic_grid);
        mPhotoList = new ArrayList<>();
        mPhototNames = new ArrayList<>();
        //添加图片按钮
        localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext,handler);
        homework_pic_grid.setAdapter(localImgGridAdapter);
        homework_pic_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", String.valueOf(position));
                if (position == mPhotoList.size()) {
                    showActionSheet();
                }

            }
        });
    }

    private void sendMessage() {
        if (NetUtil.isConnnected(this)) {

            if (apply_content.getText().toString().length()>0){
                hud = KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true);
                hud.show();
                if (mPhotoList.size()>0){
                    new PushImageUtil().setPushIamge(mContext, mPhotoList, mPhototNames, new PushImage() {
                        @Override
                        public void success(boolean state) {
                            pushImgName = StringUtils.listToString(mPhototNames,",");
                            new NewsRequest(SuggestBackActivity.this,handler).send_suggest(apply_content.getText().toString(),1001);
                        }

                        @Override
                        public void error() {
                            hud.dismiss();
                            ToastUtils.ToastShort(mContext,"图片上传失败！");
                        }
                    });
                }else {

                    pushImgName = "null";
                    new NewsRequest(this,handler).send_suggest(apply_content.getText().toString(),1001);
                }


            }else {
                ToastUtils.ToastShort(this,"发送内容不能为空!");
            }


        } else {
            ToastUtils.ToastShort(mContext, "网络请求失败");
        }


    }



    /**
     * 展示dialog
     */

    private void showActionSheet() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;
        imageLoader = new PicassoImageLoader();
        pauseOnScrollListener = new PicassoPauseOnScrollListener(false, true);
        functionConfigBuilder.setMutiSelectMaxSize(9);
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        functionConfig = functionConfigBuilder.build();

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#9BE5B4"))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(Color.parseColor("#9BE5B4"))
                .setFabPressedColor(Color.BLUE)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.parseColor("#9BE5B4"))
                .setIconBack(R.drawable.ic_fanhui)
                .setIconRotate(R.mipmap.ic_action_repeat)
                .setIconCrop(R.mipmap.ic_action_crop)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(mContext, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                                break;
                            case 1:

                                //获取拍照权限
                                if (hasPermission("android.permission.CAMERA")) {
                                    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    // 判断存储卡是否可以用，可用进行存储
                                    String state = Environment.getExternalStorageState();
                                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                        File file = new File(path, "newpic.png");
                                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                    }
                                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);

                                } else {


                                    String[] perms = {"android.permission.CAMERA"};

                                    int permsRequestCode = 200;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(perms, permsRequestCode);
                                    }

                                }

                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    private boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private boolean hasPermission(String permission){

        if(canMakeSmores()){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return(checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);
            }

        }

        return true;

    }


    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted){
                    //授权成功之后，调用系统相机进行拍照操作等
                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);

                }else{
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtils.ToastShort(this,"已拒绝进入相机，如想开启请到设置中开启！");
                }

                break;

        }

    }

    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {

                mPhotoList.clear();


                mPhotoList.addAll(resultList);
                //添加图片按钮
                localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext,handler);
                homework_pic_grid.setAdapter(localImgGridAdapter);
                Log.e("mPhotoList", mPhotoList.toString());

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
}
