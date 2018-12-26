package ke.co.qkut.qkut.messages;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ke.co.qkut.qkut.R;
import ke.co.qkut.qkut.constants.GLobalHeaders;
import ke.co.qkut.qkut.constants.URL;
import ke.co.qkut.qkut.models.Service;
import ke.co.qkut.qkut.util.newtork.local.NetworkConnection;
import ke.co.qkut.qkut.util.newtork.local.OnReceivingResult;
import ke.co.qkut.qkut.util.newtork.local.RemoteResponse;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Message";
    String title;
    int notify_no = 1;
  long time;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("Notification body", remoteMessage.getData().toString());
        Log.e("Notification object", remoteMessage.getNotification().getBody());
        MessageReceivers.notify(new Service());
        time=remoteMessage.getSentTime();
        if (remoteMessage.getNotification() != null) {
            requestNewsForNotification(remoteMessage.getNotification().getBody());


        }

        createDefaultNotification();
    }
// [END receive_message]


    @Override
    public void onNewToken(final String s) {
        Log.e("Token received",s);
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("token",s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkConnection.makeAPostRequest(URL.USER_TOKEN, jsonObject.toString(), GLobalHeaders.getGlobalHeaders(getApplicationContext()), new OnReceivingResult() {
            @Override
            public void onErrorResult(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onReceiving100SeriesResponse(RemoteResponse remoteResponse) {
                NetworkConnection.remoteResponseLogger(remoteResponse);

            }

            @Override
            public void onReceiving200SeriesResponse(RemoteResponse remoteResponse) {
                 NetworkConnection.remoteResponseLogger(remoteResponse);
            }

            @Override
            public void onReceiving300SeriesResponse(RemoteResponse remoteResponse) {
                NetworkConnection.remoteResponseLogger(remoteResponse);

            }

            @Override
            public void onReceiving400SeriesResponse(RemoteResponse remoteResponse) {
                NetworkConnection.remoteResponseLogger(remoteResponse);

            }

            @Override
            public void onReceiving500SeriesResponse(RemoteResponse remoteResponse) {
                NetworkConnection.remoteResponseLogger(remoteResponse);

            }
        });
//
        super.onNewToken(s);
    }

    public void requestNewsForNotification(String id) {
    }
    private void createDefaultNotification(){
        /*Log.e("Article name", story.getCategory().getName());
        Intent intent = new Intent(context, DetailedStory.class);
        intent.putExtra("story", Story.getStoryToJson(story));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, notify_no *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);
        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
*/
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "1");
        notificationBuilder.setContentTitle("");
        notificationBuilder.setContentText("");
        notificationBuilder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName()+R.raw.sound_effect_message ));

        NotificationManager notificationManager =(NotificationManager)getApplicationContext(). getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notify_no + 2 /* ID of notification */, notificationBuilder.build());
    }

}
