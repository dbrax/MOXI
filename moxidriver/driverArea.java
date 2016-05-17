package tz.co.delis.www.moxidriver;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import tz.co.delis.www.moxidriver.Fragments.History;
import tz.co.delis.www.moxidriver.Fragments.Profile;
import tz.co.delis.www.moxidriver.Fragments.Requests;
import tz.co.delis.www.moxidriver.db.MoxiDataSource;
import tz.co.delis.www.moxidriver.db.MoxiHelper;
import tz.co.delis.www.moxidriver.model.DriverData;

public class driverArea extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient
        .ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {




    //---------for gcm registration ----------
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;




    //------------ end of gcm

    ActionBar actionBar ;
//for location
private static final String TAG = driverArea.class.getSimpleName();
private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private boolean isLocationServiceAvailable=false;

    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 10000; // 10 sec
    private static int DISPLACEMENT = 20; // 10 meters


    private double latitude,longitude;
//end of location variables



@Bind(R.id.nav_view) NavigationView navView;


    @Bind(R.id.progBar) ProgressBar mProgressBar;
    @Bind(R.id.Appstatus) TextView mAppStatus;



    @Bind(R.id.barStatus)
    TextView barStatus;


  private boolean progBarStatus=false;
  private boolean appStatusText=false;






    protected MoxiDataSource mdataSource=new MoxiDataSource(this);


    Cursor cuslang;
    protected MoxiHelper moxiColumns;
    DriverData driverDatas = new DriverData();
    public  Switch switchStatusView;

    private TextView driverEmail;
    private LocationManager mLocationManager;

String gcmStatus;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_area);

        ButterKnife.bind(this);


        //change action
        mdataSource.open();

        Cursor driverDataCursor = mdataSource.selectDriverData();
        driverDataCursor.moveToFirst();
        driverDatas.setDriver_name(driverDataCursor.getString(driverDataCursor.getColumnIndex("Driver_Name")));
        driverDatas.setDriver_email(driverDataCursor.getString(driverDataCursor.getColumnIndex
                ("Driver_Email")));
        driverDatas.setDriver_id(driverDataCursor.getInt(driverDataCursor.getColumnIndex("Driver_id")));

        driverDatas.setDriver_phone_number(driverDataCursor.getString(driverDataCursor.getColumnIndex("Driver_phone_number")));


        //---------------------getting GCM boolean value---------------------------------------





             mdataSource.close();

       navView.setNavigationItemSelectedListener(this);
       View header =navView.getHeaderView(0);
        TextView driverName = (TextView) header.findViewById(R.id.driver_name);
        driverEmail=(TextView)header.findViewById(R.id.driver_email);
        driverName.setText(driverDatas.getDriver_name().toUpperCase());
        driverEmail.setText(driverDatas.getDriver_email());



       mProgressBar.setVisibility(View.VISIBLE);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Config.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                   // mAppStatus.setText("No error Token");

                    Log.d(TAG,"The Token is sent");
                } else {
                    Log.d(TAG,"The Tokent is not  sent");

                }



              if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                  Log.d(TAG,"PUSH NOTIFICATION RECEIVED");

                    Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();

                        RequestUpdate();

                }





            }
        };




        // Registering BroadcastReceiver
        registerReceiver();


        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();


            createLocationRequest();

            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




actionBar=getSupportActionBar();

//Notify();

    }

    private void RequestUpdate() {

        mAppStatus.setVisibility(View.INVISIBLE);



        Intent intent = new Intent(this, tz.co.delis.www.moxidriver.Request.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);



        /*
// Create new fragment and transaction
        Fragment newFragment = new Requests();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.placeHolder, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();

*/


    }

    private void Notify() {
        Intent resultIntent = new Intent(this, driverArea.class);

        NotificationUtils not = new NotificationUtils(this);

        not.simpleNotification(resultIntent);


    }

    private void registerReceiver() {



        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

//check if location services are available.....

    public boolean isLocationServiceAvailable(){
        mLocationManager= (LocationManager) this.getSystemService(LOCATION_SERVICE);

        boolean checkGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean checkNetwork = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return checkGps || checkNetwork;



    }

    private void createLocationRequest(){

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private synchronized void buildGoogleApiClient() {


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {


        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }


        return true;
    }

    protected void onResume(){
        super.onResume();
        checkPlayServices();
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        registerReceiver();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));




    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }





    protected void onStart(){
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }



    }


    private void alertEnableLocation() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS Setting.");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        progBarStatus=false;

                        switchStatusView.setChecked(false);


                        showStatus(getString(R.string.OffServiceString));




                        stopLocationUpdates();
                        mRequestingLocationUpdates = false;
                        dialog.cancel();
                    }
                }).show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_area, menu);

      MenuItem StatusSwitch = menu.findItem(R.id.switchId);

       switchStatusView =  (Switch) StatusSwitch.getActionView();

        switchStatusView.setText(getString(R.string.off_status));
        switchStatusView.setTextColor(Color.BLACK);
        switchStatusView.setHighlightColor(Color.BLACK);



       switchStatusView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    actionBar.setTitle("Moxi Driver");
                    switchStatusView.setText(getString(R.string.on_status));
                    progBarStatus=true;

                    mRequestingLocationUpdates = true;
                    //check if location services are available
                    showStatus(getString(R.string.nullCustomerRequests));


                    if(!isLocationServiceAvailable())
                    {
                        alertEnableLocation();

                        barStatus.setText(R.string.gettingLocation);
                    }
                    else {



                            barStatus.setText(R.string.gettingLocation);
                            // Starting the location updates
                            startLocationUpdates();

                            Log.d(TAG, "Periodic location updates started!");



                    }

                    // First we need to check availability of play services



                   // Appstatus.setText("No Customer Requests!");
                    //startlocation update .......{1)check if location services are open if not
                    // open 2) check if internet is available if not ask to open 3) send latitude
                    // and longitude to the server service ... after each 5 minutes .... but only
                    // send if the location has changed ...so on location change function ....}





                } else {

                    actionBar.setTitle("Moxi Driver");
                    switchStatusView.setText(getString(R.string.off_status));


                    barStatus.setText(R.string.RequestTitle);
                    progBarStatus=false;
                    showStatus(getString(R.string.OffServiceString));
                    mRequestingLocationUpdates = false;
                    updateServerLocation(latitude,longitude,0);
                     stopLocationUpdates();
                    Log.d(TAG, "Periodic location updates stopped!");
                    //stoplocation update
                    //hence stop sending infomation to the server .....

                }
            }
        });






        return true;
    }

    private void showStatus(String status) {




        if(progBarStatus) {
           mProgressBar.setVisibility(View.VISIBLE);

            mAppStatus.setText(status);
            mAppStatus.setVisibility(View.INVISIBLE);

        }else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mAppStatus.setText(status);
            mAppStatus.setVisibility(View.VISIBLE);

        }

    }


    private void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,this);


    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest,this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(id) {
            case R.id.nav_profile :

                mAppStatus.setVisibility(View.INVISIBLE);
                fragmentClass = Profile.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Insert the fragment by replacing any existing fragment

                fragmentManager.beginTransaction().replace(R.id.placeHolder, fragment).commit();


                setTitle(item.getTitle());

                break;
            case  R.id.nav_history :
                mAppStatus.setVisibility(View.INVISIBLE);
                fragmentClass = History.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Insert the fragment by replacing any existing fragment

                fragmentManager.beginTransaction().replace(R.id.placeHolder, fragment).commit();


                setTitle(item.getTitle());
                break;
            case  R.id.nav_setting:

                Intent intent = new Intent(this, MSettings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


                break;

            case  R.id.nav_request:

                fragmentClass = Requests.class;
                mAppStatus.setVisibility(View.INVISIBLE);

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }



                fragmentManager.beginTransaction().replace(R.id.placeHolder, fragment).commit();


                break;


            case R.id.nav_sign_out:


                 break;
            case  R.id.nav_about:
                //item.setChecked(true);

                setTitle(item.getTitle());
                break;


         default:

             item.setChecked(true);
             // Set action bar title
             setTitle(item.getTitle());

             break;

        }







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {

            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            }


    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {

        // Assign the new location
        mLastLocation = location;
        latitude=mLastLocation.getLatitude();
        longitude=mLastLocation.getLongitude();



        //check internet connection here ...
        barStatus.setText(R.string.updatingLocation);


        updateServerLocation(latitude,longitude,1);



        barStatus.setText(R.string.RequestTitle);
        progBarStatus=false;

        if(!isNetworkAvailable()){
            showStatus(getString(R.string.NoInternet));

        }
        else
        showStatus(getString(R.string.nullCustomerRequests));

    }

    private void updateServerLocation(double latitude, double longitude,int status) {


  String loginUrl="http://moxi.delis.co.tz/updateDriverLocation.php";

        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody body = new FormBody.Builder()
                .add("latitude",String.valueOf(latitude))
                .add("longitude", String.valueOf(longitude))
                .add("status",String.valueOf(status))
                .add("Driver_id",String.valueOf(driverDatas.getDriver_id()))
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
                    Log.v(TAG, "Location Update"+jsonData);

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(getApplicationContext(), "Location updated "
                                    .trim(),
                                    Toast
                                    .LENGTH_SHORT)
                                    .show();

                        }
                    });

                }
                catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });


    }

    public   boolean isNetworkAvailable(){
        boolean isAvailable= false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =manager.getActiveNetworkInfo();


        if (networkInfo!=null && networkInfo.isConnected())
            isAvailable=true;


        return isAvailable;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
}
