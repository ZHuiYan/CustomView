package practice.zhy.com.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Wode9 on 2017/6/6.
 * 横向圆柱图
 */

public class HCylinderView extends View {
    private Paint mPaint;
    private Paint fillPaint;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private int histogram_height;//圆柱高度
    private float defaultX;//默认起始位置距离左边距离x
    private float defaultY;//默认起始距离顶部距离y,应该大于histogram_whith/2
    private int stroke = 2;//填充色需要减去边框的宽度

    private RectF rectF;
    private boolean isChange = false;//是否填充颜色
    private float raise;//增持占比
    private float equal;//持仓占比
    private float reduce;//减仓占比

    public HCylinderView(Context context) {
        this(context, null);
    }

    public HCylinderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HCylinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(context.getResources().getColor(R.color.public_gray));

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setStrokeWidth(2);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        histogram_height = (int) dpToPx(context, 15f);//默认值
        defaultX = dpToPx(context, 20f);
        defaultY = dpToPx(context, 20f);
    }

    public void setChange(boolean isChange, float raise, float equal, float reduce) {
        this.isChange = isChange;
        this.raise = raise;
        this.equal = equal;
        this.reduce = reduce;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw1(canvas, mPaint, 0);
        fillPaint.setColor(mContext.getResources().getColor(R.color.public_blue));

        if (isChange) {//填充
            draw1(canvas, fillPaint, stroke);
            fillPaint.setColor(mContext.getResources().getColor(R.color.public_red));
            float equalW = (1 - reduce) * (mWidth - 2 * defaultX) + defaultX;
            draw2(canvas, equalW);
            fillPaint.setColor(mContext.getResources().getColor(R.color.public_yellow));
            float equalR = (1 - reduce - equal) * (mWidth - 2 * defaultX) + defaultX;
            draw2(canvas, equalR);
        }
    }

    //绘制里边的填充色
    private void draw2(Canvas canvas, float fillX) {
        rectF = new RectF(defaultX + stroke, defaultY + stroke, defaultX + histogram_height - stroke, defaultY + histogram_height - stroke);
        if (fillX < defaultX + histogram_height / 2) {
            float angel = (float) Math.toDegrees(Math.acos((defaultX + histogram_height / 2 - fillX) / (histogram_height / 2)));
            canvas.drawArc(rectF, 180 - angel, angel * 2, false, fillPaint);
        } else if (fillX > mWidth - defaultX - histogram_height / 2) {
            canvas.drawArc(rectF, 90f, 180f, false, fillPaint);
            float wh = (defaultX + histogram_height / 2) - (mWidth - fillX);
            float angel = (float) Math.toDegrees(Math.asin(wh / (histogram_height / 2)));
            rectF = new RectF(mWidth - defaultX - histogram_height + stroke, defaultY + stroke, mWidth - defaultX - stroke, defaultY + histogram_height - stroke);
            canvas.drawArc(rectF, -90f, angel, false, fillPaint);
            canvas.drawArc(rectF, 90f - angel, angel, false, fillPaint);
            Path path = new Path();
            path.moveTo(defaultX + histogram_height / 2 - stroke, defaultY + stroke);
            path.lineTo(mWidth - defaultX - histogram_height / 2 - stroke, defaultY + stroke);
            float y1 = (float) (defaultY + (histogram_height / 2 - Math.sqrt(Math.pow(histogram_height / 2, 2) - Math.pow(wh, 2))));
            path.lineTo(fillX - stroke, y1);
            float y = (float) ((defaultY + histogram_height) - (histogram_height / 2 - Math.sqrt(Math.pow(histogram_height / 2, 2) - Math.pow(wh, 2))));
            path.lineTo(fillX - stroke, y);
            path.lineTo(mWidth - defaultX - histogram_height / 2 - stroke, defaultY + histogram_height - stroke);
            path.lineTo(defaultX + histogram_height / 2 - stroke, defaultY + histogram_height - stroke);
            canvas.drawPath(path, fillPaint);
        } else {
            Path path = new Path();
            path.addArc(rectF, 90f, 180f);
            path.lineTo(fillX, defaultY + stroke);
            path.lineTo(fillX, defaultY + histogram_height - 2);
            path.lineTo(defaultX + histogram_height / 2, defaultY + histogram_height - 2);
            canvas.drawPath(path, fillPaint);
        }
    }

    //绘制最外层形状
    private void draw1(Canvas canvas, Paint mPaint, int stroke) {
        //绘制外边框
        Path path = new Path();
        rectF = new RectF(defaultX + stroke, defaultY + stroke, defaultX + histogram_height - stroke, defaultY + histogram_height - stroke);
        path.addArc(rectF, 90f, 180f);
        path.lineTo(mWidth - defaultX - histogram_height / 2, defaultY + stroke);
        rectF = new RectF(mWidth - defaultX - histogram_height + stroke, defaultY + stroke, mWidth - defaultX - stroke, defaultY + histogram_height - stroke);
        path.addArc(rectF, -90f, 180f);
        path.lineTo(defaultX + histogram_height / 2, defaultY + histogram_height - stroke);
        canvas.drawPath(path, mPaint);
    }


    private void draw(Canvas canvas, Paint mPaint, int stroke) {
        //绘制外边框
        rectF = new RectF(defaultX + stroke, defaultY + stroke, defaultX + histogram_height - stroke, defaultY + histogram_height - stroke);
        canvas.drawArc(rectF, 90f, 180f, false, mPaint);
        canvas.drawLine(defaultX + histogram_height / 2, defaultY + stroke, mWidth - defaultX - histogram_height / 2, defaultY + stroke, mPaint);
        rectF = new RectF(mWidth - defaultX - histogram_height + stroke, defaultY + stroke, mWidth - defaultX - stroke, defaultY + histogram_height - stroke);
        canvas.drawArc(rectF, -90f, 180f, false, mPaint);
        canvas.drawLine(mWidth - defaultX - histogram_height / 2, defaultY + histogram_height - stroke, defaultX + histogram_height / 2, defaultY + histogram_height - stroke, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = widthSize * 1 / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = heightSize * 1 / 2;
        }
        defaultY = (mHeight - histogram_height) / 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    public float dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }
}
