package skven.com.movienotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootCompleteListener extends BroadcastReceiver {

    private static final String TAG = "skven:BootCompleteListener";
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        String url = sharedPreferences.getString("url", "");
        String theatreName = sharedPreferences.getString("theatreName", "");
        MainActivity.startService(context, url, theatreName);
    }
}
