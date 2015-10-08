package hom.ourhome.asm301.wifimacchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WifiMacChangeActivity extends AsyncTask<String, String, String> {
    Context mContext = null;
    ProgressDialog progressdailog;

    public WifiMacChangeActivity(Context _ctx) {
        mContext = _ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressdailog = ProgressDialog.show(mContext,
                "Configuring Network Adapter", "Please Wait...");
    }

    @Override
    protected String doInBackground(String... params) {
        Process p;
        StringBuffer output = new StringBuffer();
        try {
            p = Runtime.getRuntime().exec("/system/xbin/su -c /system/bin/sh /system/xbin/WifiMacChange");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
                p.waitFor();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String response = output.toString();
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressdailog.dismiss();
        ((MainActivity) mContext).txtResult.setText(result); //
        Log.d("Output", result);
    }
}