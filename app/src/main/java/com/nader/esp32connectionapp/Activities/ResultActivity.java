package com.nader.esp32connectionapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nader.esp32connectionapp.R;
import com.nader.esp32connectionapp.Service.UploadDataAPI;
import com.nader.esp32connectionapp.Utils.Constant;
import com.nader.esp32connectionapp.Utils.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView btnResendData, btnDeleteData, btnClose,btnSave,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
        listener();
    }
    private void initView(){
        btnResendData = findViewById(R.id.btnResend);
        btnDeleteData = findViewById(R.id.btnDelete);
        btnClose = findViewById(R.id.btnClose);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
    }
    private void listener(){
        btnResendData.setOnClickListener(this);
        btnDeleteData.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnResend:
                new UploadDataAPI(ResultActivity.this, Constant.data, new UploadDataAPI.OnResultReceived() {
                    @Override
                    public void onResult(String result) {
                        if (!result.isEmpty()) {
                            try {
                                JSONObject object = new JSONObject(result);
                                String status = object.getString("status");
                                if (Integer.parseInt(status) ==  400){
                                    String message = object.getString("message");
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getBaseContext(), "Resending Data is success", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "No server connection available. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                }, true).execute();

                break;
            case R.id.btnDelete:
                Constant.data = 0;
                File fdelete = new File(Constant.filePath, Constant.fileName);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Toast.makeText(getBaseContext(), "Data is deleted successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Unfortunetely,Data is not deleted. please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btnSave:
                Toast.makeText(getBaseContext(), "Data is already downloaded, Please check Download folder.", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnClose:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                break;
            case R.id.btnBack:
                Intent intent_back = new Intent(ResultActivity.this,ControlSensorActivity.class);
                startActivity(intent_back);
        }
    }
}