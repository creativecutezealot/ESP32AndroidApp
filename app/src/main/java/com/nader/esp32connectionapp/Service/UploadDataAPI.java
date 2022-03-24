package com.nader.esp32connectionapp.Service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.nader.esp32connectionapp.Utils.Constant;
import com.nader.esp32connectionapp.Utils.LoadingDialog;

import java.io.IOException;

public class UploadDataAPI extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private boolean flagProgress;
    private LoadingDialog loadingDialog;
    private OnResultReceived mListner;
    private String url;
    private int data;

    public UploadDataAPI(Context mContext,int data, OnResultReceived mListner, boolean flagProgress) {
        this.mContext = mContext;
        this.mListner = mListner;
        this.data = data;
        this.flagProgress = flagProgress;
    }

    public interface OnResultReceived {
        void onResult(String result) throws IOException;
    }
    public void show() {
        if (flagProgress) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
    }

    private void hide() {
        if (flagProgress) {
            loadingDialog.hide();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(Void... unsued) {
        String response;
        try {
            this.url = Constant.url ;
            Log.e("URL", url);
            response = APIService.uploadData(url,data);
            Log.e(TAG, " Response: " + response);
        } catch (Exception e) {
            Log.e(TAG, "Error:... " + e.getMessage());
            response = APIService.RESPONSE_UNWANTED;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        hide();
        if (mListner != null) {
            try {
                mListner.onResult(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
