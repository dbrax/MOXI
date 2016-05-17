package tz.co.delis.www.moxidriver;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import tz.co.delis.www.moxidriver.db.MoxiDataSource;

public class LanguageSelector extends AppCompatActivity {

    protected MoxiDataSource mdataSource=new MoxiDataSource(this);


    public TextView english,turkish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selector);


        mdataSource.open();




        english = (TextView) findViewById(R.id.english);
        turkish = (TextView) findViewById(R.id.turkish);



        View.OnClickListener l =new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(v==english)
                    switchEnglish();
                else
                switchTurkish();





            }
        };


        turkish.setOnClickListener(l);
        english.setOnClickListener(l);

    }


    private void   switchEnglish(){


        mdataSource.insertLanguage("eng");

        Intent myIntent = new Intent(this, Login_reg.class);
        startActivity(myIntent);
        mdataSource.close();

    }


    private void   switchTurkish(){

        mdataSource.insertLanguage("tr");

        Resources res = this.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale("tr");
        res.updateConfiguration(conf, dm);

        Intent myIntent = new Intent(this, Login_reg.class);
        startActivity(myIntent);
        mdataSource.close();



    }





}
