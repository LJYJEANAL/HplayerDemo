package smc.hplayerdemo;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;


public class VFActivity extends BaseActivity implements GestureDetector.OnGestureListener {

    private VelocityTracker velocityTracker;
    private GestureDetector mGestureDetetor;
    private Button toGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vf);
        ViewFlipper mFlipper = ((ViewFlipper) this.findViewById(R.id.flipper));
        mFlipper.startFlipping();

        // 设置进入动画
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
        // 设置滚出动画
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
        mGestureDetetor = new GestureDetector(VFActivity.this);
        mGestureDetetor.setIsLongpressEnabled(false);//解决长按屏幕后无法拖动现象
        toGallery = (Button) findViewById(R.id.toGallery);

        toGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(VFActivity.this,GalleryActivity.class));
//                toGallery.setTranslationX(100);
//        toGallery.setTranslationY(50);
//        ObjectAnimator.ofFloat(toGallery,"translationY",0,200).setDuration(500).start();//1s内平移200个像素
                final int startxX = 0;
                final int deltaX = 150;
                final ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = animator.getAnimatedFraction();
                        toGallery.setTranslationX(startxX + (int) (deltaX * fraction));
                    }
                });
                animator.start();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("信息","event.getX()"+event.getX()+"--event.getY()"+event.getY()+"--event.getRawX()"+event.getRawX()+
//                "--event.getRawY()"+event.getRawY());
//        velocityTracker = VelocityTracker.obtain();
//        velocityTracker.addMovement(event);
//        velocityTracker.computeCurrentVelocity(1000);
//        int xvelocityTracker=(int)velocityTracker.getXVelocity();
//        int yvelocityTracker=(int)velocityTracker.getYVelocity();
//        Log.e("信息",xvelocityTracker+"----"+yvelocityTracker);
//
        return mGestureDetetor.onTouchEvent(event);
    }

    /**
     * @param e
     * @return
     */
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    /**
     * @param e
     */
    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
