package com.praveenbhati.agrovision;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.praveenbhati.agrovision.database.Database;
import com.praveenbhati.agrovision.util.PrefManager;

public class SplashActivity extends AppCompatActivity {
    PrefManager prefManager;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefManager = new PrefManager(this);
        database = new Database(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("value", String.valueOf(prefManager.getUserID()));
                Log.i("value", String.valueOf(prefManager.getUserName()));
                Log.i("value", String.valueOf(prefManager.getLogin()));

                //String login = prefManager.getLogin();
                if (prefManager.getLogin()){
                    Intent intentDashboard = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intentDashboard);
                    finish();
                }else {
                    Intent intentLogin = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
            }
        },1500);
    }
}
