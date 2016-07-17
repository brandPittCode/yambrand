package com.example.brand.yambrand;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;

public class postsRetrieveService extends Service {

    PostsDBManager db = new PostsDBManager(this);

    private static final String TAG = "StatusActivity";
    private boolean FLAG = true;
    Twitter twitter;

    public postsRetrieveService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        // The service is being created
        Log.d(TAG, "Service Created");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        //twitter.getFriendsTimeline();
        Log.d(TAG, "Service Started");
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strUserName = SP.getString("username", "NA");
        String strPassword= SP.getString("password", "NA");
        String strApiUrl= SP.getString("apiurl", "NA");
        twitter = new Twitter(strUserName, strPassword);
        twitter.setAPIRootUrl(strApiUrl);

        Thread thread=new Thread(new retrieveThread());
        thread.start();
        return 0;
        //twitter.getFriendsTimeline();
        //return mStartMode;
    }
    @Override
    public void onDestroy() {
          Log.d(TAG, "Service Stopped");
          FLAG=false;
    }

    class retrieveThread extends Thread {

        @Override
        public void run()
        {   // Moves the current Thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            while (FLAG==true)
            {
                List<Twitter.Status> statuses = twitter.getFriendsTimeline();
                for (Twitter.Status status : statuses) {
                    //System.out.println(status.getId() + ":" + status.getCreatedAt() + ":" + status.getText() + ":" + status.getUser().getScreenName());
                    db.insertPosts(status);
                 }
                 try{sleep(3000);
                }catch(InterruptedException ex){}
            }
        }
    }
}


