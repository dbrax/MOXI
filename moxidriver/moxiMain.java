package tz.co.delis.www.moxidriver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.os.Handler;


import android.os.Message;
import android.widget.Toast;

import java.util.Locale;

import tz.co.delis.www.moxidriver.db.MoxiDataSource;
import tz.co.delis.www.moxidriver.model.DriverData;


public class moxiMain extends AppCompatActivity {




    String ServerAPIKey="AIzaSyAhbIwiAVJ8NUyQNQatsc1GWLvIn7rgCfQ";
    String SenderID="958297639273";

    protected MoxiDataSource mdataSource=new MoxiDataSource(this);

    private TextView english,turkish;
    driverArea da;
    Cursor useCus;
    Cursor cus;
    Cursor driveCursorLoginDetails;
    Cursor driveCursorLoginPassword;

    DriverData driverDetails = new DriverData();



    InformationDialog info = new InformationDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moxi_main);
        IntentFilter mfilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        InternetReceiver receiver = new InternetReceiver();
        this.registerReceiver(receiver,mfilter);


        mdataSource.open();

        //------- i will start coding here tomorrow





        waitabit();




    }
    Handler waitHandle = new Handler(){

        @Override
        public void handleMessage(Message msg) {

             cus = mdataSource.selectLanguage();
            useCus = mdataSource.selectUseStatus();
            driveCursorLoginDetails=mdataSource.selectDriverLoginDetails();
            driveCursorLoginPassword=mdataSource.selectDriverPassword();
            driveCursorLoginPassword.moveToFirst();
            driveCursorLoginDetails.moveToFirst();
            cus.moveToFirst();
            useCus.moveToFirst();


            ActivitySelector();


            mdataSource.close();
        }
    };

    private void ActivitySelector() {
        if(cus.getCount()==0){
            Intent myIntent = new Intent(moxiMain.this, LanguageSelector.class);
            startActivity(myIntent);
        }
        else{

         //   Toast.makeText(moxiMain.this, ""+cus.getString(0), Toast.LENGTH_SHORT).show();
            String useSatus = useCus.getString(0);
            if (cus.getString(0).equals("eng")){  //english here...
                if(useSatus.equals("1")){

                if(driveCursorLoginDetails.getCount()==0) {


                    Intent driverUI = new Intent(moxiMain.this, driverArea.class);
                    startActivity(driverUI);

                } else {
                    mdataSource.open();









                    Intent driverUI = new Intent(moxiMain.this, driverArea.class);
                    driverUI.putExtra("login_details",driveCursorLoginDetails.getString(0));
                    driverUI.putExtra("login_password",driveCursorLoginPassword.getString(0));
                    mdataSource.close();

                    startActivity(driverUI);
                }

                }
                else{
                Intent in = new Intent(moxiMain.this, Login_reg.class);
                startActivity(in);


                }
            }
            else if(cus.getString(0).equals("tr")) { // turkish here......

               // Toast.makeText(moxiMain.this, ""+cus.getString(0), Toast.LENGTH_SHORT).show();
                Resources res = this.getResources();
                // Change locale settings in the app.
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale("tr");
                res.updateConfiguration(conf, dm);


                if(useSatus.equals("1")){
                    Intent driverUI = new Intent(moxiMain.this, driverArea.class);
                    startActivity(driverUI);

                }
                else{
                    Intent in = new Intent(moxiMain.this, Login_reg.class);
                    startActivity(in);


                }
            }
            else{


                Intent myIntent = new Intent(moxiMain.this, LanguageSelector.class);

                startActivity(myIntent);
            }
        }
    }

    private void waitabit() {
        Runnable runble = new Runnable() {
            @Override
            public void run() {
                long timing = System.currentTimeMillis() + 3000;
                while (System.currentTimeMillis() < timing)
                {
                    synchronized (this){
                        try{
                            wait(timing-System.currentTimeMillis());
                        }
                        catch (Exception e){}
                    }
                }
                waitHandle.sendEmptyMessage(0);
            }
        };

        Thread waitThread = new Thread(runble);
        waitThread.start();

    }
    public   boolean isNetworkAvailable(){
        boolean isAvailable= false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =manager.getActiveNetworkInfo();


        if (networkInfo!=null && networkInfo.isConnected())
            isAvailable=true;


        return isAvailable;
    }







}
