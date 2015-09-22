package com.adamino.ajob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.altbeacon.beacon.BeaconManager;

/**
 * Created by Adam on 22-09-2015.
 */
public class aJobActivity extends AppCompatActivity{
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

       }
  }