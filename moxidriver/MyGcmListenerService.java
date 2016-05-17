package tz.co.delis.www.moxidriver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by apple on 5/5/16.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    private NotificationUtils notificationUtils;
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        Log.d(TAG,"data:=>"+data);

        if (from.startsWith("/topics/")) {


            // message received from some topic.
        } else {
            // normal downstream message.
        }



        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */


//handling the receiving of data


        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);


            pushNotification.putExtra("customer_name",data.getString("customer_name"));
            pushNotification.putExtra("customer_telephone",data.getString("customer_telephone"));
            pushNotification.putExtra("customer_id",data.getString("customer_id"));
            pushNotification.putExtra("customer_location_name",data.getString("customer_location_name"));
            pushNotification.putExtra("Destination_name",data.getString("Destination_name"));


            // startService(pushNotification);

            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSound(defaultSoundUri);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            // play notification sound
          //  NotificationUtils notificationUtils = new NotificationUtils();
        //    notificationUtils.playNotificationSound();


        } else {

            Intent resultIntent = new Intent(getApplicationContext(), driverArea.class);
            resultIntent.putExtra("data", data);

            showNotificationMessage(getApplicationContext(), "hi", message,"6556",
                    resultIntent);
           // sendNotification(message);



        }





        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
       // sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, moxiMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Drive Request")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

}
