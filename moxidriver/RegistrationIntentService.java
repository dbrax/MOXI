package tz.co.delis.www.moxidriver;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tz.co.delis.www.moxidriver.db.MoxiDataSource;

/**
 * Created by apple on 5/5/16.
 */
public class RegistrationIntentService extends IntentService {
    private static final String[] TOPICS = {"global"};
    MoxiDataSource mdataSource;
    Cursor driverDataCursor;
    private static final String TAG = RegistrationIntentService.class.getSimpleName();
    /**
     *
     *
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken("958297639273", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        //here i send the token to the webserver....
      //  Toast.makeText(RegistrationIntentService.this, "hehehe"+token, Toast.LENGTH_SHORT).show();

         mdataSource =new MoxiDataSource(this);
        mdataSource.open();
        driverDataCursor = mdataSource.selectDriverData();
        driverDataCursor.moveToFirst();

        mdataSource.close();

       int id= driverDataCursor.getInt(driverDataCursor.getColumnIndex("Driver_id"));

        String loginUrl="http://moxi.delis.co.tz/gcm/updateGcmRegId.php";

        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody body = new FormBody.Builder()
                .add("appType","Driver")
                .add("Reg_ID", token)
                .add("user_id",String.valueOf(id))
                .build();
        Request request = new Request.Builder()
                .url(loginUrl)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String jsonData = response.body().string();
                    Log.v(TAG, jsonData);


                }
                catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });




    }


    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }

    }
}
