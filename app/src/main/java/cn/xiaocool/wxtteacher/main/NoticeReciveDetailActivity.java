package cn.xiaocool.wxtteacher.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xiaocool.wxtteacher.BaseActivity;
import cn.xiaocool.wxtteacher.R;
import cn.xiaocool.wxtteacher.adapter.ImgGridAdapter;
import cn.xiaocool.wxtteacher.bean.NoticeRecive;
import cn.xiaocool.wxtteacher.bean.UserInfo;
import cn.xiaocool.wxtteacher.dao.CommunalInterfaces;
import cn.xiaocool.wxtteacher.fragment.announce.NoticeReciveFragment;
import cn.xiaocool.wxtteacher.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtteacher.ui.NoScrollGridView;
import cn.xiaocool.wxtteacher.ui.NoScrollListView;
import cn.xiaocool.wxtteacher.ui.ProgressViewUtil;
import cn.xiaocool.wxtteacher.ui.RoundImageView;
import cn.xiaocool.wxtteacher.utils.JsonParser;
import cn.xiaocool.wxtteacher.utils.ToastUtils;
import cn.xiaocool.wxtteacher.utils.VolleyUtil;

public class NoticeReciveDetailActivity extends BaseActivity {

    private TextView homework_content,teacher_name,homework_time,homework_item_praise_names,alread_text;
    private ImageView homework_praise,homework_img,homework_discuss,quit;
    private LinearLayout linearLayout_homework_item_praise,edit_and_send;
    private RelativeLayout news_group_comment_layout;
    private NoScrollListView news_group_comment_list;
    private NoScrollGridView parent_warn_img_gridview;
    private View bg_divider;
    private static final int HOMEWORK_PRAISE_KEY = 104;
    private static final int DEL_HOMEWORK_PRAISE_KEY = 105;
    private DisplayImageOptions displayImage;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context context;
    private UserInfo user;
    private NoticeRecive.DataBean homeworkData;
    private RoundImageView item_head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_group_detail);
        context = this;
        initData();
        initview();

    }

    private void initData() {
        Intent intent = this.getIntent();
        homeworkData =(NoticeRecive.DataBean)intent.getSerializableExtra("noticedata");
        user = new UserInfo(context);
        user.readData(context);
        //读取信息
        readMessage();
    }

    private void readMessage() {
        boolean readFlag = false;
        for (int i = 0; i < homeworkData.getReceiv_list().size(); i++) {
            if (homeworkData.getReceiv_list().get(i).getReceiverid().equals(user.getUserId())){
                if (homeworkData.getReceiv_list().get(i).getCreate_time()!=null&&!homeworkData.getReceiv_list().get(i).getCreate_time().equals("0")){
                    readFlag = true;
                }
                break;
            }
        }
        if (readFlag){
            ToastUtils.ToastShort(context,"您已经读取过！");
            return;
        }
        String url = "http://wxt.xiaocool.net/index.php?g=apps&m=school&a=read_notice";
        url = url +"&noticeid="+homeworkData.getNoticeid()+"&receiverid="+user.getUserId();
        Log.e("readMessage",url);
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonParser.JSONparser(context, result)) {
                    ToastUtils.ToastShort(context, "读取信息成功");
                    getAllInformation();
                } else {
                    Log.e("readMessage", result);
                }
            }

            @Override
            public void onError() {
                Log.e("readMessage", "读取信息error");
            }
        });
    }

    private void getAllInformation() {

        RequestQueue mQueue = Volley.newRequestQueue(context);
        String URL = "http://wxt.xiaocool.net/index.php?g=apps&m=school&a=get_receive_notice&receiverid=" + user.getUserId();
        Log.e("uuuurrrrll", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String arg0) {
                Log.d("onResponse", arg0);

                try {
                    JSONObject jsonObject = new JSONObject(arg0);
                    String state = jsonObject.optString("status");
                    ProgressViewUtil.dismiss();
                    if (state.equals(CommunalInterfaces._STATE)) {
                        JSONArray dataArray = jsonObject.optJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.optJSONObject(i);
                            if (dataObject.optString("noticeid").equals(homeworkData.getNoticeid())){
                                NoticeRecive.DataBean dataBean = new NoticeRecive.DataBean();
                                dataBean.setId(dataObject.optString("id"));
                                dataBean.setNoticeid(dataObject.optString("noticeid"));
                                dataBean.setReceiverid(dataObject.optString("receiverid"));
                                dataBean.setReceivertype(dataObject.optString("receivertype"));
                                dataBean.setCreate_time(dataObject.optString("create_time"));
                                JSONArray send_message = dataObject.optJSONArray("notice_info");
                                ArrayList<NoticeRecive.DataBean.NoticeInfoBean> noticeInfoBeanArrayList = new ArrayList<>();

                                for (int j = 0; j < send_message.length(); j++) {
                                    JSONObject noticeinfoobject = send_message.optJSONObject(j);
                                    NoticeRecive.DataBean.NoticeInfoBean sendMessageBean = new NoticeRecive.DataBean.NoticeInfoBean();

                                    sendMessageBean.setName(noticeinfoobject.optString("name"));
                                    sendMessageBean.setPhoto(noticeinfoobject.optString("photo"));
                                    sendMessageBean.setId(noticeinfoobject.optString("id"));
                                    sendMessageBean.setUserid(noticeinfoobject.optString("userid"));
                                    sendMessageBean.setTitle(noticeinfoobject.optString("title"));
                                    sendMessageBean.setType(noticeinfoobject.optString("type"));
                                    sendMessageBean.setContent(noticeinfoobject.optString("content"));
                                    sendMessageBean.setCreate_time(noticeinfoobject.optString("create_time"));
                                    noticeInfoBeanArrayList.add(sendMessageBean);
                                }


                                dataBean.setNotice_info(noticeInfoBeanArrayList);


                                JSONArray picArray = dataObject.optJSONArray("pic");
                                List<NoticeRecive.DataBean.PicBean> pictureBeanList = new ArrayList<>();
                                for (int j = 0; j < picArray.length(); j++) {
                                    JSONObject picObject = picArray.optJSONObject(j);
                                    NoticeRecive.DataBean.PicBean pictureBean = new NoticeRecive.DataBean.PicBean();
                                    pictureBean.setPhoto(picObject.optString("photo"));
                                    pictureBeanList.add(pictureBean);
                                }
                                dataBean.setPic(pictureBeanList);

                                JSONArray receiverArray = dataObject.optJSONArray("receiv_list");
                                List<NoticeRecive.DataBean.ReceivListBean> receiverBeanList = new ArrayList<>();
                                for (int j = 0; j < receiverArray.length(); j++) {
                                    JSONObject reciverObject = receiverArray.optJSONObject(j);
                                    NoticeRecive.DataBean.ReceivListBean receiverBean = new NoticeRecive.DataBean.ReceivListBean();
                                    receiverBean.setId(reciverObject.optString("id"));
                                    receiverBean.setNoticeid(reciverObject.optString("noticeid"));
                                    receiverBean.setReceiverid(reciverObject.optString("receiverid"));
                                    receiverBean.setReceivertype(reciverObject.optString("receivertype"));
                                    receiverBean.setPhone(reciverObject.optString("phone"));
                                    receiverBean.setPhoto(reciverObject.optString("photo"));
                                    receiverBean.setName(reciverObject.optString("name"));
                                    receiverBean.setCreate_time(reciverObject.optString("create_time"));
                                    receiverBeanList.add(receiverBean);

                                }
                                dataBean.setReceiv_list(receiverBeanList);
                                homeworkData = dataBean;
                                break;
                            }

                        }

                     fillData();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.d("onErrorResponse", arg0.toString());
            }
        });


        mQueue.add(request);

    }
    private void initview() {

        //初始化组件
        item_head = (RoundImageView) findViewById(R.id.item_head);
        parent_warn_img_gridview = (NoScrollGridView) findViewById(R.id.parent_warn_img_gridview);
        quit = (ImageView) findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit_and_send = (LinearLayout) findViewById(R.id.edit_and_send);
        bg_divider =  findViewById(R.id.bg_divider);
        news_group_comment_layout = (RelativeLayout)findViewById(R.id.news_group_comment_layout);
        news_group_comment_list = (NoScrollListView) findViewById(R.id.news_group_comment_list);
        homework_content = (TextView) findViewById(R.id.myhomework_content);
        teacher_name = (TextView) findViewById(R.id.item_title);
        homework_time = (TextView) findViewById(R.id.item_time);
        alread_text = (TextView) findViewById(R.id.alread_text);
        homework_praise = (ImageView) findViewById(R.id.homework_praise);
        homework_discuss = (ImageView)findViewById(R.id.homework_discuss);
        homework_img = (ImageView) findViewById(R.id.homework_img);
        homework_item_praise_names = (TextView) findViewById(R.id.homework_item_praise_names);
        linearLayout_homework_item_praise = (LinearLayout)findViewById(R.id.linearLayout_homework_item_praise);
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                .cacheInMemory(true).cacheOnDisc(true).build();

        fillData();
    }

    private void fillData() {
        //填充数据

        imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+homeworkData.getNotice_info().get(0).getPhoto(),item_head,displayImage);
        homework_content.setText(homeworkData.getNotice_info().get(0).getContent());
        teacher_name.setText(homeworkData.getNotice_info().get(0).getName());


        final ArrayList<NoticeRecive.DataBean.ReceivListBean> notReads = new ArrayList<>();
        final ArrayList<NoticeRecive.DataBean.ReceivListBean> alreadyReads = new ArrayList<>();
        for (int i=0;i<homeworkData.getReceiv_list().size();i++){
            if (homeworkData.getReceiv_list().get(i).getCreate_time()==null||homeworkData.getReceiv_list().get(i).getCreate_time().equals("null")||homeworkData.getReceiv_list().get(i).getCreate_time().equals("0")){
                notReads.add(homeworkData.getReceiv_list().get(i));
            }else {
                alreadyReads.add(homeworkData.getReceiv_list().get(i));
            }
        }
        alread_text.setText("总发" + homeworkData.getReceiv_list().size()+" 已读"+alreadyReads.size()+" 未读"+notReads.size());
        alread_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setClass(context, ReadAndNoreadActivity.class);
                intent.putExtra("type", "recivenotice");
                Bundle bundle=new Bundle();
                bundle.putSerializable("notReads",(Serializable)notReads);//序列化
                bundle.putSerializable("alreadyReads", (Serializable)alreadyReads);
                intent.putExtras(bundle);//发送数据
//                intent.putExtras("notReads",(Serializable)notReads);
                context.startActivity(intent);//启动intent
            }
        });


        Date date = new Date();
        date.setTime(Long.parseLong(homeworkData.getNotice_info().get(0).getCreate_time()) * 1000);
        homework_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));



        if (homeworkData.getPic().size()>1){
            homework_img.setVisibility(View.GONE);
            parent_warn_img_gridview.setVisibility(View.VISIBLE);

            final ArrayList<String> pics = new ArrayList<>();
            for (int k=0;k<homeworkData.getPic().size();k++){
                pics.add(homeworkData.getPic().get(k).getPhoto());
            }
            ImgGridAdapter parWarnImgGridAdapter = new ImgGridAdapter(pics,context);
            parent_warn_img_gridview.setAdapter(parWarnImgGridAdapter);
            parent_warn_img_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, CircleImagesActivity.class);
                    intent.putStringArrayListExtra("Imgs",pics);
                    intent.putExtra("type","4");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        }else if (homeworkData.getPic().size()==1 && !homeworkData.getPic().get(0).getPhoto().equals("null") && !homeworkData.getPic().get(0).getPhoto().equals("")){
            parent_warn_img_gridview.setVisibility(View.GONE);
            homework_img.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            final ArrayList<String> pics = new ArrayList<>();
            for (int k=0;k<homeworkData.getPic().size();k++){
                pics.add(homeworkData.getPic().get(k).getPhoto());
            }
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + homeworkData.getPic().get(0).getPhoto(), homework_img, displayImage);
            Log.d("img", "http://wxt.xiaocool.net/" +homeworkData.getPic().get(0));
            homework_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, CircleImagesActivity.class);
                    intent.putStringArrayListExtra("Imgs", pics);
                    intent.putExtra("type","4");
                    context.startActivity(intent);
                }
            });
        }else {
            parent_warn_img_gridview.setVisibility(View.GONE);
            homework_img.setVisibility(View.GONE);

        }
    }

}
