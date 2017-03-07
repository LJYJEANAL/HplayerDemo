package smc.hplayerdemo.down;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import smc.hplayerdemo.BaseActivity;
import smc.hplayerdemo.ExpandablelistActivity;
import smc.hplayerdemo.R;


public class DownActivity extends BaseActivity implements View.OnClickListener {
    private TextView down, progress, file_name;
    private ProgressBar pb_update;
    private DownloadManager dowloadManager;
    private String downloadUrl = "http://img2.3gtv.net/pic/2017217/1487296957822-1193959466.apk";
    private DownloadManager.Request request;
    long id;
    TimerTask task;
    Timer timer;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int pro = bundle.getInt("pro");
            String name = bundle.getString("name");
            pb_update.setProgress(pro);
            progress.setText(String.valueOf(pro) + "%");
            android.util.Log.e("信息",pro+"*****");
            file_name.setText(name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        Button toBtn = (Button) findViewById(R.id.toBtn);
        toBtn.setOnClickListener(this);
        down = (TextView) findViewById(R.id.down);
        progress = (TextView) findViewById(R.id.progress);
        file_name = (TextView) findViewById(R.id.file_name);
        pb_update = (ProgressBar) findViewById(R.id.pb_update);

        down.setOnClickListener(this);
        dowloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle("456");
        //指定在wifi状态下执行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        request.setMimeType("application/vnd.android.package-archive");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //创建目录
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
        //设置文件存放目录文件存放在外部存储的download文件夹中
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-release.apk");
        pb_update.setMax(100);
        final DownloadManager.Query query = new DownloadManager.Query();
        /**
         * 取消下载
         *   dowloadManager.remove();
         */
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Cursor cursor = dowloadManager.query(query.setFilterById(id));
                if (cursor != null && cursor.moveToFirst()) {
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        pb_update.setMax(100);
                        install(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/app-release.apk");
                        task.cancel();
                    }
                    String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    int pro = (bytes_downloaded * 100) / bytes_total;
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("pro", pro);
                    bundle.putString("name", title);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
                cursor.close();
            }
        };
        timer.schedule(task, 0, 1000);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.down:
                //每下载一个文件对应得id，通过此id 可以查询数据
                id = dowloadManager.enqueue(request);
                task.run();
                down.setClickable(false);
                break;
            case R.id.toBtn:
                startActivity(new Intent(DownActivity.this, ExpandablelistActivity.class));
                break;
        }
    }

    private void install(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
