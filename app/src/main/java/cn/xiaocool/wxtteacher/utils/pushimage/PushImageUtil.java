package cn.xiaocool.wxtteacher.utils.pushimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.wxtteacher.net.request.constant.UserNetConstant;
import cn.xiaocool.wxtteacher.utils.JsonParser;


/**
 * Created by Administrator on 2016/8/25.
 */
public class PushImageUtil {
    private static final int ADD_KEY = 4;
    private static final int ADD_IMG_KEY1 = 101;
    private static final int ADD_IMG_KEY2 = 102;
    private static final int ADD_IMG_KEY3 = 103;
    private static final int ADD_IMG_KEY4 = 104;
    private static final int ADD_IMG_KEY5 = 105;
    private static final int ADD_IMG_KEY6 = 106;
    private static final int ADD_IMG_KEY7 = 107;
    private static final int ADD_IMG_KEY8 = 108;
    private static final int ADD_IMG_KEY9 = 109;
    private static Context mContext;
    private int imgNums = 0;
    private int key = 0;
    private List<PhotoInfo> photoWithPaths;
    private boolean isOk;
    private PushImage pushIamge;
    private List<String> arrayList=new ArrayList<>();
    private boolean needCompress = true;//是否需要压缩

    public void setPushIamge(Context context, List<PhotoInfo> p, List<String> list, PushImage pushIamge) {
        this.photoWithPaths = p;
        this.arrayList=list;
        this.mContext = context;
        this.pushIamge = pushIamge;
        if (photoWithPaths.size()>0){
            pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY1);
        }else {
            pushIamge.error();
        }

    }

    public void setPushIamge(Context context, List<PhotoInfo> p, List<String> list, PushImage pushIamge, boolean needCompress) {
        this.needCompress = needCompress;
        this.setPushIamge(context,p,list,pushIamge);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_IMG_KEY1:
                    if (msg.obj != null) {
                      if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                          imgNums = 1;
//                          arrayList=JsonResult.JsonParser((String) msg.obj);
//                          imageReturn.setData(arrayList.get(0).toString());
                          if (imgNums < photoWithPaths.size()) {
                              pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY2);
                          }else {
                              pushIamge.success(true);
                              isOk = true;
                          }
                      }else {
                          pushIamge.error();
                          Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                      }
                    }
                    break;
                case ADD_IMG_KEY2:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 2;
//                            arrayList=JsonResult.JsonParser((String) msg.obj);
//                            imageReturn.setData(arrayList.get(0).toString());
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY3);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY3:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 3;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY4);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY4:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 4;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY5);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY5:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 5;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY6);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY6:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 6;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY7);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY7:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 7;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY8);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY8:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 8;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY9);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY9:
                    if (msg.obj != null) {
                        if (JsonParser.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 9;
                            isOk = true;
                            pushIamge.success(true);

                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

            }
        }
    };

    private void pushImage(PhotoInfo photoWithPath, int  addImgKey) {
        pushImg(photoWithPath.getPhotoPath(), addImgKey);
    }

    public void pushImg(final String picPath,int  addImgKey){
        if(needCompress){
            convertBitmap(convertToBitmap(picPath, 720, 1280), addImgKey);
        }else{
            compressImageWithRatio(picPath,addImgKey);
        }

    }
    public void updatePhoto(final File f,final int KEY){

        new Thread(){
            Message msg = Message.obtain();
            @Override
            public void run() {
                List<Part> list=new ArrayList<Part>();
                try {
                    list.add(new FilePart("upfile",f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String url1= UserNetConstant.NET_USER_UPLOAD_HEAD_IMG;
                VolleyRequest request=new VolleyRequest(url1, list.toArray(new Part[list.size()]),new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject obj = new JSONObject(s);
                            msg.what = KEY;
                            msg.obj = obj;
                            Log.d("===图片张数",imgNums+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            handler.sendMessage(msg);
                        }
                        Log.d("===  图片上传", s);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
            }
        }.start();

    }



    public void convertBitmap(Bitmap bitmap,int  addImgKey){
        File appDir = new File(Environment.getExternalStorageDirectory(), "zxy");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random=new Random();
        String fileName ="yyy"+ random.nextInt(10000)+System.currentTimeMillis() + ".jpg";
        arrayList.add(fileName);
        Log.d("5555777", arrayList.size()+"");
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            updatePhoto(file, addImgKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compressImageWithRatio(String srcPath,int addImgKey){
        File appDir = new File(Environment.getExternalStorageDirectory(), "51helper");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random=new Random();
        String fileName ="yyy"+ random.nextInt(10000)+System.currentTimeMillis() + ".jpg";
        arrayList.add(fileName);
        File file = new File(appDir, fileName);
        updatePhoto(ImageCompress.compressPicture(srcPath,file),addImgKey);

    }


    //将图片路径转换为bitmap格式
    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        return BitmapFactory.decodeFile(path, opts);
        //WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        //return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }


}
