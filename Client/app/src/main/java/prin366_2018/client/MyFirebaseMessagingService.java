package prin366_2018.client;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Map;
import java.util.Random;

/**
 * Created by Mikhail on 23.02.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        final RemoteMessage.Notification data = remoteMessage.getNotification();
        final String id = remoteMessage.getMessageId();
        String text = data.getBody();
        String title = data.getTitle();
        String icon = data.getIcon();

        //Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        //Log.d(TAG, "FCM Notification Message: " +*
        //        remoteMessage.getNotification());
        //Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
    }
}