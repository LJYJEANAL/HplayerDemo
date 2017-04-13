package smc.hplayerdemo.gallery;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import smc.hplayerdemo.R;

public class GalleryViewActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    /**
     * 自动轮播设置
     */
    public ImageHandler handler = new ImageHandler(new WeakReference<GalleryViewActivity>(this));
    public ViewPager mViewPager;
    private RelativeLayout mViewPagerContainer;
    private static int TOTAL_COUNT = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerContainer = (RelativeLayout) findViewById(R.id.viewPagerContainer);
        initViewPager();
    }
    ImageView imageview;
    private void initViewPager() {
        //设置ViewPager的布局 图片以16:9的格式
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels * 16 / 25,
                getResources().getDisplayMetrics().heightPixels * 9 / 25);
        /**** 重要部分  ******/
        //clipChild用来定义他的子控件是否要在他应有的边界内进行绘制。 默认情况下，clipChild被设置为true。 也就是不允许进行扩展绘制。
        mViewPager.setClipChildren(false);
        //父容器一定要设置这个，否则看不出效果
        mViewPagerContainer.setClipChildren(false);
        mViewPager.setLayoutParams(params);
        //为ViewPager设置PagerAdapter
        int[] imageId = new int[]{R.mipmap.t, R.mipmap.t1, R.mipmap.t2, R.mipmap.t3, R.mipmap.t4};
        List<View> listDate = new ArrayList<View>();
        for (int i = 0; i < imageId.length; i++) {
            imageview = new ImageView(GalleryViewActivity.this);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            imageview.setImageResource(imageId[i]);
            listDate.add(imageview);
        }
        mViewPager.setAdapter(new MyPagerAdapter(listDate));
        //自动轮播设置
        mViewPager.setOnPageChangeListener(this);
        //设置ViewPager切换效果，即实现画廊效果
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //设置预加载数量
        mViewPager.setOffscreenPageLimit(2);
        //设置每页之间的左右间隔
        mViewPager.setPageMargin(0);
        //  mViewPager.setCurrentItem(Integer.MAX_VALUE/2);//默认在中间，使用户看不到边界
        //开始轮播效果
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
        //将容器的触摸事件反馈给ViewPager
        mViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
                return mViewPager.dispatchTouchEvent(event);
            }

        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mViewPagerContainer != null) {
            mViewPagerContainer.invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        //这里做切换ViewPager时，底部RadioButton的操作
        handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                break;
            default:
                break;
        }
    }
}
