package io.github.laucherish.purezhihud.network.manager;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import io.github.laucherish.purezhihud.bean.NewsDetail;
import io.github.laucherish.purezhihud.bean.NewsList;
import io.github.laucherish.purezhihud.network.service.ZhihuService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by laucherish on 16/3/15.
 */
public class RetrofitManager {

    public static final String BASE_ZHIHU_URL = "http://news-at.zhihu.com/api/4/";
    private static OkHttpClient mOkHttpClient;
    private final ZhihuService mZhihuService;

    public static RetrofitManager builder(){
        return new RetrofitManager();
    }

    private RetrofitManager() {

        initOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_ZHIHU_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mZhihuService = retrofit.create(ZhihuService.class);
    }

    private void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public Observable<NewsList> getLatestNews(){
        return mZhihuService.getLatestNews();
    }

    public Observable<NewsList> getBeforeNews(String date){
        return mZhihuService.getBeforeNews(date);
    }

    public Observable<NewsDetail> getNewsDetail(int id) {
        return mZhihuService.getNewsDetail(id);
    }

}
