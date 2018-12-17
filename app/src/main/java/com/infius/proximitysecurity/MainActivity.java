package com.infius.proximitysecurity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.infius.proximitysecurity.activities.HomeActivity;
import com.infius.proximitysecurity.activities.ScannerActivity;
import com.infius.proximitysecurity.utilities.AppConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHomeActivity();
    }

    private void openHomeActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                MainActivity.this.finish();
            }
        }, AppConstants.SPLASH_TIMEOUT);
    }
}
