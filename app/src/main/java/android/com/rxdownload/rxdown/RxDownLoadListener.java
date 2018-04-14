package android.com.rxdownload.rxdown;

/**
 * author : atex
 * e-mail : xxx@xx
 * time   : 2018/04/14
 * desc   :
 * version: 1.0
 */

public interface RxDownLoadListener {
    void onStart();
    void onChange(int ran);
    void onFail();
    void onFinish();
}
