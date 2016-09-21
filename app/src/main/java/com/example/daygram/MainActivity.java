package com.example.daygram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int TYPE_B = 0;

    private static final int TYPE_A = 1;

    private ListView list;

    //private ArrayList<HashMap<String, Object>> listItem = null;

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

        list = (ListView) findViewById(R.id.list_view);

        Intent intent = getIntent();
        final int itemClicked = intent.getIntExtra("itemClicked", 0);
        int itemLength = intent.getIntExtra("itemLength", 0);

        Log.e("MainActivity content", "is " + intent.getStringExtra("content"));

        if (itemLength < day) {
            for (int i = 1; i <= day; i++) {
                mData.add(null);
            }
            adapter = new BothAdapter(MainActivity.this, mData);
            list.setAdapter(adapter);
        }
        else if (intent.getStringExtra("week") != null
                && intent.getStringExtra("date") != null) {
            contentList.setWeek(intent.getStringExtra("week"));
            contentList.setDate(intent.getStringExtra("date"));
            contentList.setContent(intent.getStringExtra("content"));

            mData.set(itemClicked, contentList);
            adapter = new BothAdapter(MainActivity.this, mData);
            list.setAdapter(adapter);
        }

        list.setSelection(adapter.getCount()-1);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditContent.class);
                intent.putExtra("position", (day - (position + 1)));
                intent.putExtra("itemClicked", position);
                intent.putExtra("itemLength", list.getCount());
                startActivity(intent);
            }
        });
    }
}
