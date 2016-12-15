package cn.xiaocool.wxtteacher.utils.pushimage;

import com.android.internal.http.multipart.Part;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by 尉鑫鑫 on 2016/9/19.
 */
public class VolleyRequest extends StringRequest {
    private Part[] parts;

    public VolleyRequest(String url, Part[] parts, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        this.parts = parts;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + Part.getBoundary();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Part.sendParts(baos, parts);
        } catch (IOException e) {
            VolleyLog.e(e, "error when sending parts to output!");
        }
        return baos.toByteArray();
    }
}
