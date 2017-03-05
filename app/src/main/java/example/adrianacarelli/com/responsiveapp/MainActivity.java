package example.adrianacarelli.com.responsiveapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button mUiThreadButton;
    Button mPostButton;
    Button mAsyncTaskButton;
    Button mIntentServiceButton;
    TextView mResultsTextView;
    DelayReceiver delayReceiver = new DelayReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultsTextView = (TextView) findViewById(R.id.textView);
        mUiThreadButton = (Button) findViewById(R.id.uiThreadButton);
        mPostButton = (Button) findViewById(R.id.postButton);
        mAsyncTaskButton = (Button) findViewById(R.id.asyncTaskButton);
        mIntentServiceButton = (Button) findViewById(R.id.intentServiceButton);

        mUiThreadButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);
        mAsyncTaskButton.setOnClickListener(this);
        mIntentServiceButton.setOnClickListener(this);


    }
    @Override
    protected void onResume (){
        super.onResume();
        registerReceiver(delayReceiver, new IntentFilter(DelayIntentService.ACTION_DELAY));
    }

    protected void onPause (){
        super.onPause();
        unregisterReceiver(delayReceiver);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.uiThreadButton:
                SystemClock.sleep(5000);
                mResultsTextView.setText("Updated on UI Thread");
                break;
            case R.id.postButton:
                mResultsTextView.setText("Starting View.post() ");

                new Thread(new Runnable() {
                    public void run() {
                        SystemClock.sleep(5000);
                        mResultsTextView.post(new Runnable() {
                            public void run() {
                                mResultsTextView.setText("Updated using View.post() ");
                            }
                        });
                    }
                }).start();

                break;
            case R.id.asyncTaskButton:
                new DelayTask().execute();
                break;
            case R.id.intentServiceButton:

                mResultsTextView.setText("Starting Intent Service ");

                Intent delayIntent = new Intent(getApplicationContext(), DelayIntentService.class);
                startService(delayIntent);
                break;
        }
    }

    class DelayTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            mResultsTextView.setText("Starting AsyncTask");
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result==0){
                mResultsTextView.setText("Updated via AsyncTask");
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            SystemClock.sleep(5000);
            return 0;

        }
    }

    public class DelayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DelayIntentService.ACTION_DELAY)){
                String message = intent.getExtras().getString(DelayIntentService.EXTRA_MESSAGE);
                mResultsTextView.setText( message);
            }
        }
    }

}
