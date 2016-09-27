package com.example.daygram;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ListView list;

    private BothAdapter adapter = null;

    public static final Calendar c = Calendar.getInstance();

    public static final int day = c.get(Calendar.DAY_OF_MONTH);

    public static final int monthNow = c.get(Calendar.MONTH) + 1;

    public static final int yearNow = c.get(Calendar.YEAR);

    ContentList contentList = new ContentList();

    //数据准备：
    private ArrayList<Object> mData = new ArrayList<>();

    private ArrayList<Object> monthData = new ArrayList<>();

    private Button monthClick;

    private Button yearClick;

    public static String[] monthArray = new String[] {"January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if ((ArrayList<Object>) getObject("daygram.dat") != null ) {
            mData = (ArrayList<Object>) getObject("daygram.dat");
        }

        list = (ListView) findViewById(R.id.list_view);

        Intent intent = getIntent();
        final int itemClicked = intent.getIntExtra("itemClicked", 0);
        //int itemLength = intent.getIntExtra("itemLength", 0);

        final String passContent = intent.getStringExtra("content");
        Log.e("passContent", "in MainActivity is " + passContent);


        if (mData.size() == 0) {
            for (int i = 1; i <= day; i++) {
                mData.add(null);
            }
            adapter = new BothAdapter(MainActivity.this, mData);
            list.setAdapter(adapter);
        } else {
            adapter = new BothAdapter(MainActivity.this, mData);
            list.setAdapter(adapter);
        }
        if (intent.getStringExtra("week") != null
                && intent.getStringExtra("date") != null) {
            contentList.setWeek(intent.getStringExtra("week"));
            contentList.setDate(intent.getStringExtra("date"));
            contentList.setContent(intent.getStringExtra("content"));

            mData.set(itemClicked, contentList);
            saveObject("daygram.dat");
            adapter = new BothAdapter(MainActivity.this, mData);
            list.setAdapter(adapter);
        }

        list.setSelection(adapter.getCount() - 1);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("确认删除？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mData.set(position, null);
                    saveObject("daygram.dat");
                    adapter = new BothAdapter(MainActivity.this, mData);
                    list.setAdapter(adapter);
                    list.setSelection(adapter.getCount() - 1);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
                builder.create().show();
                return false;
                }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, EditContent.class);
                    intent.putExtra("position", (day - (position + 1)));
                    intent.putExtra("itemClicked", position);
                    intent.putExtra("itemLength", list.getCount());
                    if (mData.get(position) != null) {
                        Object[] obj = mData.toArray();
                        for (int i = 0; i < mData.size(); i++) {
                            if (i == position) {
                                contentList = (ContentList) obj[i];
                            }
                        }
                        intent.putExtra("passContent", contentList.getContent());
                    }
                    startActivity(intent);
                }
            });

        String month = String.valueOf(c.get(Calendar.MONTH));
        monthClick = (Button) findViewById(R.id.month);
        for (int i = 0; i < 12; i++) {
            String j = String.valueOf(i);
            if (month.equals(j)) {
                month = monthArray[i];
                break;
            }
        }

        monthClick.setText(month + "  |");
        monthClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonth(monthClick);
            }
        });

        yearClick = (Button) findViewById(R.id.year);
        String year = String.valueOf(c.get(Calendar.YEAR));
        yearClick.setText(year + "    |");
        yearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYear(yearClick);
            }
        });
    }

    private void saveObject(String name){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = this.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(mData);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }

    private Object getObject(String name){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = this.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }

    public void showMonth(View v)
    {
        PopupMenu pop = new PopupMenu(MainActivity.this, v);
        // 加载一个 R.menu.menu_control
        pop.getMenuInflater().inflate(R.menu.menu_month, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Jan:
                        monthClick.setText(monthArray[0] + "    |");
                        if (monthNow == 1 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(1, 31);
                        }

                        break;
                    case R.id.Feb:
                        monthClick.setText(monthArray[1] + "    |");
                        if (monthNow == 2 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            if (Integer.parseInt(yearClick.getText().toString().substring(0, 4)) % 4 == 0
                                    &&
                                    Integer.parseInt(yearClick.getText().toString().substring(0, 4)) % 100 != 0
                                    ||
                                    Integer.parseInt(yearClick.getText().toString().substring(0, 4)) % 400 == 0) {
                                monthChange(2, 29);
                            } else {
                                monthChange(2, 28);
                            }
                        }
                        break;
                    case R.id.Mar:
                        monthClick.setText(monthArray[2] + "        |");
                        if (monthNow == 3 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(3, 31);
                        }
                        break;
                    case R.id.Apr:
                        monthClick.setText(monthArray[3] + "         |");
                        if (monthNow == 4 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(4, 30);
                        }
                        break;
                    case R.id.May:
                        monthClick.setText(monthArray[4] + "            |");
                        if (monthNow == 5 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(5, 31);
                        }
                        break;
                    case R.id.Jun:
                        monthClick.setText(monthArray[5] + "           |");
                        if (monthNow == 6 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(6, 30);
                        }
                        break;
                    case R.id.Jul:
                        monthClick.setText(monthArray[6] + "           |");
                        if (monthNow == 7 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(7, 31);
                        }
                        break;
                    case R.id.Aug:
                        monthClick.setText(monthArray[7] + "          |");
                        if (monthNow == 8 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(8, 31);
                        }
                        break;
                    case R.id.Sep:
                        monthClick.setText(monthArray[8] + "  |");
                        if (monthNow == 9 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(9, 30);
                        }
                        break;
                    case R.id.Oct:
                        monthClick.setText(monthArray[9] + "   |");
                        if (monthNow == 10 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(10 , 31);
                        }
                        break;
                    case R.id.Nov:
                        monthClick.setText(monthArray[10] + "  |");
                        if (monthNow == 11 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(11, 30);
                        }
                        break;
                    case R.id.Dec:
                        monthClick.setText(monthArray[11] + "  |");
                        if (monthNow == 12 &&
                                yearNow == Integer.parseInt(yearClick.getText().toString().substring(0, 4))) {
                            thisMonth();
                        } else {
                            monthChange(12, 31);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        pop.show();
    }

    public void showYear(View v)
    {
        PopupMenu pop = new PopupMenu(MainActivity.this, v);
        // 加载一个 R.menu.menu_control
        pop.getMenuInflater().inflate(R.menu.menu_year, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.one:
                        yearClick.setText("2011    |");
                        break;
                    case R.id.two:
                        yearClick.setText("2012    |");
                        break;
                    case R.id.three:
                        yearClick.setText("2013    |");
                        break;
                    case R.id.four:
                        yearClick.setText("2014    |");
                        break;
                    case R.id.five:
                        yearClick.setText("2015    |");
                        break;
                    case R.id.six:
                        yearClick.setText("2016    |");
                        break;
                }
                return true;
            }
        });
        pop.show();
    }

    public void monthChange(final int monthPass, int dayTotal) {
        if (monthData.size() == 0) {
            for (int i = 0; i < dayTotal; i++) {
                monthData.add(null);
            }
            adapter = new BothAdapter(MainActivity.this,monthData);
            list.setAdapter(adapter);
        } else {
            adapter = new BothAdapter(MainActivity.this,monthData);
            list.setAdapter(adapter);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MonthAndYearChange.class);
                intent.putExtra("month", monthPass);
                intent.putExtra("year", yearClick.getText().toString().substring(0,4));
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    public void thisMonth() {
        if ((ArrayList<Object>) getObject("daygram.dat") != null ) {
            mData = (ArrayList<Object>) getObject("daygram.dat");
        }
        if (mData.size() != 0) {
            adapter = new BothAdapter(MainActivity.this, mData);
            list.setAdapter(adapter);
            list.setSelection(list.getCount() - 1);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MonthAndYearChange.class);
                intent.putExtra("month", 9);
                intent.putExtra("year", yearClick.getText().toString().substring(0,4));
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}
