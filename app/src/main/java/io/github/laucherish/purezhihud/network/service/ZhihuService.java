package io.github.laucherish.purezhihud.network.service;

import io.github.laucherish.purezhihud.bean.NewsDetail;
import io.github.laucherish.purezhihud.bean.NewsList;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by laucherish on 16/3/15.
 */
public interface ZhihuService {

    @GET("news/latest")
    Observable<NewsList> getLatestNews();

    @GET("news/before/{date}")
    Observable<NewsList> getBeforeNews(@Path("date") String date);

    @GET("news/{id}")
    Observable<NewsDetail> getNewsDetail(@Path("id") int id);
}
