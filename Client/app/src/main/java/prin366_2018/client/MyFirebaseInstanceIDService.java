package prin366_2018.client;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Created by Mikhail on 23.02.2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    // Get updated InstanceID token.

    private static final String TAG = "MyFirebaseIIDService";
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";
    private static final String TOKEN =  FirebaseInstanceId.getInstance().getToken();

    /**
     * The Application's current Instance ID token is no longer valid
     * and thus a new one must be requested.
     */
    @Override
    public void onTokenRefresh() {
        // If you need to handle the generation of a token, initially or
        // after a refresh this is where you should do that.
        Log.d(TAG, "FCM Token: " + TOKEN);

        // Once a token is generated, we subscribe to topic.
        FirebaseMessaging.getInstance()
                .subscribeToTopic(FRIENDLY_ENGAGE_TOPIC);
    }

    public String getToken(){
        return TOKEN;
    }

}
