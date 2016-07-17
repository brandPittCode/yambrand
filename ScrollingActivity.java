package com.example.brand.yambrand;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScrollingActivity extends AppCompatActivity {
    ListView listView ;
    PostsDBManager db = new PostsDBManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        listView = (ListView) findViewById(R.id.list);
        int i=0;
        int p=40;
        String id,date,text,name;
        String[] values = new String[p];
        Cursor cursor=db.RetrievePosts(p);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            id=cursor.getString(0);
            date=cursor.getString(1);
            text=cursor.getString(2);
            name=cursor.getString(3);
            values[i]= id + ":" + date + ":" + text + ":" + name;
            cursor.moveToNext();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
       // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

}

