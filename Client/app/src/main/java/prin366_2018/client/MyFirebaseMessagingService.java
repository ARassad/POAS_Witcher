package prin366_2018.client;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.witcher_icon)
                        .setContentTitle(title)
                        .setSound(sound)
                        .setContentText(text);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(getId(), mBuilder.build());
    }

    private int getId() {
        return (int) System.currentTimeMillis() % Integer.MAX_VALUE;
    }
}