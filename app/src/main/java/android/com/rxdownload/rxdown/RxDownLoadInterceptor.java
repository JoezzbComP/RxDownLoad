package android.com.rxdownload.rxdown;

import android.app.Activity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * author : atex
 * e-mail : xxx@xx
 * time   : 2018/04/14
 * desc   :
 * version: 1.0
 */

public class RxDownLoadInterceptor implements Interceptor {

    private Activity activity;
    private RxDownLoadListener rxDownLoadListener;

    public RxDownLoadInterceptor(Activity activity, RxDownLoadListener rxDownLoadListener) {
        this.activity = activity;
        this.rxDownLoadListener = rxDownLoadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response=chain.proceed(chain.request());
        //将下载的信息回调 传给自己自定义接收的RxDownLoadResponseBody  类，我们可以在RxDownLoadResponseBody类中计算获取到已经下载的大小和总大小进行计算，
        return response.newBuilder().body(new RxDownLoadResponseBody(activity,rxDownLoadListener,response.body())).build();
    }
}
