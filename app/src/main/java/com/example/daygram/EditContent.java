package com.example.daygram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by yubin on 2016/9/16.
 */
public class EditContent extends Activity {

    private TextView time;

    private EditText editContent;

    private Button clock;

    private Button done;

    private int mHour;

    private int mMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.edit_layout);

        Intent intent = getIntent();

        final int position = intent.getIntExtra("position", 0);

        final int itemClicked = intent.getIntExtra("itemClicked", 0);

        final int itemLength = intent.getIntExtra("itemLength", 0);

        final String passContent = intent.getStringExtra("passContent");

        Log.e("position", "position: " + intent.getIntExtra("position", 0));

        time = (TextView) findViewById(R.id.time);

        final String s = getDate(position);
        final String[] sArray = s.split("/");
        final String[] sArray1 = s.split(" ");
        if (sArray[0].equals("SUNDAY ")) {
            SpannableStringBuilder style=new SpannableStringBuilder(s);
            int start = s.indexOf("SUNDAY ");
            int end = start + "SUNDAY ".length();
            style.setSpan(new ForegroundColorSpan(Color.RED),
                    start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            time.setText(style);
        } else {
            time.setText(s);
        }

        editContent = (EditText) findViewById(R.id.edit_content);
        editContent.setText(passContent);

        clock = (Button) findViewById(R.id.clock);
        done = (Button) findViewById(R.id.done);

        editContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clock.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
                return false;
            }
        });

        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;

                int index = editContent.getSelectionStart();
                Editable editable = editContent.getText();

                long time = System.currentTimeMillis();
                final Calendar mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(time);
                mHour = mCalendar.get(Calendar.HOUR);
                mMinutes = mCalendar.get(Calendar.MINUTE);

                if(mCalendar.get(Calendar.AM_PM) == 0)
                {
                    s = "上午" + mHour + ":" + mMinutes + "分";
                }
                else
                {
                    s = "下午" + mHour + ":" + mMinutes + "分";
                }

                editable.insert(index, s);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditContent.this, MainActivity.class);
                intent.putExtra("itemClicked", itemClicked);
                intent.putExtra("itemLength", itemLength);

                intent.putExtra("week", sArray[0].substring(0, 3));
                Log.e("EditContent week is ", "week is " + sArray[0].substring(0, 3));
                intent.putExtra("date", sArray1[3]);
                intent.putExtra("content", editContent.getText().toString());
                Log.e("EditContent content", "is " + intent.getStringExtra("content"));
                startActivity(intent);
            }
        });
    }

    public static String getDate(int position){
        final Calendar c = Calendar.getInstance();
        String[] monthArray = new String[] {"January", "February", "March", "April",
                "May", "June", "July", "August", "September", "October", "November", "December"};
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String month = String.valueOf(c.get(Calendar.MONTH));// 获取当前月份
        for (int i = 0; i < 12; i++) {
            String j = String.valueOf(i);
            if (month.equals(j)) {
                month = monthArray[i];
                break;
            }
        }
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) - position);// 获取当前月份的日期号码
        int a;
        if (c.get(Calendar.DAY_OF_WEEK) - (position % 7) <= 0) {
            a = c.get(Calendar.DAY_OF_WEEK) + (7 - position % 7);
        } else {
            a = c.get(Calendar.DAY_OF_WEEK) - (position % 7);
        }
        String week = String.valueOf(a);
        if("1".equals(week)){
            week ="SUNDAY";
        }else if("2".equals(week)){
            week ="MONDAY";
        }else if("3".equals(week)){
            week ="TUESDAY";
        }else if("4".equals(week)){
            week ="WEDNESDAY";
        }else if("5".equals(week)){
            week ="THURSDAY";
        }else if("6".equals(week)){
            week ="FRIDAY";
        }else if("7".equals(week)){
            week ="SATURDAY";
        }
        Log.e("week", week);
        return week + " / " + month + " " + day + " / " + year;
    }
}
