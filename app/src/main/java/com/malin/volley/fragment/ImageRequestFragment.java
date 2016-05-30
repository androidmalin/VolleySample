package com.malin.volley.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.malin.volley.R;
import com.malin.volley.utils.MapToJsonString;
import com.malin.volley.utils.VolleyUtil;
import com.orhanobut.logger.Logger;

import java.util.Map;

/**
 * Created by malin on 16-5-30.
 */
public class ImageRequestFragment extends Fragment {

    private static final String TAG = ImageRequestFragment.class.getSimpleName();
    private ImageRequestFragment mStringFragment;
    private Bundle mBundle;
    private String mType;
    private RequestQueue mRequestQueue;
    private static final String URL_IMAGE = "http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg";
    private ImageView mImageView;

    public ImageRequestFragment newFragemnt(Bundle bundle) {
        mStringFragment = new ImageRequestFragment();
        mStringFragment.setArguments(bundle);
        return mStringFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_one_layout, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mImageView = (ImageView) view.findViewById(R.id.iv_image_request_one);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        if (mBundle != null) {
            mType = mBundle.getString("");
        }
        mRequestQueue = VolleyUtil.getRequestQueue(getActivity());
    }

    private void getImageViaVolley() {

        //RequestQueue  mRequestQueue = Volley.newRequestQueue(mContext);

        /**
         *  ImageRequest的构造函数接收六个参数
         * 第一个参数就是图片的URL地址，这个没什么需要解释的
         * 第二个参数是图片请求成功的回调，这里我们把返回的Bitmap参数设置到ImageView中
         * 第三个参数用于指定允许图片最大的宽度
         * 第四个参数用于指定允许图片最大的高度
         *
         * 如果指定的网络图片的宽度或高度大于这里的最大值，则会对图片进行压缩
         * 指定成0的话就表示不管图片有多大，都不会进行压缩。

         * 第五个参数用于指定图片的颜色属性
         * Bitmap.Config下的几个常量都可以在这里使用，
         * 其中ARGB_8888可以展示最好的颜色属性，每个图片像素占据4个字节的大小
         * 而RGB_565则表示每个图片像素占据2个字节大小。

         * 第六个参数是图片请求失败的回调，这里我们当请求失败时在ImageView中显示一张默认图片
         * 最后将这个ImageRequest对象添加到RequestQueue里就可以了，如下所示：
         **/

        /**2.
         * String url:图片的URL地址
         * Response.Listener<Bitmap> listener:图片请求成功的回调
         * int maxWidth:图片最大的宽度
         * int maxHeight:图片最大的高度
         * ScaleType scaleType,
         * Config decodeConfig,
         * Response.ErrorListener errorListener
         */
        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        boolean isMainUi = Looper.getMainLooper()==Looper.myLooper();
                        Logger.d("isMainUi:" + isMainUi);
                        Logger.d("成功");
                        if (bitmap != null && !bitmap.isRecycled()) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                },
                2000,
                2000,
                ImageView.ScaleType.CENTER,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.mipmap.bili_default_avatar);
                        boolean isMainUi = Looper.getMainLooper()==Looper.myLooper();
                        Logger.e("isMainUi:" + isMainUi);
                        Logger.e("失败");
                        Logger.e(error.getMessage(), error);
                        NetworkResponse networkResponse = error.networkResponse;
                        int statusCode = networkResponse.statusCode;
                        byte[] data = networkResponse.data;
                        Map<String, String> headers = networkResponse.headers;
                        boolean notModified = networkResponse.notModified;
                        long networkTimeMs = networkResponse.networkTimeMs;
                        Logger.e("statusCode:" + statusCode);

                        if (data != null) {
                            Logger.e("data:" + data.toString());
                        } else {
                            Logger.e("data==null");
                        }

                        if (headers != null) {
                            String json = MapToJsonString.mapToJSONObject(headers);
                            Logger.json(json);
                        } else {
                            Logger.e("headers == null");
                        }

                        Logger.e("notModified:" + notModified);
                        Logger.e("networkTimeMs:" + networkTimeMs + "ms");
                    }
                }


        );

        mRequestQueue.add(imageRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getImageViaVolley();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue!=null){
            mRequestQueue.cancelAll(TAG);
        }
    }
}
