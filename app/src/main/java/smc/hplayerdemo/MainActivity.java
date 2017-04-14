package smc.hplayerdemo;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import smc.hplayerdemo.gallery.GalleryViewActivity;
import smc.hplayerdemo.securitycodeview.SecurityCodeActivity;
import smc.hplayerdemo.view.HVideoPlayer;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_AUTO_COMPLETE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_ERROR;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_NORMAL;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PAUSE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;

public class MainActivity extends BaseActivity implements View.OnClickListener{
     private  String url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/39/362/39362_fq.m3u8?" +
             "sec=1d2e7da363e1ed7b40aec38c9d8d11ce&portalId=64&contentType=1&pid=39362&nettype=wifi&uac=android&rid=null";
    private HVideoPlayer hvideoPlayer;
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;//加入重力感应
    private SensorManager sensorManager;
    //全屏下播放器对象
    public HVideoPlayer mFullScreenPlayer;
   String source2 ="http://vod.gslb.cmvideo.cn/zhengshi/2207/140/000/2207140000/media/FILENAME_54.mp4.m3u8?msisdn=b2c0cc81-560b-3a9e-b789-4ea7e431c593&sid=2207140000&timestamp=20170412100004&Channel_ID=316900000000001&preview=1&playseek=000000-001000&ProgramID=622401892&encrypt=898eb1b4c1f1edd335d9ba3c23f3ded7";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hvideoPlayer = (HVideoPlayer) findViewById(R.id.custom_videoplayer_standard);
        int height=MainActivity.this.getResources().getDisplayMetrics().heightPixels;
        RelativeLayout.LayoutParams lp =(RelativeLayout.LayoutParams) hvideoPlayer.getLayoutParams();
        lp.height=height/3;
        hvideoPlayer.setLayoutParams(lp);
        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
        Button checkUrl_btn = (Button) findViewById(R.id.checkUrl_btn);
        checkUrl_btn.setOnClickListener(this);
        Button secode_btn = (Button) findViewById(R.id.secode_btn);
        secode_btn.setOnClickListener(this);
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
        url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/42/2/42002_fq.m3u8?sec=37a916a807fb044edb2948d8deb8b8cb&portalId=64&contentType=1&pid=42002&nettype=wifi&uac=android&rid=null";

        hvideoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");
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
                    Toast.makeText(MainActivity.this, "播放失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    int i=0;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.start:
                if (hvideoPlayer.currentState == CURRENT_STATE_NORMAL || hvideoPlayer.currentState == CURRENT_STATE_ERROR) {//错误状态
                        hvideoPlayer.prepareVideo();
                    hvideoPlayer.onEvent(  hvideoPlayer.currentState != CURRENT_STATE_ERROR ? JCUserAction.ON_CLICK_START_ICON : JCUserAction.ON_CLICK_START_ERROR);
                } else if (  hvideoPlayer.currentState == CURRENT_STATE_PLAYING) {//播放状态
                    hvideoPlayer. onEvent(JCUserAction.ON_CLICK_PAUSE);
                    JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(false);
                    hvideoPlayer.setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
                } else if (  hvideoPlayer.currentState == CURRENT_STATE_PAUSE) {
                    hvideoPlayer.onEvent(JCUserAction.ON_CLICK_RESUME);
                    JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(true);
                    hvideoPlayer.setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
                } else if (  hvideoPlayer.currentState == CURRENT_STATE_AUTO_COMPLETE) {//播放完成后再次点击开始按钮会调用
                    JCVideoPlayer.releaseAllVideos();
                    hvideoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");
                    hvideoPlayer. onEvent(JCUserAction.ON_CLICK_START_AUTO_COMPLETE);
                }
                break;
            case  R.id.btn:
                startActivity(new Intent(MainActivity.this, GalleryViewActivity.class));
//                startActivity(new Intent(MainActivity.this, FirstActivity.class));
                break;
            case  R.id.secode_btn:
                startActivity(new Intent(MainActivity.this, SecurityCodeActivity.class));
//                startActivity(new Intent(MainActivity.this, FirstActivity.class));
                break;
            case  R.id.checkUrl_btn:
                JCVideoPlayer.releaseAllVideos();
 //               String url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/40/934/40934_fq.m3u8?sec=6bc7b73a3e0ce5c744ed738d8288dd99&portalId=64&contentType=1&pid=40934&nettype=wifi&uac=android&rid=null"
//                 url= "http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/38/970/38970_fq.m3u8?sec=b4f3468399ed2c0a31a9506ee26b1d92&portalId=64&contentType=1&pid=38970&nettype=wifi&uac=android&rid=null";
//                  url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/39/362/39362_fq.m3u8?sec=1d2e7da363e1ed7b40aec38c9d8d11ce&portalId=64&contentType=1&pid=39362&nettype=wifi&uac=android&rid=null";
                if (hvideoPlayer!=null){
                    if (i==3){
                        url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/40/282/40282_fq.m3u8?sec=ea208f51a0a7c4017c7b81669cde1f50&portalId=64&contentType=1&pid=40282&nettype=wifi&uac=android&rid=null";
                    }
                    if (i==1){
                        url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/40/934/40934_fq.m3u8?sec=6bc7b73a3e0ce5c744ed738d8288dd99&portalId=64&contentType=1&pid=40934&nettype=wifi&uac=android&rid=null";
                    }
                    if (i==2){
                        url= "http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/38/970/38970_fq.m3u8?sec=b4f3468399ed2c0a31a9506ee26b1d92&portalId=64&contentType=1&pid=38970&nettype=wifi&uac=android&rid=null";
                    }else{
                        url="http://egdtv-pic.oss-cn-shenzhen.aliyuncs.com/m6/0/0/39/362/39362_fq.m3u8?sec=1d2e7da363e1ed7b40aec38c9d8d11ce&portalId=64&contentType=1&pid=39362&nettype=wifi&uac=android&rid=null";
                    }
                }
                i++;
                if (i==5){
                    i=0;
                }
                hvideoPlayer.getThumUrl("http://p.qpic.cn/videoyun/0/2449_ded7b566b37911e5942f0b208e48548d_2/640");
                hvideoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");
                break;
        }

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
}
