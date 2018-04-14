package android.com.rxdownload;

import android.app.Activity;
import android.com.rxdownload.rxdown.RestApi;
import android.com.rxdownload.rxdown.RxDownLoadInterceptor;
import android.com.rxdownload.rxdown.RxDownLoadListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * author : atex
 * e-mail : xxx@xx
 * time   : 2018/04/14
 * desc   :
 * version: 1.0
 */

public class RxDownLoadResetData {
    private RestApi restApi;
    static RxDownLoadResetData rxDownLoadResetData;
    private Retrofit retrofit;

    private Retrofit.Builder builder;
    public static RxDownLoadResetData getInstance(){
        if (rxDownLoadResetData==null){
            rxDownLoadResetData=new RxDownLoadResetData();
        }
        return rxDownLoadResetData;
    }

    OkHttpClient.Builder okBuilder;
    public RxDownLoadResetData() {
        builder=new Retrofit.Builder()
                .baseUrl("https://github.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        okBuilder=new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(30000L, TimeUnit.SECONDS);
    }


    //下载

    /**
     *
     * @param url
     * @param activity
     * @param rxDownLoadListener 将下载的回调类传给 自己自定义的拦截器 RxDownLoadInterceptor
     * @return
     */
    public Observable<ResponseBody> downLoad(String url, Activity activity,RxDownLoadListener rxDownLoadListener){

          OkHttpClient okHttpClient=
                  okBuilder.addInterceptor(new RxDownLoadInterceptor(activity,rxDownLoadListener))
                .build();
        retrofit=builder.client(okHttpClient).build();
        restApi=retrofit.create(RestApi.class);
        rxDownLoadListener.onStart();
        return restApi.downLoad(url);
    }

}
