package smc.hplayerdemo;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import smc.hplayerdemo.down.DownActivity;
import smc.hplayerdemo.view.HVideoPlayer;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_AUTO_COMPLETE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_ERROR;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_NORMAL;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PAUSE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;

public class MainActivity extends BaseActivity implements View.OnClickListener{
     private  String Url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/39/362/39362_fq.m3u8?" +
             "sec=1d2e7da363e1ed7b40aec38c9d8d11ce&portalId=64&contentType=1&pid=39362&nettype=wifi&uac=android&rid=null";
//   "http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/38/970/38970_fq.m3u8?" +
//            "sec=b4f3468399ed2c0a31a9506ee26b1d92&portalId=64&contentType=1&pid=38970&nettype=wifi&uac=android&rid=null";
    private HVideoPlayer hvideoPlayer;
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;//加入重力感应
    private SensorManager sensorManager;
    //全屏下播放器对象
    public HVideoPlayer mFullScreenPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hvideoPlayer = (HVideoPlayer) findViewById(R.id.custom_videoplayer_standard);
        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
        ScrollView mScrollView = (ScrollView)findViewById(R.id.sv);
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.setHorizontalScrollBarEnabled(false);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        //ImageLoader初始化
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(this).build());
        hvideoPlayer.setOnFullScreenListener(onFullScreenListener);
        hvideoPlayer.setJcUserAction(jcUserAction);
        hvideoPlayer.titleTextView.setText("DEMO66");
        hvideoPlayer.startButton.setOnClickListener(this);
        //设置海报传入海报rul
        hvideoPlayer.getThumUrl("http://p.qpic.cn/videoyun/0/2449_ded7b566b37911e5942f0b208e48548d_2/640");
        //传入播放源
        String url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/40/934/40934_fq.m3u8?sec=6bc7b73a3e0ce5c744ed738d8288dd99&portalId=64&contentType=1&pid=40934&nettype=wifi&uac=android&rid=null";
        hvideoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,
                "房间名称");
    }
    private  HVideoPlayer.OnFullScreenListener onFullScreenListener= new HVideoPlayer.OnFullScreenListener() {
        @Override
        public void onFullScreen(HVideoPlayer hVideoPlayer) {
            mFullScreenPlayer = hVideoPlayer;
        }
    };
    private JCUserAction jcUserAction=new JCUserAction() {
        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                //开始播放
                case CURRENT_STATE_PLAYING:
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                    break;
                //暂停播放
                case CURRENT_STATE_PAUSE:
                    Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                    break;
                case CURRENT_STATE_AUTO_COMPLETE://播放完成
                    Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();
                    break;
                case CURRENT_STATE_ERROR://播放失败
                    hvideoPlayer.prepareVideo();
                    Toast.makeText(MainActivity.this, "播放s失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.start:
                if (hvideoPlayer.currentState == CURRENT_STATE_NORMAL || hvideoPlayer.currentState == CURRENT_STATE_ERROR) {//错误状态
                Log.e("信息",  hvideoPlayer.currentState+"---111");//开始
                        hvideoPlayer.prepareVideo();
                    hvideoPlayer.onEvent(  hvideoPlayer.currentState != CURRENT_STATE_ERROR ? JCUserAction.ON_CLICK_START_ICON : JCUserAction.ON_CLICK_START_ERROR);
                } else if (  hvideoPlayer.currentState == CURRENT_STATE_PLAYING) {//播放状态
                    Log.e("信息",  hvideoPlayer.currentState+"---222");//暂停
                    hvideoPlayer. onEvent(JCUserAction.ON_CLICK_PAUSE);
                    JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(false);
                    hvideoPlayer.setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
                } else if (  hvideoPlayer.currentState == CURRENT_STATE_PAUSE) {
                    Log.e("信息",  hvideoPlayer.currentState+"---333");//暂停后再次播放 拉动播放
                    hvideoPlayer.onEvent(JCUserAction.ON_CLICK_RESUME);
                    JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(true);
                    hvideoPlayer.setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
                } else if (  hvideoPlayer.currentState == CURRENT_STATE_AUTO_COMPLETE) {//播放完成后再次点击开始按钮会调用
                    Log.e("信息",  hvideoPlayer.currentState+"---444");
                    mills=0;
                    hvideoPlayer. onEvent(JCUserAction.ON_CLICK_START_AUTO_COMPLETE);
                }
                break;
            case  R.id.btn:
                startActivity(new Intent(MainActivity.this, DownActivity.class));
                break;
        }

    }
    /**
     * @param time
     *            倒数时间（单位：秒）
     */
    private CountDownTimer timer;
    long mills;
    private void countDownTimer() {

        timer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Toast.makeText(MainActivity.this, (millisUntilFinished / 1000) + "秒", Toast.LENGTH_SHORT).show();
//                obtain_btn.setText((millisUntilFinished / 1000) + "秒");
                if (millisUntilFinished/1000==1){
                    mills=1;

                }
            }

            @Override
            public void onFinish() {
            }
        };
        timer.start();
    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
