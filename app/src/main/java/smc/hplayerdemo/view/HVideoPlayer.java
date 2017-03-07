package smc.hplayerdemo.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import smc.hplayerdemo.R;

/**
 * Created by Administrator on 2017/2/20.
 */

public class HVideoPlayer extends JCVideoPlayerStandard {

    public HVideoPlayer(Context context) {
        super(context);
    }

    public HVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.custom_video_player;
    }

    /**
     * 重写父类方法，防止自动隐藏播放器工具栏。
     * 如需要自动隐藏请删除此方法或调用super.startDismissControlViewTimer();
     */
    @Override
    public void startDismissControlViewTimer() {
        super.startDismissControlViewTimer();
    }


    /**
     * //重写onError 视频播放错误的时候调用
     *
     * @param what
     * @param extra
     */
    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }
    public void getThumUrl(String thumUrl) {
//        thumbImageView
        ImageLoader.getInstance().displayImage(thumUrl,  thumbImageView);

    }


    /**
     * JieCaoVideoPlayer 是通过 setUp方法 来初始化播放器参数
     *
     * @param url
     * @param screen
     * @param objects
     */
    private OnFullScreenListener mOnFullScreenListener;

    @Override
    public void setUp(String url, int screen, Object... objects) {
        //强制全屏
        FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        if (objects.length != 1) {
            mOnFullScreenListener = (OnFullScreenListener) objects[1];
        }
        super.setUp(url, screen,  objects[0], mOnFullScreenListener);//单个必须这样设置
        //点击返回按钮隐藏弹幕
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });

        //重写全屏按钮点击事件
        fullscreenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
                if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                    backPress();
                } else {
                    //全屏
                    startWindowFullscreen();

                }
            }
        });
//        startButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR) {//错误状态
//                Log.e("信息",currentState+"---111");//开始
//                    prepareVideo();
//                    if ( currentState == CURRENT_STATE_ERROR){
//                    }
//                    onEvent(currentState != CURRENT_STATE_ERROR ? JCUserAction.ON_CLICK_START_ICON : JCUserAction.ON_CLICK_START_ERROR);
//                } else if (currentState == CURRENT_STATE_PLAYING) {//播放状态
//                    Log.e("信息",currentState+"---222");//暂停
//                    onEvent(JCUserAction.ON_CLICK_PAUSE);
//                    JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(false);
//                    setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
//                } else if (currentState == CURRENT_STATE_PAUSE) {
//                    Log.e("信息",currentState+"---333");//暂停后再次播放 拉动播放
//                    onEvent(JCUserAction.ON_CLICK_RESUME);
//                    JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(true);
//                    setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
//                } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
//                    Log.e("信息",currentState+"---444");
//                    onEvent(JCUserAction.ON_CLICK_START_AUTO_COMPLETE);
//                    prepareVideo();
//                }
//            }
//        });

    }
    /**
     * 判断当前是否是全屏
     *
     * @return
     */
    public boolean isFullScreen() {
        return currentScreen == SCREEN_WINDOW_FULLSCREEN?true:false;
    }

    //全屏事件监听，返回全屏播放器对象
    public void setOnFullScreenListener(OnFullScreenListener onFullScreenListener) {
        this.mOnFullScreenListener = onFullScreenListener;
    }
    /**
     * 全屏回调
     */
    public interface OnFullScreenListener {
        void onFullScreen(HVideoPlayer hVideoPlayer);
    }
//    private  CurrentStateListener currentStateListener;
//    public void getCurrentStateListener(CurrentStateListener currentStateListener) {
//        this.currentStateListener = currentStateListener;
//    }
//    public interface CurrentStateListener{
//        void currentState(int status);
//    }

}