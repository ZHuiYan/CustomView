package practice.zhy.com.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Wode9 on 2017/6/3.
 * 竖向圆柱
 */

public class VCylinderView extends View {
    private Context mContext;
    private Paint mPaint;//外边框画笔
    private Paint fillPaint;//填充画笔
    private int mWidth;
    private int mHeight;

    private int fillColor;//圆圈填充色
    private float vHeight = 200;//竖向高度
    private String descripiton = "";//圆圈描述文字

    private int histogram_width = 37;//柱状图宽度
    private float defaultX = 37;//默认起始位置距离左边距离x
    private float defaultY = 37;//默认起始距离顶部距离y,应该大于histogram_whith/2
    private float distance = 20f;//字体距离柱状图的高度
    private int stroke = 2;//边框宽度，填充颜色时的位置需要减去外边框的宽度
    private float radius;//圆圈半径


    private RectF rectF;//圆弧外边的矩形
    private boolean isChange = false;//圆柱图是否填充颜色
    private float percent;//填充部分的占比
    private String percentStr = "0.00%";//画的字体

    private boolean isSelect;//圆圈是否有填充


    public VCylinderView(Context context) {
        this(context, null);
    }

    public VCylinderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VCylinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VCylinderView, defStyleAttr, 0);
        fillColor = array.getColor(R.styleable.VCylinderView_fillColor, context.getResources().getColor(R.color.public_gray));
        vHeight = array.getDimension(R.styleable.VCylinderView_vHeight, 200f);
        descripiton = array.getString(R.styleable.VCylinderView_text);
        array.recycle();
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.public_lightblack));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setStrokeWidth(2);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        histogram_width = (int) dpToPx(context, 15f);//默认值
        defaultX = dpToPx(context, 20f);

        defaultY = dpToPx(context, 20f);
        distance = dpToPx(context, 20f);
    }

    public void setChange(int color, boolean isChange, float percent) {
        this.isChange = isChange;
        this.percent = percent;
        percentStr = get2decimal((percent * 100 + "")) + "%";
        fillPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //外边圆
        mPaint.setStrokeWidth(2);
        canvas.drawLine(defaultX, defaultY, defaultX, defaultY + vHeight, mPaint);
        canvas.drawLine(defaultX + histogram_width, defaultY, defaultX + histogram_width, defaultY + vHeight, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        rectF = new RectF(defaultX, defaultY + vHeight - histogram_width / 2, histogram_width + defaultX, defaultY + vHeight + histogram_width / 2);
        canvas.drawArc(rectF, 0f, 180f, false, mPaint);
        rectF = new RectF(defaultX, defaultY - histogram_width / 2, histogram_width + defaultX, defaultY + histogram_width / 2);
        canvas.drawArc(rectF, 0f, -180f, false, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        Rect rect = new Rect();
        mPaint.getTextBounds(percentStr, 0, percentStr.length(), rect);
        mPaint.setTextSize(dpToPx(mContext, 15f));
        canvas.drawText(percentStr, defaultX + histogram_width / 2 - mPaint.measureText(percentStr) / 2, defaultY + vHeight + histogram_width / 2 + distance, mPaint);


        //设置柱状图填充色
        if (isChange && percent != 0.0) {//设置填充色,如果为0%则只画外边框即可
            if (getFillY() > defaultY + vHeight) {//顶部在下部圆弧中
                float h = histogram_width / 2 - (defaultY + vHeight + histogram_width / 2 - getFillY());
                rectF = new RectF(defaultX + stroke, defaultY + vHeight - histogram_width / 2 + stroke, histogram_width + defaultX - stroke, defaultY + vHeight + histogram_width / 2 - stroke);
                canvas.drawArc(rectF, (float) Math.toDegrees(Math.asin(h / (histogram_width / 2))), (float) (180 - Math.toDegrees(Math.asin(h / (histogram_width / 2))) * 2), false, fillPaint);
            } else if (getFillY() < defaultY) {//顶部在顶部圆弧中
                float angle = (float) Math.toDegrees(Math.asin((defaultY - getFillY()) / (histogram_width / 2)));//圆弧角度
                float x = histogram_width / 2 - (float) Math.sqrt(Math.pow(histogram_width / 2, 2) - Math.pow(defaultY - getFillY(), 2));
                rectF = new RectF(defaultX + stroke, defaultY - histogram_width / 2 + stroke, histogram_width + defaultX - stroke, defaultY + histogram_width / 2 - stroke);
                canvas.drawArc(rectF, -180, angle, false, fillPaint);
                canvas.drawArc(rectF, -angle, angle, false, fillPaint);
                Path path = new Path();
                path.moveTo(defaultX + stroke, defaultY + stroke);
                path.lineTo(defaultX + x, getFillY() + stroke);
                path.lineTo(defaultX + histogram_width - x, getFillY() + stroke);
                path.lineTo(defaultX + histogram_width - stroke, defaultY);
                path.lineTo(defaultX + histogram_width - stroke, defaultY + vHeight);
                path.lineTo(defaultX + stroke, defaultY + vHeight);
                path.lineTo(defaultX + stroke, defaultY);
                canvas.drawPath(path, fillPaint);
                //填充下半部分圆弧
                rectF = new RectF(defaultX + stroke, defaultY + vHeight - histogram_width / 2 + stroke, histogram_width + defaultX - stroke, defaultY + vHeight + histogram_width / 2 - stroke);
                canvas.drawArc(rectF, 0f, 180f, false, fillPaint);
            } else {//顶部在中间矩形区域
                RectF rectF1 = new RectF(defaultX + stroke, getFillY(), defaultX + histogram_width - stroke, defaultY + vHeight);
                canvas.drawRect(rectF1, fillPaint);

                rectF = new RectF(defaultX + stroke, defaultY + vHeight - histogram_width / 2 + stroke, histogram_width + defaultX - stroke, defaultY + vHeight + histogram_width / 2 - stroke);
                canvas.drawArc(rectF, 0f, 180f, false, fillPaint);
            }
            if (percent == 1) {//如果是百分百就是整个填充满的形状
                rectF = new RectF(defaultX + stroke, getFillY() - histogram_width / 2 + stroke, histogram_width + defaultX - stroke, getFillY() + histogram_width / 2 - stroke);
                canvas.drawArc(rectF, 0f, -180f, false, fillPaint);
            }
        }

    }

    public float getFillY() {
        float y;
        float realH = (vHeight + histogram_width) * (percent);
        y = vHeight + defaultY + histogram_width / 2 - realH;
        if (percent == 1) {
            y = defaultY;
        }
        return y;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mWidth = widthSize;
        mHeight = heightSize;
        defaultX = (mWidth - histogram_width) / 2;
        radius = mWidth / 2 - dpToPx(mContext,8);
        /*if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize * 1 / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize * 1 / 2;
        }*/
        setMeasuredDimension(mWidth, mHeight);
    }

    public float dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    public String get2decimal(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (NumberValidationUtils.isDecimal(str) || NumberValidationUtils.isWholeNumber(str)) {
                java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
                return df.format(Float.parseFloat(str));
            } else {
                return str;
            }
        } else {
            return "";
        }
    }
}
