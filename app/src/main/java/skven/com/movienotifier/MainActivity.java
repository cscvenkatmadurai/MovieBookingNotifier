package skven.com.movienotifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "skven:MainActivity";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private EditText urlEditText;
    private EditText theatreNameEditText;
    private TextView countTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlEditText = findViewById(R.id.edit_text_url);
        theatreNameEditText = findViewById(R.id.edit_text_theatre_name);
        countTextView = findViewById(R.id.tv_count);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        long count = sharedPreferences.getLong("count", 0);
        boolean error = sharedPreferences.getBoolean("error", false);
        countTextView.setText("count: " + Long.toString(count) + "  error: " + error);
        verifyStoragePermissions(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        long count = sharedPreferences.getLong("count", 0);
        boolean error = sharedPreferences.getBoolean("error", false);
        countTextView.setText("count: " + Long.toString(count) + "  error: " + error);
    }

    public void startService(View v) {
        String url = urlEditText.getText().toString();
        String theatreName = theatreNameEditText.getText().toString();
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPref", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("url", url);
        edit.putString("theatreName", theatreName);
        edit.apply();
        startService(this, url, theatreName);
    }

    public static void startService(Context context, String url, String theatreName) {
        if ( url == null || url.isEmpty() || theatreName == null || theatreName.isEmpty() ) {
            return;
        }
        Intent serviceIntent = new Intent(context, ExampleService.class);
        serviceIntent.putExtra("url", url);
        serviceIntent.putExtra("theatreName", theatreName);
        Log.i(TAG, "going to start fg service");
        ContextCompat.startForegroundService(context, serviceIntent);
    }

    public void stopService(View v) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPref", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
