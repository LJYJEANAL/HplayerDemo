package smc.hplayerdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/3/1.
 */

public class SecondBezierView extends View {

    private Paint paint;
    //这个是在java代码中构建的时候调用
    public SecondBezierView(Context context) {
        super(context);
    }
    //这个是在使用xml构建的时候并且没有指定style的时候调用
    public SecondBezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //这是指定了style的时候调用的方法
    public SecondBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
}
