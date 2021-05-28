package skven.com.movienotifier;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import static skven.com.movienotifier.ApplicationImp.CHANNEL_ID;


public class ExampleService extends Service {

    private static final String TAG = "skven:ExampleService";




    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "fg service started");
        String url = intent.getStringExtra("url");
        String theatreName = intent.getStringExtra("theatreName");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(url)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        new Thread(new BgProcesser(getApplicationContext(), url, theatreName)).start();
        //stopSelf();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
