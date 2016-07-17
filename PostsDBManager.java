package com.example.brand.yambrand;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import winterwell.jtwitter.Twitter;

/**
 * Created by Brand on 18/01/2016.
 */
public class PostsDBManager extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "PostsDB.db";
        public static final String POSTS_TABLE_NAME = "posts";
        public static final String POSTS_COLUMN_ID = "post_id";
        public static final String POSTS_COLUMN_CREATED = "post_date";
        public static final String POSTS_COLUMN_TXT = "post_text";
        public static final String POSTS_COLUMN_USER = "post_user";

        public PostsDBManager(Context context){
            super(context, DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("create table posts " + "(post_id text, post_date date,post_text text,post_user text)");
            }catch (Exception e){
                Log.e("StatusActivity", "Error", e);
                e.printStackTrace();
               }
            }

        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}

        public long insertPosts(Twitter.Status status) {

            SQLiteDatabase db = this.getWritableDatabase();

            String id = String.valueOf(status.getId());
            String date = String.valueOf(status.getCreatedAt());
            String ptext = String.valueOf(status.getText());
            String puser = String.valueOf(status.getUser());

            ContentValues initialValues = new ContentValues();

            initialValues.put( POSTS_COLUMN_ID,id);
            initialValues.put(POSTS_COLUMN_CREATED, date);
            initialValues.put(POSTS_COLUMN_TXT, ptext);
            initialValues.put(POSTS_COLUMN_USER, puser);
            return db.insert(POSTS_TABLE_NAME, null, initialValues);
        }

        public Cursor RetrievePosts(int p) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor= db.rawQuery("Select * from " + POSTS_TABLE_NAME + " ORDER BY post_date DESC LIMIT " + p,null);
        return cursor;
    }


    }


