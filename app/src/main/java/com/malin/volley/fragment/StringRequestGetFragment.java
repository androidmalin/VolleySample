package com.malin.volley.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.malin.volley.R;
import com.malin.volley.utils.StringUtil;
import com.malin.volley.utils.VolleyUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by malin on 16-5-30.
 */
public class StringRequestGetFragment extends Fragment {

    private static final String TAG = StringRequestGetFragment.class.getSimpleName();
    private StringRequestGetFragment mStringFragment;
    private Bundle mBundle;
    private String mType;
    private RequestQueue mRequestQueue;
    private static final String URL_JSON = "http://www.weather.com.cn/adat/sk/101010100.html";//查询北京天气信息

    private TextView mTextView;

    public StringRequestGetFragment newFragemnt(Bundle bundle) {
        mStringFragment = new StringRequestGetFragment();
        mStringFragment.setArguments(bundle);
        return mStringFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.string_layout, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mTextView = (TextView) view.findViewById(R.id.tv_w_result);
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

    private void getDataUseVolleyGet() {
        //请求之前，先取消之前的请求（取消还没有进行完的请求）
        VolleyUtil.getRequestQueue(getActivity()).cancelAll(TAG);

//        1. 创建一个RequestQueue对象。
//        2. 创建一个StringRequest对象。
//        3. 将StringRequest对象添加到RequestQueue里面。

        /**
         * 1.创建一个RequestQueue对象
         *
         * RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，然后按照一定的算法并发地发出这些请求。
         * RequestQueue内部的设计就是非常合适高并发的，因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，
         * 这是非常浪费资源的，基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了
         */

//        mRequestQueue = Volley.newRequestQueue(getActivity());

        /**
         * 2.创建一个StringRequest对象
         *
         * StringRequest的构造函数需要传入三个参数
         *
         * 第一个:目标服务器的URL地址，
         * 第二个:服务器响应成功的回调，
         * 第三个:服务器响应失败的回调。
         */
        StringRequest stringRequest = new StringRequest(
                URL_JSON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        boolean isMainUi = Looper.getMainLooper() == Looper.myLooper();
                        Logger.d("isMainUi:" + isMainUi);
                        Logger.d("成功");

                        if (!TextUtils.isEmpty(response)){
                            Logger.json(response);
                            mTextView.setText(StringUtil.formatString(response));
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        boolean isMainUi = Looper.getMainLooper() == Looper.myLooper();
                        Logger.e("isMainUi:" + isMainUi);
                        Logger.e("失败");
                        Logger.e(error.getMessage(), error);
                    }
                });
        /**
         * 3.将StringRequest对象添加到RequestQueue里面
         * StringRequest对象添加到RequestQueue
         */
        mRequestQueue.add(stringRequest);
        stringRequest.setTag(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataUseVolleyGet();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue!=null){
            mRequestQueue.cancelAll(TAG);
        }
    }
}
