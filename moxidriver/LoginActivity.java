package tz.co.delis.www.moxidriver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tz.co.delis.www.moxidriver.db.MoxiDataSource;
import tz.co.delis.www.moxidriver.model.DriverData;

public class LoginActivity extends AppCompatActivity {

    // need to rewrite this activity ...
    //need to change the ui a little bit ...
    // need to parse json file received from the server ...
    //need to autheticate the user and add the data in the sqlite database then switch the
    //activity to the main ..


    // ---------Activity class members -----------------------------
    protected MoxiDataSource mdataSource=new MoxiDataSource(this);

    public TextView res;

    @Bind(R.id.email_phone) EditText email_or_phone_view;
    @Bind(R.id.password) EditText password_view;
    @Bind(R.id.loginbtn) Button loginbtn;


    private String emailPhone;
    private String password;

    InformationDialog info = new InformationDialog();
    private static final String loginUrl = "http://moxi.delis.co.tz/getDriverData.php";
    RequestBody formBody;
    Request request;
    Response response;
    private static final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //......getting the data from the user interface.......



      //  res = (TextView) findViewById(R.id.response);


        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.loginbtn :
                        emailPhone = email_or_phone_view.getText().toString();
                        password = password_view.getText().toString();
                        DriverLogin();
                        break;

                }



            }
        };

        loginbtn.setOnClickListener(l);






    }

    public void DriverLogin() {




    if (emailPhone.length() >= 1 && password.length() >= 1) {


        if(isNetworkAvailable()) {
          /*/  LoginTask login = new LoginTask();
            login.execute(loginUrl, "secon params");
*/

            final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage(getString(R.string.loggin));
            dialog.show();

            String loginUrl="http://moxi.delis.co.tz/getDriverData.php";


            Response response;
            RequestBody body = new FormBody.Builder()
                    .add("login_data",emailPhone)
                    .add("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(loginUrl)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    dialog.dismiss();
                    Log.v(TAG, "error..");

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {


                        dialog.dismiss();

                        final String jsonData = response.body().string();
                        Log.v(TAG,"it worked.."+jsonData);


                        if (jsonData.equals("false")) {
                            //this where we handle the denial part.....

                            info.InformationDialogBuilder("Attention", "Invalid login in check your log in details",
                                    "TRY AGAIN", "con");
                            info.show(getFragmentManager(), "error_dialog");


                        } else {


                            try {


                      MoxiAuth(jsonData);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });







        }
        else{
            // here is where we handle the ui side if the user didnt fill anything..
            info.InformationDialogBuilder("Attention","No Internet Connection",
                    "OK","con");
            info.show(getFragmentManager(), "error_dialog");



        }


        }
        else{
// here is where we handle the ui side if the user didnt fill anything..
            info.InformationDialogBuilder("Attention","Please fill the details to login",
                    "OK","con");
            info.show(getFragmentManager(), "error_dialog");


        }



}








    //----------------------working with the background task here.. -------------




   public void MoxiAuth(String s) throws  Exception{

      DriverData drivedata = new DriverData();
       JSONObject driverJsonData = new JSONObject(s);


       drivedata.setDriver_id(Integer.parseInt(driverJsonData.getString("Driver_id")));
       drivedata.setDriver_name(driverJsonData.getString("Driver_name"));
       drivedata.setDriver_surname(driverJsonData.getString("Driver_surname"));
       drivedata.setDriver_phone_number(driverJsonData.getString("Driver_phone_number"));
       drivedata.setDriver_License_id(driverJsonData.getString("Driver_License_id"));
       drivedata.setDriver_email(driverJsonData.getString("Driver_email"));

       mdataSource.open();
       mdataSource.insertDriverData(drivedata.getDriver_id(),drivedata.getDriver_name(),drivedata
               .getDriver_surname(),drivedata.getDriver_phone_number(),drivedata
               .getDriver_License_id(),drivedata.getDriver_email(),password,emailPhone);
       mdataSource.updateUseStatus("1");


       mdataSource.close();


       Intent driverUI = new Intent(LoginActivity.this,driverArea.class);


       startActivity(driverUI);




    /*   if(re.equals("1")){


           mdataSource.open();
           DriverData loginDetail = new DriverData();

            mdataSource.updateUseStatus("1");

            mdataSource.insertDriverLoginDetails(emailPhone, password);

           mdataSource.close();

           Intent driverUI = new Intent(LoginActivity.this,driverArea.class);
           driverUI.putExtra("login_details",emailPhone);
           driverUI.putExtra("login_password",password);


           startActivity(driverUI);

       }
       else{



       }


*/


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
