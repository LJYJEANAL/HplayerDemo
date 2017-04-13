package smc.hplayerdemo;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_AUTO_COMPLETE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_ERROR;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_NORMAL;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PAUSE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;

public class StandActivity extends AppCompatActivity implements View.OnClickListener {
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;//加入重力感应
    private SensorManager sensorManager;
    private JCVideoPlayerStandard jcPlayer;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        jcPlayer = (JCVideoPlayerStandard) findViewById(R.id.jcPlayer);
        url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                jcPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "1224");
                if (jcPlayer.currentState == CURRENT_STATE_NORMAL || jcPlayer.currentState == CURRENT_STATE_ERROR) {
                    jcPlayer.prepareVideo();
                    jcPlayer.onEvent(jcPlayer.currentState != CURRENT_STATE_ERROR ? JCUserAction.ON_CLICK_START_ICON : JCUserAction.ON_CLICK_START_ERROR);
                } else if (jcPlayer.currentState == CURRENT_STATE_PLAYING) {
                    jcPlayer.onEvent(JCUserAction.ON_CLICK_PAUSE);
                    JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(false);
                    jcPlayer.setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
                } else if (jcPlayer.currentState == CURRENT_STATE_PAUSE) {
                    jcPlayer.onEvent(JCUserAction.ON_CLICK_RESUME);
                    JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(true);
                    jcPlayer.setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
                } else if (jcPlayer.currentState == CURRENT_STATE_AUTO_COMPLETE) {
                    jcPlayer.onEvent(JCUserAction.ON_CLICK_START_AUTO_COMPLETE);
                    jcPlayer.prepareVideo();
                }
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
