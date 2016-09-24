package com.example.daygram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.OsConstants;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView list;

    private BothAdapter adapter = null;

    public static final Calendar c = Calendar.getInstance();

    public static final int day = c.get(Calendar.DAY_OF_MONTH);

    ContentList contentList = new ContentList();

    //数据准备：
    public static ArrayList<Object> mData = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mData = (ArrayList<Object>) getObject("daygram.dat");

        list = (ListView) findViewById(R.id.list_view);

        Intent intent = getIntent();
        final int itemClicked = intent.getIntExtra("itemClicked", 0);
        int itemLength = intent.getIntExtra("itemLength", 0);

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
}
