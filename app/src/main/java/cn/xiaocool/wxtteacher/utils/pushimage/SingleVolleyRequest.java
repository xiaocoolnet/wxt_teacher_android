package cn.xiaocool.wxtteacher.utils.pushimage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by asus on 2016-05-06.
 */
public class SingleVolleyRequest {
    //私有化属性
    private static SingleVolleyRequest singleQueue;
    private RequestQueue requestQueue;
    private static Context context;
    //私有构造
    private SingleVolleyRequest(Context context){
        this.context=context;
        requestQueue=getRequestQueue();

    }
    //提供获取请求队列的方法
   public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context);//重新new一个，如果为null，重新生成

        }
        return requestQueue;

    }
    //提供获取类对象的方法
    public  static synchronized SingleVolleyRequest getInstance(Context context){
        if (singleQueue==null){
            singleQueue=new SingleVolleyRequest(context);
        }
        return singleQueue;
    }
    public <T>void addToRequestQueue(Request<T> red){
        getRequestQueue().add(red);
    }
}
