package tz.co.delis.www.moxidriver;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import tz.co.delis.www.moxidriver.db.MoxiDataSource;

public class MSettings extends AppCompatActivity {

    @Bind(R.id.buttonSetting)
    Button settingBtn;
    Resources res;
    String newLang;
    protected MoxiDataSource  mDataSource= new MoxiDataSource(this);
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msettings);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settingsTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


         spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.langz, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        res = this.getResources();

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newLang=spinner.getSelectedItem().toString();


                mDataSource.open();
                if (newLang.equals("Turkish")) {



                    mDataSource.updateLanguage("tr");




                    changeTurkish();



                }

                else {

                   mDataSource.updateLanguage("eng");

                    changeEnglish();
               }

mDataSource.close();
            }
        });




    }

    private void changeEnglish() {

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        this.finish();
        Intent refresh = new Intent(this, driverArea.class);
        startActivity(refresh);
    }

    private void changeTurkish() {

        Locale locale = new Locale("tr");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        this.finish();
        Intent refresh = new Intent(this, driverArea.class);
        startActivity(refresh);
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
