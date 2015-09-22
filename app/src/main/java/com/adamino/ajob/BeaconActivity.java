package com.adamino.ajob;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    public static final String TAG = "BeaconsEverywhere";
    private BeaconManager beaconManager;
    private BackgroundPowerSaver backgroundPowerSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        BeaconManager.setBeaconSimulator(new BeaconSimulator());
        ((BeaconSimulator) BeaconManager.getBeaconSimulator()).createBasicSimulatedBeacons();

        Button beaconButton = (Button) findViewById(R.id.beaconButton);
        beaconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    beaconManager.startMonitoringBeaconsInRegion(new Region("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A", null, null, null));
                } catch (RemoteException e) {
                }

            }
        });

        Button beaconInfoButton = (Button) findViewById(R.id.beaconInfoButton);
        beaconInfoButton.setVisibility(View.GONE);
        beaconInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (BeaconActivity.this, aJobActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView beaconView = (TextView) findViewById(R.id.beaconView);
                        beaconView.setText("I just saw a new beacon!");
                        Button beaconInfoButton = (Button) findViewById(R.id.beaconInfoButton);
                        beaconInfoButton.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void didExitRegion(Region region) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView beaconView = (TextView) findViewById(R.id.beaconView);
                        beaconView.setText("I no longer see a beacon...");
                    }
                });
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView beaconView = (TextView) findViewById(R.id.beaconView);
                        beaconView.setText("I no longer see a beacon...");
                    }
                });

            }

        });
    }
}
