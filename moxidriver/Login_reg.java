package tz.co.delis.www.moxidriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login_reg extends AppCompatActivity {




    private Button register;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

        login = (Button) findViewById(R.id.loginBtn);
        register = (Button) findViewById(R.id.registerBtn);


        View.OnClickListener l =new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(v==login)
                    login();
                else if(v==register)
                    register();





            }
        };



        login.setOnClickListener(l);







    }


    private void login(){



       Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
//create database
//create communication module with the server json way...
//read okhttp more
//connect with the php script
//create a ui dialog of errors and stuff





    }


    private void register(){


    }
}
