package com.malin.volley.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.malin.volley.R;
import com.malin.volley.fragment.GetJsonRequestUFT8Fragment;
import com.malin.volley.fragment.ImageRequestFragment;
import com.malin.volley.fragment.StringRequestGetFragment;
import com.malin.volley.fragment.StringRequestGetUFT8Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malin on 16-5-30.
 */
public class HomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Context mContext;
    private TextView mTitle;
    private Typeface mTypeface;
    private static final String FRONT_LIGHT_PATH = "fonts/Lato-light.ttf";
    private int COLOR_WHITE;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        initView();
        initValue();
        initToolBar();
        initListener();
        initViewPager();
        initTabLayout();

    }


    private void initValue() {
        mContext = getApplicationContext();
        COLOR_WHITE = ContextCompat.getColor(mContext,android.R.color.white);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void initToolBar() {
        mToolbar.setTitle("Volley");
        mToolbar.setTitleTextColor(COLOR_WHITE);
        mToolbar.setSubtitle("");
        mToolbar.setSubtitleTextColor(COLOR_WHITE);
        mToolbar.setNavigationIcon(R.mipmap.bili_default_avatar);
        mToolbar.setLogo(null);
        mToolbar.setTitleTextAppearance(mContext, R.style.ActionBarTitle);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);


        new Thread(new Runnable() {
            @Override
            public void run() {
                mTypeface = Typeface.createFromAsset(mContext.getAssets(), FRONT_LIGHT_PATH);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTitle.setText("Sample");
                        mTitle.setTextSize(16);
                        mTitle.setTextColor(COLOR_WHITE);
                        mTitle.setTypeface(mTypeface, Typeface.BOLD);
                    }
                });
            }
        }).start();

        setSupportActionBar(mToolbar);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "navigation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_action_menu_game: {
                Toast.makeText(HomeActivity.this, "1", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.toolbar_action_menu_search: {
                Toast.makeText(HomeActivity.this, "2", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.toolbar_action_menu_download: {
                Toast.makeText(HomeActivity.this, "3", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViewPager() {
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StringRequestGetFragment(), "直播");
        adapter.addFragment(new StringRequestGetUFT8Fragment(), "推荐");
        adapter.addFragment(new GetJsonRequestUFT8Fragment(), "番剧");
        adapter.addFragment(new ImageRequestFragment(), "分区");
//        adapter.addFragment(new StringRequestGetFragment(), "关注");
//        adapter.addFragment(new StringRequestGetFragment(), "发现");
        viewPager.setAdapter(adapter);
    }

    static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    private void initTabLayout() {
        if (mTabLayout != null && mViewPager != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }


}
