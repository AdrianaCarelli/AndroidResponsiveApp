package example.adrianacarelli.com.responsiveapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by adrianacarelli on 23/02/17.
 */

public class DelayIntentService extends IntentService {

    public static final String ACTION_DELAY = "example.adrianacarelli.com.responsiveapp.action.DELAY";
    public static final String EXTRA_MESSAGE = "example.adrianacarelli.com.responsiveapp.extra.MESSAGE";

    public DelayIntentService() {
        super("DelayIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SystemClock.sleep(5000);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_DELAY);
        broadcastIntent.putExtra(EXTRA_MESSAGE, "Updated Using IntentService & BroadcastReceiver");
        sendBroadcast(broadcastIntent);

    }
}
