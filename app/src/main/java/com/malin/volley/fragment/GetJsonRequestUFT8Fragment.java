package com.malin.volley.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.malin.volley.R;
import com.malin.volley.utils.StringUtil;
import com.malin.volley.utils.VolleyUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

/**
 * Created by malin on 16-5-30.
 */
public class GetJsonRequestUFT8Fragment extends Fragment {

    private static final String TAG = GetJsonRequestUFT8Fragment.class.getSimpleName();
    private GetJsonRequestUFT8Fragment mStringFragment;
    private Bundle mBundle;
    private String mType;
    private RequestQueue mRequestQueue;
    private static final String URL_JSON = "http://www.weather.com.cn/adat/sk/101010100.html";//查询北京天气信息

    private TextView mTextView;

    public GetJsonRequestUFT8Fragment newFragemnt(Bundle bundle) {
        mStringFragment = new GetJsonRequestUFT8Fragment();
        mStringFragment.setArguments(bundle);
        return mStringFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.json_uft_8_layout, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mTextView = (TextView) view.findViewById(R.id.tv_json_utf_8_result);
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

    private void getJsonDataVolleyGet() {

        /**
         * 1.创建一个RequestQueue对象
         */
        //RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);

        /**
         * 2.创建一个StringRequest对象。
         */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                URL_JSON,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean isMainUi = Looper.getMainLooper()==Looper.myLooper();
                        Logger.d("isMainUi:" + isMainUi);
                        Logger.d("成功");
                        if (response != null) {
                            Logger.json(response.toString());
                            mTextView.setText(StringUtil.formatString(response.toString()));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        boolean isMainUi = Looper.getMainLooper()==Looper.myLooper();
                        Logger.e("isMainUi:" + isMainUi);
                        Logger.e("失败");
                        Logger.e(error.getMessage(), error);
                    }
                });

        /**
         *  3.将StringRequest对象添加到RequestQueue里面
         */
        mRequestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getJsonDataVolleyGet();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue!=null){
            mRequestQueue.cancelAll(TAG);
        }
    }
}
