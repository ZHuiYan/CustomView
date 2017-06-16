package practice.zhy.com.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Wode9 on 2017/6/3.
 */

public class HistogramView extends View {

    private Context mContext;
    private Paint mPaint;//外边框画笔
    private Paint fillPaint;//填充画笔
    private int fillColor;//填充色
    private float vHeight = 200;//竖向高度
    private int cWidth = 400;//横向宽度
    private int histogram_width;//柱状图宽度
    private float defaultX;//默认起始位置距离左边距离x
    private float defaultY;//默认起始距离顶部距离y
    private float radius;//圆角半径
    private float distance;//字体距离柱状图的高度

    private RectF rectF;
    private boolean isChange = false;//是否填充颜色
    private float percent;//填充部分的占比
    private String percentStr = "0%";//画的字体

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VCylinderView, defStyleAttr, 0);
        fillColor = array.getColor(R.styleable.VCylinderView_fillColor, context.getResources().getColor(R.color.colorPrimary));
        vHeight = array.getDimension(R.styleable.VCylinderView_vHeight, 200f);
        array.recycle();
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.colorAccent));
        mPaint.setAntiAlias(true);

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setStrokeWidth(2);
        fillPaint.setStyle(Paint.Style.FILL);

        histogram_width = (int) dpToPx(context, 20f);//默认值
        defaultX = dpToPx(context, 20f);
        defaultY = dpToPx(context, 20f);
        radius = dpToPx(context, 10f);
        distance = dpToPx(context,20f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize * 1 / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize * 1 / 2;
        }
        setMeasuredDimension(width, height);
    }

    public void setIsChange(boolean isChange, float percent) {
        this.isChange = isChange;
        this.percent = percent;
        percentStr = get2decimal((percent * 100 + "")) + "%";
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //外边圆
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        rectF = new RectF(defaultX, defaultY, defaultX + histogram_width, defaultY + vHeight);
        canvas.drawRoundRect(rectF, radius, radius, mPaint);

        //百分比
        mPaint.setStyle(Paint.Style.FILL);
        Rect rect = new Rect();
        mPaint.getTextBounds(percentStr,0,percentStr.length(),rect);
        mPaint.setTextSize(dpToPx(mContext,15f));
        canvas.drawText(percentStr,defaultX + histogram_width / 2 - rect.width() / 2,defaultY + vHeight + distance,mPaint);

        //填充颜色
        if (isChange) {
            fillPaint.setColor(fillColor);
            rectF = new RectF(defaultX, getFillY(), defaultX + histogram_width, defaultY + vHeight);
            canvas.drawRoundRect(rectF, radius, radius, fillPaint);
        }
    }

    public float getFillY() {
        return (defaultY + vHeight) * (1 - percent);
    }

    public float dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }
    public  String get2decimal(String str){
        if (!TextUtils.isEmpty(str)){
            if (NumberValidationUtils.isDecimal(str) || NumberValidationUtils.isWholeNumber(str)){
                java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00");
                return df.format(Float.parseFloat(str));
            }else {
                return str;
            }
        }else {
            return "";
        }
    }
}
