package smc.hplayerdemo.downloader;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import smc.hplayerdemo.BaseActivity;
import smc.hplayerdemo.R;

import static smc.hplayerdemo.R.id.continue_button;
import static smc.hplayerdemo.R.id.downloadButton;
import static smc.hplayerdemo.R.id.pause_button;

public class DowloaderActivity extends BaseActivity implements ProgressResponseBody.ProgressListener,View.OnClickListener{
    public static final String TAG = "信息";
    public static final String PACKAGE_URL = "http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk";
    ProgressBar progressBar;
    private long breakPoints;
    private ProgressDownloader downloader;
    private File file;
    private long totalBytes;
    private long contentLength;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dowloader);
        progressBar=   (ProgressBar) findViewById(R.id.progressBar);
        Button    downloadButton=   (Button) findViewById(R.id.downloadButton);
        Button  pause_button=   (Button) findViewById(R.id.pause_button);
        Button continue_button=   (Button) findViewById(R.id.continue_button);
        downloadButton.setOnClickListener(this);
        pause_button.setOnClickListener(this);
        continue_button.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case downloadButton:
                // 新下载前清空断点信息
                breakPoints = 0L;
//                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ".apk");
                file=new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "dow", "gdzbt.apk");
                downloader = new ProgressDownloader(PACKAGE_URL, file, this);
                Toast.makeText(this, "下载", Toast.LENGTH_SHORT).show();
                downloader.download(0L);
                break;
            case pause_button:
                downloader.pause();
                Toast.makeText(this, "下载暂停", Toast.LENGTH_SHORT).show();
                // 存储此时的totalBytes，即断点位置。
                breakPoints = totalBytes;
                break;
            case continue_button:
                downloader.download(breakPoints);
                break;
        }
    }

    @Override
    public void onPreExecute(long contentLength) {
        // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
        if (this.contentLength == 0L) {
            this.contentLength = contentLength;
            progressBar.setMax((int) (contentLength / 1024));
        }
    }

    @Override
    public void update(long totalBytes, boolean done) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints;
        progressBar.setProgress((int) (totalBytes + breakPoints) / 1024);
        if (done) {
            // 切换到主线程
            Observable
                    .empty()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnCompleted(new Action0() {
                        @Override
                        public void call() {
                            Toast.makeText(DowloaderActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribe();
        }
    }
}
