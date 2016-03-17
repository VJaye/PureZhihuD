package io.github.laucherish.purezhihud;

import android.app.Application;

import io.github.laucherish.purezhihud.utils.L;

/**
 * Created by laucherish on 16/3/17.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        L.init();
    }
}
