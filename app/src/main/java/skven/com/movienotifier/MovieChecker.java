package skven.com.movienotifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MovieChecker {
    private static final String TAG = "skven:MovieChecker";


    public static void updatePostMovieBookingHaveStarted(Context context, String urlString, String theatreName) throws Exception {
        long count = 0;
        boolean hasBookingOpened = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        while (!hasBookingOpened) {
            try {
                System.setProperty("http.agent", "Chrome");
                //https://in.bookmyshow.com/buytickets/master-madurai/movie-madu-ET00110368-MT/20210113
                // https://in.bookmyshow.com/buytickets/aayirathil-oruvan-madurai/movie-madu-ET00002304-MT/20210109
                URL url = new URL(urlString);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                StringBuilder totalLine = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    Log.d(TAG,  line);
                    totalLine.append(line);
                }
                in.close();

                if (totalLine.toString().toLowerCase().contains(theatreName)) {
                    sharedPreferences.edit().putBoolean("error", false).apply();
                    hasBookingOpened = true;
                    return;
                }

                Log.i(TAG, "count: " + count);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putLong("count", count).apply();
                count++;
                Thread.sleep(5_000L);
            } catch (Exception e) {
                Log.e(TAG, "excepton in MovieChecker", e);
                sharedPreferences.edit().putBoolean("error", true).apply();
            }
        }
    }

}
