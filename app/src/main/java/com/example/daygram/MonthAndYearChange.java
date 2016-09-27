package com.example.daygram;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;

/**
 * Created by yubin on 2016/9/26.
 */
public class MonthAndYearChange extends Activity {

    private TextView time_change;

    private EditText editContent;

    private Button clockChange;

    private Button doneChange;

    private int mHour;

    private int mMinutes;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.change_layout);

        Intent intent = getIntent();

        final int month = intent.getIntExtra("month", 0);
        String year = intent.getStringExtra("year");

        final int y = Integer.parseInt(year);

        final int position = intent.getIntExtra("position", 0);

        time_change = (TextView) findViewById(R.id.time_change);
        final String s = getDate(position, month, y);
        final String[] sArray = s.split("/");
        final String[] sArray1 = s.split(" ");
        if (sArray[0].equals("SUNDAY ")) {
            SpannableStringBuilder style=new SpannableStringBuilder(s);
            int start = s.indexOf("SUNDAY ");
            int end = start + "SUNDAY ".length();
            style.setSpan(new ForegroundColorSpan(Color.RED),
                    start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            time_change.setText(style);
        } else {
            time_change.setText(s);
        }

        editContent = (EditText) findViewById(R.id.edit_content_change);
        editContent.setText(intent.getStringExtra("passContent"));
        editContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clockChange.setVisibility(View.VISIBLE);
                doneChange.setVisibility(View.VISIBLE);
                return false;
            }
        });

        clockChange = (Button) findViewById(R.id.clock_change);
        clockChange.setOnClickListener(new View.OnClickListener() {
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

        doneChange = (Button) findViewById(R.id.done_change);
        doneChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonthAndYearChange.this, MainActivity.class);

                intent.putExtra("itemClickedAnother", position);

                intent.putExtra("yearChoose", y);
                intent.putExtra("monthChoose", month);

                intent.putExtra("weekAnother", sArray[0].substring(0, 3));
                intent.putExtra("dateAnother", sArray1[3]);
                intent.putExtra("contentAnother", editContent.getText().toString());

                startActivity(intent);
            }
        });
    }

    public static String getDate(int position, int m, int y){

        String[] monthArray = new String[] {"January", "February", "March", "April",
                "May", "June", "July", "August", "September",
                "October", "November", "December"};
        String month = "";
        for (int i = 0; i < monthArray.length; i++) {
            if ((m - 1) == i) {
                month = monthArray[i];
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);//先指定年份
        calendar.set(Calendar.MONTH, m - 1);//再指定月份 Java月份从0开始算

        String day = String.valueOf(position + 1);// 获取当前月份的日期号码

        String week = "";

        //获取指定年份月份中指定某天是星期几
        calendar.set(Calendar.DAY_OF_MONTH, position + 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                week = "SUNDAY";
                break;
            case 2:
                week = "MONDAY";
                break;
            case 3:
                week = "TUESDAY";
                break;
            case 4:
                week = "WEDNESDAY";
                break;
            case 5:
                week = "THURSDAY";
                break;
            case 6:
                week = "FRIDAY";
                break;
            case 7:
                week = "SATURDAY";
                break;
        }
        return week + " / " + month + " " + day + " / " + y;
    }
}
