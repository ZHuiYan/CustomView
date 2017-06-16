package practice.zhy.com.myview;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DigitalClock;
import android.widget.TextClock;

import java.util.Calendar;

/**
 * Custom digital clock
 *
 * @author veally@foxmail.com
 */
public class CustomDigitalClockView extends DigitalClock {
    private Calendar mCalendar;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";
    private FormatChangeObserver mFormatChangeObserver;
    private Runnable mTicker;
    private Handler mHandler;
    private long endTime;
    private long startTime;
    public static long distanceTime;
    private ClockListener mClockListener;
    private boolean mTickerStopped;
    @SuppressWarnings("unused")
    private String mFormat;

    public CustomDigitalClockView(Context context) {
        super(context);
        initClock(context);
    }

    public CustomDigitalClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        setFormat();

    }


    /**
     * 消息
     */
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: // 设置时间
                    setText(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        Log.e("ppp","onAttachedToWindow");
        super.onAttachedToWindow();
        mHandler = new Handler();
        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                if (startTime != 0 && endTime != 0) {
                    long currentTime = startTime;
                    distanceTime = endTime - currentTime;
                    distanceTime /= 1000;
                    if (distanceTime <= 0) {
                        setText("00:00:00");
                        mTickerStopped = true;
                        mClockListener.timeEnd();
                    } else {
                        setText(dealTime(distanceTime));
                    }
                    startTime = startTime + 1000;
                } else {
                    setText("00:00:00");
                }
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    /**
     * deal time string
     *
     * @param time
     * @return
     */
    public static Spanned dealTime(long time) {
        Spanned str;
        StringBuffer returnString = new StringBuffer();
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));
        returnString.append(hoursStr).append(":")
                .append(minutesStr).append(":").append(secondStr);
        str = Html.fromHtml(returnString.toString());
        return str;
    }

    /**
     * format time
     *
     * @param timeStr
     * @return
     */
    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
        getContext().getContentResolver().unregisterContentObserver(
                mFormatChangeObserver);
        Log.e("ppp","onDetachedFromWindow");
    }

    public void IsStop(boolean flog) {
        mTickerStopped = flog;
    }

    /**
     * Clock end time from now on.
     *
     * @param endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Clock end time from now on.
     *
     * @param startTime
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    public void setClockListener(ClockListener clockListener) {
        this.mClockListener = clockListener;
    }

    public interface ClockListener {
        void timeEnd();
    }
}