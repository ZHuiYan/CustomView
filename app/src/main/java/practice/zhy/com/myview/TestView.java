package practice.zhy.com.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Wode9 on 2017/6/5.
 */

public class TestView extends View {
    private Paint mPaint;
    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(20,20,200,200);
        canvas.drawRect(rectF,mPaint);
        canvas.drawArc(rectF,-180,12,true,mPaint);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        RectF rectF1 = new RectF(20,210,200,400);
        Path path = new Path();
        path.addArc(rectF1,-180,90);
        path.lineTo(200,210);
        path.lineTo(200,305);
        path.addArc(rectF1,0,90);
        path.lineTo(20,400);
        path.lineTo(20,305);

//        path.lineTo(200,400);
//        path.lineTo(20,400);
        canvas.drawPath(path,mPaint);
    }
}
