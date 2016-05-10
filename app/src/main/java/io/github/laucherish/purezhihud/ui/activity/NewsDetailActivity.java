package io.github.laucherish.purezhihud.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.FrameLayout;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import io.github.laucherish.purezhihud.R;
import io.github.laucherish.purezhihud.base.BaseActivity;
import io.github.laucherish.purezhihud.bean.News;
import io.github.laucherish.purezhihud.ui.fragment.NewsDetailFragment;

/**
 * Created by laucherish on 16/3/17.
 */
public class NewsDetailActivity extends BaseActivity {
    @Bind(R.id.fl_container)
    FrameLayout mContainer;
    public static final String KEY_NEWS = "key_news";
    private Fragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    public static void start(Activity activity, News news, Bundle activityOptions) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtra(KEY_NEWS, news);
        ActivityCompat.startActivity(activity, intent, activityOptions);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mSwipeBackLayout.setEdgeDp(120); //设置滑动返回触发范围
        News news = getIntent().getParcelableExtra(KEY_NEWS);
        showNewsDetailFragment(news);
    }

    private void showNewsDetailFragment(News news) {
        fragment = NewsDetailFragment.newInstance(news);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, fragment, NewsDetailFragment.TAG);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
