package smc.hplayerdemo.gallery;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */



public class MyPagerAdapter extends PagerAdapter {
    private List<View> list;// 声明一个list集合

    public MyPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        //return viewList==null?0:viewList.size();
//            return list.size();//ViewPager里的个数
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//            ImageView imageView = new ImageView(GalleryViewActivity.this);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setImageResource(R.mipmap.t);
//            ((ViewPager)container).addView(imageView);
        position %= list.size();
        if (position < 0) {
            position = list.size() + position;
        }
        View view = list.get(position);
        // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//            ((ViewPager)container).removeView((ImageView)object);
    }
}