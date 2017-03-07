package smc.hplayerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;


public class VFActivity extends BaseActivity {
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
        Button toGallery = (Button) findViewById(R.id.toGallery);
        toGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VFActivity.this,GalleryActivity.class));
            }
        });

    }
}
