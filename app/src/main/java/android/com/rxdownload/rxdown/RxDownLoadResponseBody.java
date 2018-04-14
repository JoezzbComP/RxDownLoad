package android.com.rxdownload.rxdown;

import android.app.Activity;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * author : atex
 * e-mail : xxx@xx
 * time   : 2018/04/14
 * desc   :
 * version: 1.0
 */

public class RxDownLoadResponseBody extends ResponseBody{

    private Activity activity;
    private RxDownLoadListener rxDownLoadListener;
    private ResponseBody responseBody;

    public RxDownLoadResponseBody(Activity activity, RxDownLoadListener rxDownLoadListener, ResponseBody responseBody) {
        this.activity = activity;
        this.rxDownLoadListener = rxDownLoadListener;
        this.responseBody = responseBody;
    }

    private BufferedSource bufferedSource;


    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource==null){
            bufferedSource= Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source){
        return new ForwardingSource(source) {
            long totalBytes=0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long readBytes=super.read(sink,byteCount);
                totalBytes+=readBytes!=-1?readBytes:0;
                if (rxDownLoadListener!=null){
                    if (readBytes!=-1){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //回调下载的进度
                                //这里回传下载的进度信息（因为网络请求需要在io线程执行，所以该接口的onChange回调是再io线程中的，而非ui线程不能刷新界面，所以我们需要传入一个activity对象，
                                // 来进行线程调度，通过主线程回调。
                                // 这里需要注意的是：因为Activity是传进来的对象，如果当前下载的Activity对象被销毁，而可能导致崩溃或者内存泄漏）
                                //可扩展，添加activity对象的null判断（或者传入application对象）
                                rxDownLoadListener.onChange((int) (totalBytes*100/responseBody.contentLength()));
                            }
                        });
                    }
                }
                return readBytes;
            }
        };
    }
}
