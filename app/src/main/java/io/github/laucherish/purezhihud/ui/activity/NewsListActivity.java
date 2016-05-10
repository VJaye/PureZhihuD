package io.github.laucherish.purezhihud.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import io.github.laucherish.purezhihud.R;
import io.github.laucherish.purezhihud.base.BaseActivity;
import io.github.laucherish.purezhihud.base.Constant;
import io.github.laucherish.purezhihud.ui.adapter.NewsListAdapter;
import io.github.laucherish.purezhihud.ui.fragment.NewsListFragment;
import io.github.laucherish.purezhihud.utils.PrefUtil;

public class NewsListActivity extends BaseActivity implements View.OnTouchListener {

    @Bind(R.id.fl_main)
    ViewGroup mViewGroup;
    @Bind(R.id.iv_main)
    ImageView mIvMain;
    @Bind(R.id.menu_me)
    ImageView mMenu_me;
    @Bind(R.id.fragment_sidebar)
    RelativeLayout mSide;
    @Bind(R.id.menu_me_tv)

    private final long ANIMTION_TIME = 1000;
    private NewsListFragment mFragment;
    private Spring spring;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        addFragment(0, 0, null, null);
        setSwipeBackEnable(false);
    }

    private void addFragment(int position, int scroll, NewsListAdapter adapter, String curDate) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mFragment != null) {
            transaction.remove(mFragment);
        }
        mFragment = NewsListFragment.newInstance(position, scroll, adapter, curDate);
        mFragment.setmOnRecyclerViewCreated(new onViewCreatedListener());
        transaction.replace(R.id.fl_container, mFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_action_about:
                AboutActivity.start(this);
                return true;

            case R.id.menu_action_daynight:
                boolean isNight = PrefUtil.isNight();
                if (isNight) {
                    PrefUtil.setDay();
                    setTheme(Constant.RESOURCES_DAYTHEME);
                } else {
                    PrefUtil.setNight();
                    setTheme(Constant.RESOURCES_NIGHTTHEME);
                }
                setDrawableCahe();
                getState();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
                mMenu_me.setScaleX(mappedValue);
                mMenu_me.setScaleY(mappedValue);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void setDrawableCahe() {
        //设置false清除缓存
        mViewGroup.setDrawingCacheEnabled(false);
        //设置true之后可以获取Bitmap
        mViewGroup.setDrawingCacheEnabled(true);
        mIvMain.setImageBitmap(mViewGroup.getDrawingCache());
        mIvMain.setAlpha(1f);
        mIvMain.setVisibility(View.VISIBLE);
    }

    public void getState() {
        RecyclerView recyclerView = mFragment.getRecyclerView();
        recyclerView.stopScroll();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int position = layoutManager.findFirstVisibleItemPosition();
            int scroll = recyclerView.getChildAt(0).getTop();
            addFragment(position, scroll, mFragment.getmNewsListAdapter(), mFragment.getCurDate());
        }
    }

    private void startAnimation(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f).setDuration(ANIMTION_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float n = (float) animation.getAnimatedValue();
                view.setAlpha(1f - n);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIvMain.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // When pressed start solving the spring to 1.
                spring.setEndValue(1);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // When released start solving the spring to 0.
                spring.setEndValue(0);
                break;
        }
        return true;
    }

    class onViewCreatedListener implements NewsListFragment.OnRecyclerViewCreated {

        @Override
        public void recyclerViewCreated() {
            startAnimation(mIvMain);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpringSystem springSystem = SpringSystem.create();
        spring = springSystem.createSpring();
// Set the spring in motion; moving from 0 to 1
//        spring.setEndValue(1);
        mMenu_me.setOnTouchListener(this);
    }
}
