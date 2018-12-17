package com.infius.proximitysecurity.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.fragments.ScannerFragment;

public class ScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_activity);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new ScannerFragment(), "tag").commit();

    }
}
