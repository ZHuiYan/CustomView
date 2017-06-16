package practice.zhy.com.myview;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CustomDigitalClockView.ClockListener {

    private CustomDigitalClockView timeView;
    TextView tv;
    private long now, endtime;
    private long nextNowTime;//下一轮的开始时间
    private long nextEndTime;//下一轮的结束时间

    private VCylinderView dazhang;
    private HCylinderView hCylinderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        timeView = (CustomDigitalClockView) findViewById(R.id.timeView);
        tv = (TextView) findViewById(R.id.tv_text);
        timeView.setClockListener(this);

        now = System.currentTimeMillis();
        endtime = calculateEndTime(now);
        nextNowTime = endtime;
        Log.e("ppp","netNow:" + nextNowTime);
        Log.e("ppp","nextEndTime:" + nextEndTime);
        timeView.setStartTime(now);
        timeView.setEndTime(endtime); // 设置当前时间
        timeView.IsStop(false);


        dazhang = (VCylinderView) findViewById(R.id.dazhang);
        hCylinderView = (HCylinderView) findViewById(R.id.hc);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dazhang.setChange(getResources().getColor(R.color.public_red), true, 0.98f);
                hCylinderView.setChange(true, 0.01f, 0.95f, 0.04f);
            }
        });
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dazhang.setChange(getResources().getColor(R.color.public_red), true, 0.00f);
                hCylinderView.setChange(true, 0.01f, 0.99f, 0.0f);
            }
        });
    }


    private long calculateEndTime(long nowTime) {
        long endTime = 0;
        //判断now 范围：如果是在 15:00-00:00 或者 00:00 -9：30 本期竞猜结束倒计时：非交易时间段
        //如果是在：9：30 - 15：00 下期竞猜开始倒计时：交易时间段
        String time = TimeUtil.formatStr(nowTime, "HH:mm:ss");
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));
        String time1 = TimeUtil.formatStr(nowTime, "yyyy-MM-dd");
        Log.e("ppp","hour=" + hour);
        Log.e("ppp","minute=" + minute);
        if ((hour > 9 && hour < 15) || (hour == 9 && minute >= 30)) {
            tv.setText("下期竞猜开始倒计时:");
            time1 = time1 + " 15:00:00";
            endTime = TimeUtil.getStringToLong("yyyy-MM-dd HH:mm:ss", time1);
        } else if ((hour > 0 && hour < 9) || (hour == 0 && minute > 0) || (hour == 9 && minute < 30)) {
            time1 = time1 + " 9:30:00";
            endTime = TimeUtil.getStringToLong("yyyy-MM-dd HH:mm:ss", time1);
            tv.setText("本期竞猜结束倒计时:");
        } else if ((hour >= 15 && hour <= 23)) {
            Calendar calendar = Calendar.getInstance();//获得是本系统的，应该是取
            calendar.setTimeInMillis(nowTime);//设置为服务器返回的日期
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, day + 1);//当前日期的后一天
            Date date = calendar.getTime();
            time1 = TimeUtil.date2String(date, "yyyy-MM-dd") + " 9:30:00";
            endTime = TimeUtil.getStringToLong("yyyy-MM-dd HH:mm:ss", time1);
            tv.setText("本期竞猜结束倒计时:");
        }
        return endTime;
    }
    public static String formatStr(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(time);
        return formatter.format(date);
    }

    @Override
    public void timeEnd() {
        Log.e("ppp","mainactivity:timeEnd");
        nextEndTime = calculateEndTime(nextNowTime);
        timeView.setStartTime(nextNowTime);
        timeView.setEndTime(nextEndTime);
        timeView.IsStop(false);
    }
}
