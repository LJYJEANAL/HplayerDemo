package smc.hplayerdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/3/13.
 */

public class CircleView extends View{
    private  int mColor= Color.RED;
    private Paint mPain=new Paint();

    public CircleView(Context context) {
        super(context);
        init();
    }
    public CircleView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        init();
    }
    public CircleView(Context context, AttributeSet attributeSet, int defStyleAttr) {

        super(context,attributeSet,defStyleAttr);
        init();
    }
   private  void init(){
       mPain.setColor(mColor);

   }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置样式-空心矩形
        mPain.setStyle(Paint.Style.STROKE);
        int width=getWidth();
        int heigth=getHeight();
        int radius=Math.min(width,heigth)/2;
        canvas.drawCircle(width/2,heigth/2,radius,mPain);
//        canvas.drawRect(150,75,50,20,mPain);
    }
}
