package com.nader.esp32connectionapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nader.esp32connectionapp.R;
import com.nader.esp32connectionapp.Utils.Common;
import com.nader.esp32connectionapp.Utils.Preferences;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txtUserName, txtPassword, txtConfirmPassword;
    private TextView btnCreate, btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        onClickListner();
    }
    private void initView(){
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnCreate = findViewById(R.id.btnCreate);
        btnSignin = findViewById(R.id.btnSignin);
    }
    private void onClickListner(){
        btnSignin.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreate:
                String userName = txtUserName.getText().toString();
                String password  = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();
                if (userName.isEmpty()){
                    Common.showAlert(SignUpActivity.this, "Warning", "Please enter user name.");
                    return;
                }
                if (password.isEmpty()){
                    Common.showAlert(SignUpActivity.this, "Warning", "Please enter password.");
                    return;
                }
                if (confirmPassword.isEmpty()){
                    Common.showAlert(SignUpActivity.this, "Warning", "Please enter password.");
                    return;
                }
                if (!password.equals(confirmPassword)){
                    Common.showAlert(SignUpActivity.this, "Warning", "Please confirm password.");
                    return;
                }
                Preferences.setValue(SignUpActivity.this, Preferences.USER_NAME, userName);
                Preferences.setValue(SignUpActivity.this, Preferences.USER_PASSWORD, password);
                Intent intent_signin = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent_signin);
                finish();
                break;
            case R.id.btnSignin:
                finish();
                break;
        }

    }
}