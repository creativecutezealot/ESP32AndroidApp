package com.nader.esp32connectionapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nader.esp32connectionapp.Adapters.ScanDeviceAdapter;
import com.nader.esp32connectionapp.Models.DeviceModal;
import com.nader.esp32connectionapp.R;
import com.nader.esp32connectionapp.Utils.Constant;
import com.nader.esp32connectionapp.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.Set;

public class PairedDevicesActivity extends AppCompatActivity implements ScanDeviceAdapter.ItemClickListener{
    private RecyclerView deviceListView;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothAdapter myBluetooth = null;
    private LoadingDialog loadingDialog;
    private ScanDeviceAdapter scanDeviceAdapter;
    ArrayList<DeviceModal> devices;
    public static String EXTRA_ADDRESS = "device_address";
    public static String EXTRA_NAME = "device_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);
        initView();
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        scanDevice();
    }
    private void initView(){
        deviceListView = findViewById(R.id.deviceListView);
    }
    private void scanDevice(){
        loadingDialog = new LoadingDialog(PairedDevicesActivity.this, false);
        pairedDevices = myBluetooth.getBondedDevices();
        devices = new ArrayList<>();
        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                String deviceName = bt.getName();
                String deviceAddress = bt.getAddress();
                DeviceModal deviceModal = new DeviceModal(deviceName,deviceAddress);
                devices.add(deviceModal);
            }
            loadingDialog.hide();
            scanDeviceAdapter = new ScanDeviceAdapter(PairedDevicesActivity.this,devices);
            scanDeviceAdapter.setClickListener(PairedDevicesActivity.this);
            deviceListView.setLayoutManager(new LinearLayoutManager(PairedDevicesActivity.this));
            deviceListView.setAdapter(scanDeviceAdapter);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(getApplicationContext(),devices.get(position).getDeviceName() , Toast.LENGTH_LONG).show();
        Constant.deviceAddress = devices.get(position).getDeviceAddress();
        Constant.deviceName = devices.get(position).getDeviceName();
        Intent intent = new Intent(PairedDevicesActivity.this, ControlSensorActivity.class);
        startActivity(intent);
    }
}