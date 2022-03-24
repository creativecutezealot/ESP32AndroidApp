package com.nader.esp32connectionapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nader.esp32connectionapp.R;
import com.nader.esp32connectionapp.Utils.Common;
import com.nader.esp32connectionapp.Utils.Preferences;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txtUserName, txtPassword;
    private TextView  btnSignin;
    private static final int PERMISSONS_REQUEST_CODE= 1240;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        onClickListener();
        isPermissionGranted();
    }
    private void isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermission()){
                initView();
            } else {
                requestPermission(SignInActivity.this);
            }
        } else {
            initView();
        }
    }
    public void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSONS_REQUEST_CODE);
    }
    public boolean checkPermission() {
        int StorageResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storageReadResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return StorageResult == PackageManager.PERMISSION_GRANTED && storageReadResult == PackageManager.PERMISSION_GRANTED;
    }
    private void initView(){
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        btnSignin = findViewById(R.id.btnSignin);
//        btnCreat = findViewById(R.id.btnCreate);

    }
    private void onClickListener(){
        btnSignin.setOnClickListener(this);
//        btnCreat.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignin:
                String userName = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                if (userName.isEmpty()){
                    Common.showAlert(SignInActivity.this, "Warning", "Please enter User Name");
                    return;
                }
                if (password.isEmpty()){
                    Common.showAlert(SignInActivity.this, "Warning", "Please enter Password");
                    return;
                }
                String savedUserName = Preferences.getValue_String(SignInActivity.this, Preferences.USER_NAME);
                String savedPassword = Preferences.getValue_String(SignInActivity.this, Preferences.USER_PASSWORD);
                if (userName.equals("espapp")){
                    if (password.equals("password123")){
                        Intent intent_signin = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent_signin);
                        finish();
                    }else {
                        Common.showAlert(SignInActivity.this, "Warning", "Wrong Password. Please enter valid Password");
                        return;
                    }
                }else {
                    Common.showAlert(SignInActivity.this, "Warning", "Wrong User Name. Please enter valid user name");
                    return;
                }
                break;
//            case R.id.btnCreate:
//                Intent intent_creat = new Intent(SignInActivity.this,SignUpActivity.class);
//                startActivity(intent_creat);
//                break;
        }

    }
}