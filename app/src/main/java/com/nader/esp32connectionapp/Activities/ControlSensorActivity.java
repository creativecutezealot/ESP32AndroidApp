package com.nader.esp32connectionapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nader.esp32connectionapp.R;
import com.nader.esp32connectionapp.Service.UploadDataAPI;
import com.nader.esp32connectionapp.Utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

public class ControlSensorActivity extends AppCompatActivity {
    private TextView  txtSensor1,txtSensor2,txtVoltage,txtAutoValue,txtManualValue,btnSendData, btnResult, btnClose;
    private Spinner spinner,spinnerDevice,spinnerLocation;
    String names[] = {"Send only Sensor1","Send only Sensor2","Send arithmetic Sensor1 & Sensor2","send manual value"};
    String locations[] = {"location1","location2","location3","location4"};
    String devices[] = {"device1","device2","device3","device4"};
    ArrayAdapter<String> arrayAdapter,deviceArrayAdapter, locationArrayAdapter;
    private ConnectedThread mConnectedThread;
    Handler bluetoothIn;
    final int handlerState = 0;
    private StringBuilder recDataString = new StringBuilder();
    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private int pgsValue=0, pgsKind;
//    private boolean beginState=false;
//    private int seconds, minutes, hour;
//    Timer timer;
    private String address = null;
    private String name = null;
    int data = 0;
    int locationPosition;
    int devicePosition;
    private String receviedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_sensor);
        initView();
        onSendData();
        onResult();
        onClose();
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                           // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        String checkstr=dataInPrint.replace("\r\n", "");
                        //pgsValue=Integer.parseInt(dataInPrint.replace("\r\n", ""));
                        pgsKind=Integer.parseInt(checkstr.substring(0,1));
                        pgsValue=Integer.parseInt(checkstr.substring(1));
//                        if( (pgsKind & 0x01)==0){
//                            pgsBarHead.setProgress(pgsValue);
//                        }
//                        if( ((pgsKind>>1) & 0x01)==0){
//                            pgsBarNeck.setProgress(pgsValue);
//                        }
//                        if( ((pgsKind>>2) & 0x01)==0){
//                            pgsBarWaist.setProgress(pgsValue);
//                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data

                        dataInPrint = " ";
                    }
                }
            }
        };
    }
    private void initView(){
        txtSensor1 = findViewById(R.id.txtSensor1);
        txtSensor2 = findViewById(R.id.txtSensor2);
        txtVoltage = findViewById(R.id.txtVoltage);
        txtManualValue = findViewById(R.id.txtManualValue);
        txtAutoValue = findViewById(R.id.txtAutoVale);
        btnSendData = findViewById(R.id.btnSendData);
        btnResult = findViewById(R.id.btnResult);
        btnClose = findViewById(R.id.btnClose);
        spinner = findViewById(R.id.spinner);
        spinnerDevice = findViewById(R.id.spinnerDevice);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"selection is "+ names[position],Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deviceArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,devices);
        spinnerDevice.setAdapter(deviceArrayAdapter);
        spinnerDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                devicePosition = position +1 ;

//                Toast.makeText(getApplicationContext(),"selection is "+ devices[position],Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,locations);
        spinnerLocation.setAdapter(locationArrayAdapter);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationPosition = position +1;
//                Toast.makeText(getApplicationContext(),"selection is "+ locations[position],Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        address = Constant.deviceAddress;
        name = Constant.deviceName;
//        txtDevice.setText(name);
        BluetoothDevice device = myBluetooth.getRemoteDevice(address);
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();

        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread =  new ConnectedThread(btSocket);
        mConnectedThread.start();

    }
    @Override
    public void onPause() {
        super.onPause();
        try
        {
            mConnectedThread.write("Init");
            btSocket.close();

        } catch (IOException e) {
        }

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(myUUID);
    }
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    if (readMessage.equals("Sensor1:701,Sensor2:703,voltage:8.5")){
                        receviedData = readMessage;
                        String[] subStr = readMessage.split(",");
                        String sensor1Val = subStr[0].split(":")[1];
                        String sensor2Val = subStr[1].split(":")[1];
                        String voltageVal = subStr[2].split(":")[1].split("\r\n")[0];
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), readMessage, Toast.LENGTH_LONG).show();
                                txtSensor1.setText(sensor1Val);
                                txtSensor2.setText(sensor2Val);
                                txtVoltage.setText(voltageVal);
                                int average = (Integer.parseInt(sensor1Val) + Integer.parseInt(sensor2Val))/2;
                                txtAutoValue.setText(String.valueOf(average));
                            }
                        });
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String input) {
            try {

                mmOutStream.write(input.toString().getBytes());
            } catch (IOException e) {

                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
    private void onSendData(){
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataoption = spinner.getSelectedItem().toString();
                String deviceId = spinnerDevice.getSelectedItem().toString();
                String edificeId = spinnerLocation.getSelectedItem().toString();
                if (dataoption.equals("Send only Sensor1")){
                    String sensor1Str = txtSensor1.getText().toString();
                    data = Integer.parseInt(sensor1Str);
                }else if (dataoption.equals("Send only Sensor2")){
                    String sensor2Str = txtSensor2.getText().toString();
                    data = Integer.parseInt(sensor2Str);
                }else if (dataoption.equals("Send arithmetic Sensor1 & Sensor2")){
                    String autoStr = txtAutoValue.getText().toString();
                    data = Integer.parseInt(autoStr);
                }else if (dataoption.equals("send manual value")){
                    String manualStr = txtManualValue.getText().toString();
                    if (manualStr.isEmpty()){
                        Toast.makeText(getBaseContext(), "Please enter manual value.", Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        data = Integer.parseInt(manualStr);
                    }
                }
                String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
                Constant.data = data;
                Constant.edificeId = locationPosition;
                Constant.deviceId = devicePosition;
                Constant.fetchDate = currentDate + "T" + currentTime;
                new UploadDataAPI(ControlSensorActivity.this,data, new UploadDataAPI.OnResultReceived() {
                    @Override
                    public void onResult(String result) throws IOException {
                        if (!result.isEmpty()) {
                            try {
                                JSONObject object = new JSONObject(result);
                                String status = object.getString("status");
                                if (Integer.parseInt(status) ==  400){
                                    String message = object.getString("message");
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("token", Constant.token);
                                        jsonObject.put("edificeId", locationPosition);
                                        jsonObject.put("deviceId", devicePosition);
                                        jsonObject.put("fetchDate", currentDate + "T" + currentTime);
                                        JSONArray dataArr = new JSONArray();
                                        JSONObject dataObject1 = new JSONObject();
                                        dataObject1.put("value", data);
                                        dataObject1.put("sensorId",Constant.sensorId);
                                        dataArr.put(dataObject1);
                                        jsonObject.put("data",dataArr);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String bodyStr = jsonObject.toString();
                                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                    String fileName = "espData"+timeStamp+"_"+".txt";
                                    Constant.fileName = fileName;
//                                    String filePath = ControlSensorActivity.this.getFilesDir().getPath().toString();
                                    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                                    try {
                                        File gpxfile = new File(filePath, fileName);
                                        FileWriter writer = new FileWriter(gpxfile);
                                        writer.append(bodyStr);
                                        writer.flush();
                                        writer.close();
                                        Constant.filePath = filePath;
                                        Intent intent = new Intent(ControlSensorActivity.this,ResultActivity.class);
                                        startActivity(intent);
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                }, true).execute();
            }
        });
    }
    private void onResult(){
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlSensorActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });
    }
    private void onClose(){
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
    }
}