package com.example.brand.yambrand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    Button updateButton;
    String post;
    Twitter twitter;
    Intent intent;
    PostsDBManager db = new PostsDBManager(this);

    private static final String TAG = "StatusActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pref:
                switchTask();
                return true;
            case R.id.strServ:
                startService(new Intent(this, postsRetrieveService.class));
                return true;
            case R.id.stpServ:
                stopService(new Intent(this, postsRetrieveService.class));
                return true;
            case R.id.posts:
                switchTask2();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        updateButton = (Button) findViewById(R.id.update_button);

        //New object type twitter with the user credentials
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strUserName = SP.getString("username", "NA");
        String strPassword= SP.getString("password", "NA");
        String strApiUrl= SP.getString("apiurl", "NA");
        //db.onUpgrade(db.getWritableDatabase(), 1, 2);
        twitter = new Twitter(strUserName, strPassword); // <4>
        //Definition of the post visualizer application url
        //twitter.setAPIRootUrl("http://yamba.newcircle.com/api");
        twitter.setAPIRootUrl(strApiUrl);
        // Set of the text changing monitoring for our editText
        editText.addTextChangedListener(postWatcher);

        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Retrieve the post from the edit text
                post = editText.getText().toString();
                // Task to execute when clicked
                new CustomTask().execute((Void[]) null);
                // Set an entry on the log file for the status button click event
                Log.d(TAG, "upload_button_clickEvent");
            }
        });

    }

    public void switchTask() {
        Intent intent = new Intent(this, test.class);
        startActivity(intent);
    }

    public void switchTask2() {
        Intent intent = new Intent(this, ScrollingActivity.class);
        startActivity(intent);
    }

    public class CustomTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... param) {
            // Posting the text retrieved
            try {
                twitter.setStatus(post);
            }catch (Exception e){
                Log.e("StatusActivity", "Error", e);
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void param) {
            // Message after the execute
            Toast.makeText(getApplicationContext(), "Post Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    //Declaration of the textwatcher objet
    private final TextWatcher postWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
        // Function executed while the text is been modified in the editText
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //editText become visible after modifying the post
            textView.setVisibility(View.VISIBLE);
            //show the quantity of available characters to add
            textView.setText("" + (140 - s.length()));
            if (s.length() <= 70) {
                //Setting the green color for the text of the counter
                textView.setTextColor(0xFF64D604);
            } else if (s.length() <= 100) {
                //Setting the yellow color for the text of the counter
                textView.setTextColor(0xFFFFEA00);
            } else {
                //setting the red color for the text of the counter
                textView.setTextColor(0xFFFC0303);
            }
        }
        public void afterTextChanged(Editable s) {
        }
    };
}
