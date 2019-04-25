package com.demo.ht.myandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.demo.ht.myandroid.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huangtuo on 2018/7/20.
 */

public class SnapUpCountDownTimerView extends LinearLayout {

    private TextView tv_hour_decade;
    private TextView tv_hour_unit;
    private TextView tv_min_decade;
    private TextView tv_min_unit;
    private TextView tv_sec_decade;
    private TextView tv_sec_unit;

    private Context context;

    private int hour_decade;
    private int hour_unit;
    private int min_decade;
    private int min_unit;
    private int sec_decade;
    private int sec_unit;

    private Timer timer;
    private TimerFinish timerFinish;

    public void setTimerFinish(TimerFinish timerFinish) {
        this.timerFinish = timerFinish;
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        }
    };

    public SnapUpCountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_countdowntimer, this);

        tv_hour_decade = (TextView) view.findViewById(R.id.tv_hour_decade);
        tv_hour_unit = (TextView) view.findViewById(R.id.tv_hour_unit);
        tv_min_decade = (TextView) view.findViewById(R.id.tv_min_decade);
        tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
        tv_sec_decade = (TextView) view.findViewById(R.id.tv_sec_decade);
        tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.SnapUpCountDownTimerView);
        int size = array.getInteger(R.styleable.SnapUpCountDownTimerView_viewSize, 14);
        int textColor = array.getColor(R.styleable.SnapUpCountDownTimerView_textColor, getResources().getColor(R.color.bg_yellow));

        TextView colonMinute = (TextView)view.findViewById(R.id.colon_minute);
        TextView colonhour = (TextView)view.findViewById(R.id.colon_hour);

        setTextStyle(tv_hour_decade, size, textColor);
        setTextStyle(tv_hour_unit, size, textColor);
        setTextStyle(tv_min_decade, size, textColor);
        setTextStyle(tv_min_unit, size, textColor);
        setTextStyle(tv_sec_decade, size, textColor);
        setTextStyle(tv_sec_unit, size, textColor);
        setTextStyle(colonMinute, size, textColor);
        setTextStyle(colonhour, size, textColor);

    }

    private void setTextStyle(TextView textView, int size, int color){
        textView.setTextSize(size);
        textView.setTextColor(color);
    }


    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }


    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void setSecond(int second){
        int hour = second / 3600;
        int min = (second - (hour * 3600)) / 60;
        int sec = second - hour * 3600 - min * 60;
        setTime(hour, min, sec);
    }

    public void setTime(int hour, int min, int sec) {

        if (hour >= 60 || min >= 60 || sec >= 60 || hour < 0 || min < 0
                || sec < 0) {
            throw new RuntimeException("时间格式错误,请检查你的代码");
        }

        hour_decade = hour / 10;
        hour_unit = hour - hour_decade * 10;

        min_decade = min / 10;
        min_unit = min - min_decade * 10;

        sec_decade = sec / 10;
        sec_unit = sec - sec_decade * 10;

        tv_hour_decade.setText(hour_decade + "");
        tv_hour_unit.setText(hour_unit + "");
        tv_min_decade.setText(min_decade + "");
        tv_min_unit.setText(min_unit + "");
        tv_sec_decade.setText(sec_decade + "");
        tv_sec_unit.setText(sec_unit + "");
    }


    private void countDown() {
        if (isCarry4Unit(tv_sec_unit)) {
            if (isCarry4Decade(tv_sec_decade)) {
                if (isCarry4Unit(tv_min_unit)) {
                    if (isCarry4Decade(tv_min_decade)) {
                        if (isCarry4Unit(tv_hour_unit)) {
                            if (isCarry4Decade(tv_hour_decade)) {
                                if(timerFinish != null){
                                    timerFinish.onTimerFinish();
                                }
                                stop();
                                setTime(0, 0, 0);//重置为0
                            }
                        }
                    }
                }
            }
        }
    }


    private boolean isCarry4Decade(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 5;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }
    }

    private boolean isCarry4Unit(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 9;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }
    }

    public interface TimerFinish{
        void onTimerFinish();
    }
}