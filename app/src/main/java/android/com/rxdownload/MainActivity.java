package android.com.rxdownload;

import android.Manifest;
import android.com.rxdownload.rxdown.FileUtils;
import android.com.rxdownload.rxdown.RxDownLoadListener;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Button btn_Download;
    TextView tv_Pro;

    private RxDownLoadResetData rxDownLoadResetData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxDownLoadResetData=RxDownLoadResetData.getInstance();
        tv_Pro=findViewById(R.id.tv_Pro);
        btn_Download=findViewById(R.id.btn_Download);
        //6.0权限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        btn_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down("https://www.imooc.com/mobile/mukewang.apk",rxDownLoadListener);
            }
        });
    }

    /**
     *
     * @param url
     * @param rxDownLoadListener  传入下载的回调
     */
    public void down(String url, final RxDownLoadListener rxDownLoadListener){
        rxDownLoadResetData.downLoad(url,MainActivity.this,rxDownLoadListener)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<ResponseBody, Boolean>() {
                    @Override
                    public Boolean call(ResponseBody responseBody) {

                        //到这里就已经下载成功了
                        return FileUtils.writeFile2Disk( responseBody, "imooc");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean bol) {
                        if (bol.booleanValue()){
                            rxDownLoadListener.onFinish();
                        }else{
                            rxDownLoadListener.onFail();
                        }
                    }
                });

    }

    private RxDownLoadListener rxDownLoadListener=new RxDownLoadListener() {
        @Override
        public void onStart() {

            Toast.makeText(MainActivity.this,"下载开始",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChange(int ran) {
            tv_Pro.setText(ran+"%");
        }

        @Override
        public void onFail() {
            Toast.makeText(MainActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish() {
            Toast.makeText(MainActivity.this,"下载结束",Toast.LENGTH_SHORT).show();
        }
    };
}
