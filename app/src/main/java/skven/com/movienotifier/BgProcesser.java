package skven.com.movienotifier;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import static android.content.Context.AUDIO_SERVICE;

public class BgProcesser implements Runnable {
    private static final String TAG = "skven:BgProcesser";
    private Context context;
    private String url;
    private String theatreName;

    BgProcesser(final Context context, final String url, final String theatreName) {
        this.context = context;
        this.url = url;
        this.theatreName = theatreName;
    }

    @Override
    public void run() {

        try {
            Log.i(TAG, "url: " + url);
            Log.i(TAG, "theatreName: " + theatreName);
            MovieChecker.updatePostMovieBookingHaveStarted(context, url, theatreName);
            final AudioManager mAudioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

            AssetFileDescriptor afd = context.getAssets().openFd("master.mp3");
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.setLooping(true);
            player.start();
        } catch (Exception e) {
            Log.e(TAG, "error: ", e);
        }

    }
}
