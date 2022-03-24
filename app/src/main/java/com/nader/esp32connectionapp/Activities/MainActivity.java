package com.nader.esp32connectionapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ficat.easyble.BleManager;
import com.nader.esp32connectionapp.R;

public class MainActivity extends AppCompatActivity {
    private TextView btnfind,btnResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnfind = findViewById(R.id.btnFind);
        btnResult = findViewById(R.id.btnResult);
        btnfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BleManager.isBluetoothOn()) {
                    BleManager.toggleBluetooth(true);
                }
                Intent intent = new Intent(MainActivity.this,PairedDevicesActivity.class);
                startActivity(intent);
            }
        });
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                startActivity(intent);
            }
        });

    }
    private boolean isGpsOn() {
        LocationManager locationManager
                = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}