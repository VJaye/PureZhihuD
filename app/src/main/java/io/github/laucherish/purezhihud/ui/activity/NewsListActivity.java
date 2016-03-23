package io.github.laucherish.purezhihud.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
        mSwipeBackLayout.setEdgeDp(20);
        Fragment fragment = NewsListFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragment, NewsListFragment.TAG);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
