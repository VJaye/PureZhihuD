package io.github.laucherish.purezhihud.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import io.github.laucherish.purezhihud.R;
import io.github.laucherish.purezhihud.base.BaseActivity;
import io.github.laucherish.purezhihud.ui.fragment.NewsListFragment;

public class NewsListActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Fragment fragment = NewsListFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container,fragment,NewsListFragment.TAG);
        transaction.commit();
    }
}
