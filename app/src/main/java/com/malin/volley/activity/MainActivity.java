package com.malin.volley.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.malin.volley.R;
import com.malin.volley.bean.Weather;
import com.malin.volley.bean.WeatherInfo;
import com.malin.volley.request.ChartStringRequest;
import com.malin.volley.request.GsonRequest;
import com.malin.volley.request.XMLRequest;
import com.malin.volley.utils.DensityUtil;
import com.malin.volley.utils.JsonUtils;
import com.malin.volley.utils.MapToJsonString;
import com.malin.volley.utils.StringUtil;
import com.malin.volley.utils.VolleyUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "UUCty";
    private Context mContext;
    private ImageView mImageView;
    private ImageView mImageViewTwo;
    private NetworkImageView mNetworkImageView;
    private TextView mTextViewGetResult;
    private TextView mTextViewGetResultUFT8;
    private TextView mTextViewPostResultUTF8;
    private TextView mTextViewJsonResult;
    private TextView mTextViewXML;
    private TextView mTextViewWeather;
    private static final String URL_OK = "http://www.baidu.com";
    private static final String URL_JSON = "http://www.weather.com.cn/adat/sk/101010100.html";//查询北京天气信息
    private static final String URL_IMAGE = "http://i0.hdslb.com/Wallpaper/2011-autumn.jpg";
    private static final String URL_IMAGE_TWO = "http://i0.hdslb.com/Wallpaper/summer_2011_wide.jpg";//4320*1080
    private static final String URL_IMAGE_THREE = "http://i0.hdslb.com/Wallpaper/BILIBILI-dong.jpg";
    private static final String URL_XML = "http://flash.weather.com.cn/wmaps/xml/china.xml";
    private RequestQueue mRequestQueue;

    /**
     * 取出缓存文件的步骤
     *1. adb shell su 0 cp /data/data/com.malin.volley/cache/volley/-993813455-74959100 /sdcard/
     *2. cd
     *3. adb pull /sdcard/-993813455-74959100 /home/malin/volleycache
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initValue();
        initView();
        getDataUseVolleyGet();
        getDataUseVolleyGetUFT8();
        getDataPostVolley();
        getJsonDataVolleyGet();
        getImageViaVolley();
        loadImageViaVolleyImageLoader();
        getNetworkImageViewViaVolley();
        getXML();
        getGsonData();
    }

    private void initValue() {
        Logger.init(TAG);
        mContext = getApplicationContext();
        mRequestQueue = VolleyUtil.getRequestQueue(getApplicationContext());
    }

    private void initView() {
        mTextViewGetResult = (TextView) findViewById(R.id.tv_get_result);
        mTextViewGetResultUFT8 = (TextView) findViewById(R.id.tv_get_result_uft8);
        mTextViewPostResultUTF8 = (TextView) findViewById(R.id.tv_post_result_uft8);
        mTextViewJsonResult = (TextView) findViewById(R.id.tv_get_json_result);
        mImageView = (ImageView) findViewById(R.id.iv_image);
        mImageViewTwo = (ImageView) findViewById(R.id.iv_image_two);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.networkImageView);
        mTextViewXML = (TextView) findViewById(R.id.tv_xml_result);
        mTextViewWeather = (TextView) findViewById(R.id.tv_weather_result);
    }


    /**
     * get
     */
    private void getDataUseVolleyGet() {

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

        //adb shell su 0 cp /data/data/com.malin.volley/cache/volley/541731702-1257645756 /sdcard/
        //adb shell su 0 cp /data/data/com.malin.volley/cache/volley/-993813455-74959100 /sdcard/
        //cd
        //adb pull /sdcard/-993813455-74959100 /home/malin/volleycache
        mRequestQueue = Volley.newRequestQueue(mContext);

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

                        boolean isMainUi = getIsMainUI();
                        Logger.d("isMainUi:" + isMainUi);
                        Logger.d("成功");
                        if (!TextUtils.isEmpty(response)) {
                            Logger.json(response);
                            mTextViewGetResult.setText(StringUtil.formatString(response));
                        } else {
                            mTextViewGetResult.setText("response==null");
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        boolean isMainUi = getIsMainUI();
                        Logger.e("isMainUi:" + isMainUi);
                        Logger.e("失败");
                        if (error != null) {
                            Logger.e(error.getMessage(), error);
                            mTextViewGetResult.setText(error.getMessage());
                        } else {
                            mTextViewGetResult.setText("error==null");
                        }
                    }
                });
        /**
         * 3.将StringRequest对象添加到RequestQueue里面
         * StringRequest对象添加到RequestQueue
         */
        mRequestQueue.add(stringRequest);
        stringRequest.setTag("stringRequest");
    }


    /**
     * get utf-8
     */
    private void getDataUseVolleyGetUFT8() {

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
        //RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);

        /**
         * 2.创建一个StringRequest对象
         *
         * StringRequest的构造函数需要传入三个参数
         *
         * 第一个:目标服务器的URL地址，
         * 第二个:服务器响应成功的回调，
         * 第三个:服务器响应失败的回调。
         */
        ChartStringRequest stringRequest = new ChartStringRequest(
                URL_JSON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        boolean isMainUi = getIsMainUI();
                        Logger.d("isMainUi:" + isMainUi);
                        Logger.d("成功");
                        Logger.json(response);
                        mTextViewGetResultUFT8.setText(StringUtil.formatString(response));
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        boolean isMainUi = getIsMainUI();
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
    }


    /**
     * post
     */
    private void getDataPostVolley() {

        /**
         * 1.创建一个RequestQueue对象
         */
        //RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);

        /**
         * 2.创建一个StringRequest对象。
         */
        ChartStringRequest stringRequest = new ChartStringRequest(
                Request.Method.POST,
                URL_OK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean isMainUi = getIsMainUI();
                        Logger.d("isMainUi:" + isMainUi);
                        Logger.d("成功");
                        if (JsonUtils.isGoodJson(response)){
                            Logger.d("response is json");
                            Logger.json(response);
                        }else{
                            Logger.d("response is not 0json");
                            Logger.d(response);
                        }
                        mTextViewPostResultUTF8.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动
                        mTextViewPostResultUTF8.setText(Html.fromHtml(response));

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        boolean isMainUi = getIsMainUI();
                        Logger.e("isMainUi:" + isMainUi);
                        Logger.e("失败");
                        Logger.e(error.getMessage(), error);
                        Logger.e("");
                        mTextViewPostResultUTF8.setText(error.getMessage());
                    }
                })
            //需要在StringRequest的匿名类中重写getParams()方法，在这里设置POST参数就可以了
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("params1", "value1");
                map.put("params2", "value2");
                return map;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    private void getJsonDataVolleyGet() {

        /**
         * 1.创建一个RequestQueue对象
         */
        //RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);

        /**
         * 2.创建一个StringRequest对象。
         */
        /**
         * 第一个:目标服务器的URL地址，
         * 第二个参数为null的话，为Get请求
         * 第三个:服务器响应成功的回调，
         * 第四个:服务器响应失败的回调。
         */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                URL_JSON,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean isMainUi = getIsMainUI();
                        Logger.d("isMainUi:" + isMainUi);
                        Logger.d("成功");
                        if (response != null) {
                            Logger.json(response.toString());
                            mTextViewJsonResult.setText(StringUtil.formatString(response.toString()));
                        }else{
                            mTextViewJsonResult.setText("response == null");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        boolean isMainUi = getIsMainUI();
                        Logger.e("isMainUi:" + isMainUi);
                        Logger.e("失败");
                        Logger.e(error.getMessage(), error);
                        if (error!=null){
                            StringBuilder sb = new StringBuilder();

                            if (error.networkResponse!=null){
                                sb.append("error.networkResponse!=null"+"\n");
                                Map<String, String> headers = error.networkResponse.headers;
                                byte[] data = error.networkResponse.data;
                                int statusCode  = error.networkResponse.statusCode;
                                long networkTimeMs = error.networkResponse.networkTimeMs;

                                sb.append("statusCode:"+statusCode+"\n");
                                if (headers!=null){
                                    sb.append("headers:"+headers.toString()+"\n");
                                }
                                if (data!=null){
                                    sb.append("data:"+data.toString()+"\n");
                                }
                                sb.append("networkTimeMs:"+networkTimeMs+"\n");
                            }else{
                                sb.append("error.networkResponse==null"+"\n");
                            }
                            sb.append("error!=null"+"\n"+"errorMessage:"+error.getLocalizedMessage());
                            mTextViewJsonResult.setText(sb.toString());
                        }else{
                            mTextViewJsonResult.setText("error==null");
                        }
                    }
                });

        /**
         *  3.将StringRequest对象添加到RequestQueue里面
         */
        mRequestQueue.add(jsonObjectRequest);
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

        /**
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
                        boolean isMainUi = getIsMainUI();
                        Logger.d("isMainUi:" + isMainUi);
                        Logger.d("成功");
                        if (bitmap != null && !bitmap.isRecycled()) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                },
                DensityUtil.convertDipOrPx(getApplicationContext(),300),
                DensityUtil.convertDipOrPx(getApplicationContext(),300),
                ImageView.ScaleType.CENTER,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.mipmap.bili_default_avatar);
                        boolean isMainUi = getIsMainUI();
                        Logger.e("isMainUi:" + isMainUi);
                        Logger.e("失败");
                        Logger.e(error.getMessage(), error);

                        if (error!=null&&error.networkResponse!=null){
                            StringBuilder sb = new StringBuilder();
                            NetworkResponse networkResponse = error.networkResponse;
                            int statusCode = networkResponse.statusCode;
                            byte[] data = networkResponse.data;
                            Map<String, String> headers = networkResponse.headers;
                            boolean notModified = networkResponse.notModified;
                            long networkTimeMs = networkResponse.networkTimeMs;
                            Logger.e("statusCode:" + statusCode);
                            sb.append("Image load error:networkResponse "+"\n");
                            sb.append("statusCode:" + statusCode+"\n");
                            sb.append("notModified:" + notModified+"\n");
                            sb.append("networkTimeMs:" + networkTimeMs+"ms"+"\n");
                            if (data != null) {
                                Logger.e("data:" + data.toString());
                            } else {
                                Logger.e("data==null");
                            }

                            if (headers != null) {
                                String json = MapToJsonString.mapToJSONObject(headers);
                                Logger.json(json);
                                sb.append("header info:"+"\n"+StringUtil.formatString(json)+"\n");
                            } else {
                                Logger.e("headers == null");
                                sb.append("headers == null");
                            }

                            mTextViewGetResult.setText(sb.toString());
                            Logger.e("notModified:" + notModified);
                            Logger.e("networkTimeMs:" + networkTimeMs + "ms");
                        }
                    }
                }
        );

        mRequestQueue.add(imageRequest);
    }


    /**
     * 使用ImageLoader
     */
    private void loadImageViaVolleyImageLoader() {
        /**
         * 1. 创建一个RequestQueue对象。
         * 2. 创建一个ImageLoader对象。
         * 3. 获取一个ImageListener对象。
         * 4. 调用ImageLoader的get()方法加载网络上的图片。
         **/

        //1.创建一个RequestQueue对象。
        //RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);

        //2. 创建一个ImageLoader对象。
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());

        /**
         * 我们通过调用ImageLoader的getImageListener()方法能够获取到一个ImageListener对象，
         * getImageListener()方法接收三个参数
         *
         * 第一个参数指定用于显示图片的ImageView控件，
         * 第二个参数指定加载图片的过程中显示的图片，
         * 第三个参数指定加载图片失败的情况下显示的图片。
         */

        //3.获取一个ImageListener对象。
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(
                mImageViewTwo,
                R.mipmap.bili_default_avatar,
                R.mipmap.ic_launcher
        );
        int ww = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                100,
                getResources().getDisplayMetrics());

        //4.调用ImageLoader的get()方法加载网络上的图片。
        imageLoader.get(
                URL_IMAGE_TWO,
                imageListener,
                ww,
                ww,
                ImageView.ScaleType.CENTER_CROP);

    }


    /**
     * BitmapCache并实现了ImageCache接口
     */
    private class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mLruCache;

        public BitmapCache() {

            int maxSize = 10 * 1024 * 1024;
            mLruCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mLruCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mLruCache.put(url, bitmap);
        }
    }


    /**
     * networkImageView
     */
    private void getNetworkImageViewViaVolley() {

        /**
         * 1.创建一个RequestQueue对象
         * 2.创建一个ImageLoader对象
         * 3.在布局文件中添加一个NetworkImageView控件
         * 4.在代码中获取该控件的实例
         * 5.设置要加载的图片地址
         */

        //1.创建一个RequestQueue对象
        // RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);

        //2.创建一个ImageLoader对象
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());

        //3.在布局文件中添加一个NetworkImageView控件

        //4.在代码中获取该控件的实例
        //5.设置要加载的图片地址
        /**
         * setDefaultImageResId() 设置加载中显示的图片
         * setErrorImageResId() 加载失败时显示的图片
         * setImageUrl() 目标图片的URL地址
         */
        mNetworkImageView.setDefaultImageResId(R.mipmap.bili_default_avatar);
        mNetworkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        mNetworkImageView.setImageUrl(URL_IMAGE_THREE, imageLoader);

        /**
         * NetworkImageView并不需要提供任何设置最大宽高的方法也能够对加载的图片进行压缩。
         * 这是由于NetworkImageView是一个控件，在加载图片的时候它会自动获取自身的宽高，
         * 然后对比网络图片的宽度，再决定是否需要对图片进行压缩。也就是说，
         * 压缩过程是在内部完全自动化的，并不需要我们关心，
         * NetworkImageView会始终呈现给我们一张大小刚刚好的网络图片，不会多占用任何一点内存，
         * 这也是NetworkImageView最简单好用的一点吧。
         *
         * 当然了，如果你不想对图片进行压缩的话，其实也很简单，
         * 只需要在布局文件中把NetworkImageView的layout_width和layout_height都设置成wrap_content就可以了，
         * 这样NetworkImageView就会将该图片的原始大小展示出来，不会进行任何压缩。
         */
    }


    private boolean getIsMainUI() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private void getXML() {
        //RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);
        XMLRequest xmlRequest = new XMLRequest(
                URL_XML,
                new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        try {
                            int eventType = response.getEventType();
                            final StringBuilder builder = new StringBuilder();
                            String pName;
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        String nodeName = response.getName();
                                        if ("city".equals(nodeName)) {
                                            pName = response.getAttributeValue(0);
                                            if (!TextUtils.isEmpty(pName)) {
                                                Log.d("TAG", "pName is " + pName);
                                                builder.append(pName).append("\n");
                                            }
                                        }
                                        break;
                                }
                                eventType = response.next();
                            }

                            boolean isMainUi = getIsMainUI();
                            Logger.d("isMainUi:" + isMainUi);
                            Logger.d("成功");
                            if (response != null) {
                                mTextViewXML.setText(builder.toString());
                            }else{
                                mTextViewJsonResult.setText("response == null");
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
        mRequestQueue.add(xmlRequest);
    }


    private void getGsonData() {
        // RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);

        GsonRequest<Weather> gsonRequest = new GsonRequest<Weather>(
                URL_JSON,
                Weather.class,
                new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        Logger.d("成功");
                        WeatherInfo info = weather.weatherinfo;
                        String city = info.city;
                        String WD = info.WD;
                        String WS = info.WS;
                        StringBuilder sb = new StringBuilder();
                        sb.append(city).append(" ").append(WD).append(" ").append(WS);

                        mTextViewWeather.setText(sb.toString());
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.getMessage(), error);
                    }
                }

        );
        //请用缓存
        gsonRequest.setShouldCache(true);
        mRequestQueue.add(gsonRequest);
    }

}
