package android.com.rxdownload.rxdown;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * author : atex
 * e-mail : xxx@xx
 * time   : 2018/04/14
 * desc   :
 * version: 1.0
 */

public interface RestApi {


    @Streaming
    @GET
     Observable<ResponseBody> downLoad(@Url String url);
}
