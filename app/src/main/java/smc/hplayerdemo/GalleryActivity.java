package smc.hplayerdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smc.hplayerdemo.util.SysInfoUtils;
import smc.hplayerdemo.view.MyAdGallery;

public class GalleryActivity extends BaseActivity {
    private MyAdGallery home_ad_gallery;//图片轮播
    private LinearLayout home_adpoint_ll;//小点轮播
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        home_ad_gallery = (MyAdGallery) findViewById(R.id.home_ad_gallery);
        home_adpoint_ll = (LinearLayout) findViewById(R.id.home_adpoint_ll);

        List<String> mAdUrlList = new ArrayList<String>();
//        mAdUrlList.add();
//        网络请求。。获得轮播图片的地址集合mAdUrlList

        /**
         * 参数一：上下文
         * 参数二：轮播图片的地址集合
         * 参数三：自动轮播时间，写0为不自动切换
         * 参数四：轮播小点布局，可为空
         * 参数五：选中小点的xml文件id，圆点容器为空写0
         * 参数五：未选中小点的xml文件id，圆点容器可为空写0
         */
        home_ad_gallery.start(GalleryActivity.this, mAdUrlList, 3000, home_adpoint_ll, R.drawable.dot_focused,
                R.drawable.dot_normal);

        home_ad_gallery.setMyOnItemClickListener(new MyAdGallery.MyOnItemClickListener() {

            @Override
            public void onItemClick(int curIndex) {
//                界面跳转
                Toast.makeText(GalleryActivity.this,"第"+(curIndex+1)+"个",Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("信息","手机型号:"+SysInfoUtils.getModel()+"获取操作系统的版本号:"+SysInfoUtils.getSysRelease());
    }
}
