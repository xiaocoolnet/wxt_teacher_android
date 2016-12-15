package cn.xiaocool.wxtteacher.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import com.baoyz.actionsheet.ActionSheet;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.wxtteacher.BaseActivity;
import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.adapter.LocalImgGridAdapter;
import cn.xiaocool.wxtteacher.dao.CommunalInterfaces;
import cn.xiaocool.wxtteacher.net.NewsRequest;
import cn.xiaocool.wxtteacher.net.UserRequest;
import cn.xiaocool.wxtteacher.net.request.constant.NetUtil;
import cn.xiaocool.wxtteacher.utils.StringUtils;
import cn.xiaocool.wxtteacher.utils.ToastUtils;
import cn.xiaocool.wxtteacher.utils.pushimage.PushImage;
import cn.xiaocool.wxtteacher.utils.pushimage.PushImageUtil;
import cn.xiaocool.wxtteacher.view.PicassoImageLoader;
import cn.xiaocool.wxtteacher.view.PicassoPauseOnScrollListener;

public class AddNewsGroupActivity extends BaseActivity implements View.OnClickListener {





    private RelativeLayout up_jiantou;
    private EditText homework_content;
    private Intent intent;
    private TextView homework_send, tv_choose_class, tv_select_count;
    private GridView homework_pic_grid;
    private String id;

    private static final int ADD_KEY = 4;
    private String type;
    private LocalImgGridAdapter localImgGridAdapter;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<String> mPhototNames;
    private String pushImgName;
    private KProgressHUD hud;
    private FunctionConfig functionConfig;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_KEY:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            hud.dismiss();
                            Toast.makeText(AddNewsGroupActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            hud.dismiss();

                            Toast.makeText(AddNewsGroupActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 5231:
                    mPhotoList.remove((int)msg.obj);
                    localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, AddNewsGroupActivity.this,handler);
                    homework_pic_grid.setAdapter(localImgGridAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news_group);
        initView();
    }

    private void initView() {
        type = "消息群发";
        mPhotoList = new ArrayList<>();
        mPhototNames = new ArrayList<>();
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        tv_choose_class = (TextView) findViewById(R.id.choose_class);
        tv_choose_class.setOnClickListener(this);

        homework_content = (EditText) findViewById(R.id.homework_content);
        // homework_receiveList = (TextView) findViewById(R.id.homework_receiveList);
        homework_pic_grid = (GridView) findViewById(R.id.homework_pic_grid);
        homework_send = (TextView) findViewById(R.id.homework_send);
        homework_send.setOnClickListener(this);
        intent = getIntent();
        String classname = intent.getStringExtra("className");
        type = intent.getStringExtra("type");
        tv_choose_class.setText(classname);


        tv_select_count = (TextView) findViewById(R.id.tv_select_count);
        //添加图片按钮
        localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, AddNewsGroupActivity.this,handler);
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

        CoreConfig coreConfig = new CoreConfig.Builder(AddNewsGroupActivity.this, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        ActionSheet.createBuilder(AddNewsGroupActivity.this, getSupportFragmentManager())
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

                boolean cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
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
    private String genre;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.choose_class:
                Intent intent = new Intent(AddNewsGroupActivity.this, MyClassListActivity.class);
                intent.putExtra("type", type);
                startActivityForResult(intent, 101);
                break;
            case R.id.homework_send:

                //极光推送辨别身份(1家长 2教师)
                genre = "";
                if (type.equals("student")) {
                    genre = "1";
                } else {
                    genre = "2";
                }
                if (homework_content.getText().length() < 1) {
                    ToastUtils.ToastShort(AddNewsGroupActivity.this, "发送内容不能为空");
                } else {
                    hud = KProgressHUD.create(this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(true);
                    hud.show();
                    if (NetUtil.isConnnected(this)) {

                        if (id.equals(null)){
                            ToastUtils.ToastShort(AddNewsGroupActivity.this, "请选择接收人！");
                        }else {
                            if (mPhotoList.size() < 1) {
                                pushImgName = "null";
                                new NewsRequest(AddNewsGroupActivity.this, handler).send_newsgroup(homework_content.getText().toString(), id, pushImgName,genre, ADD_KEY);
                            } else {

                                new PushImageUtil().setPushIamge(AddNewsGroupActivity.this, mPhotoList, mPhototNames, new PushImage() {
                                    @Override
                                    public void success(boolean state) {
                                        hud.dismiss();
                                        pushImgName = StringUtils.listToString(mPhototNames,",");
                                        new NewsRequest(AddNewsGroupActivity.this, handler).send_newsgroup(homework_content.getText().toString(), id, pushImgName,genre, ADD_KEY);
                                    }

                                    @Override
                                    public void error() {
                                        ToastUtils.ToastShort(AddNewsGroupActivity.this,"图片上传失败！");
                                        hud.dismiss();
                                    }
                                });
                            }

                        }



                    } else {
                        hud.dismiss();
                        ToastUtils.ToastShort(AddNewsGroupActivity.this, "网络请求失败");
                    }
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

                localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, AddNewsGroupActivity.this,handler);
                homework_pic_grid.setAdapter(localImgGridAdapter);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(AddNewsGroupActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    //重写onActivityResult以获得你需要的信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 101:
                    if (data != null) {
                        String sss = data.getStringExtra("sss");
                        Log.e("sssss", sss);
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

                        id =StringUtils.listToString(ids,",");
                        tv_select_count.setText("共选择" + ids.size() + "人");
                        tv_choose_class.setText(haschoose);
                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
